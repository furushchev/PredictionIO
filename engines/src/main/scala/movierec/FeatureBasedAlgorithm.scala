/** Copyright 2014 TappingStone, Inc.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package io.prediction.engines.movierec

import java.util.NoSuchElementException
import io.prediction.controller._

import nak.classify.NaiveBayes
import breeze.linalg.Counter

import nak.data.Example

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap



// FIXME. For now, we don't save the model.
case class FeatureBasedModel(
  val features: Array[String] = Array[String](),
  val userClassifierMap: Map[String, NaiveBayes[Boolean, String]] =
    Map[String, NaiveBayes[Boolean, String]](),
  val movieFeaturesMap: Map[String, Counter[String, Double]] =
    Map[String, Counter[String, Double]](),
  val featureMoviesMap: Map[String, Seq[String]] = 
    Map[String, Seq[String]]())
extends Serializable {
  override def toString = "FeatureBasedModel: " +
    s"features (size = ${features.size}) = [${features.take(3).toSeq}, ...], " +
    s"userClassifierMap (size = ${userClassifierMap.size}) " +
    s"= {${userClassifierMap.take(3).toSeq}, ...}, " +
    s"movieFeaturesMap (size = ${movieFeaturesMap.size}) " +
    s"= {${movieFeaturesMap.take(3).toSeq}, ...}, " + 
    s"featureMoviesMap (size = ${featureMoviesMap.size}) " +
    s"= {${featureMoviesMap.take(3).toSeq}}"
}

// FeatureBaseAlgorithm use all mtypes as features.
class FeatureBasedAlgorithm
  extends LAlgorithm[EmptyParams, PreparedData, FeatureBasedModel,
      Query, Prediction] {

  def train(data: PreparedData): FeatureBasedModel = {
    val featureCounts = data.movies
      .flatMap{ case(mindex, movie) => movie.mtypes }// All mtypes strings together in ArrayBuffer
      .groupBy(identity)// e.g. Map(Animation -> ArrayBuffer(Animation, Animation, Animation), ....
      .mapValues(_.size)// e.g. Map(Animation -> 3, ...[3 is the number of Animations]


    val features: Seq[String] = featureCounts.toSeq.sortBy(-_._2).map(_._1)
    //+toSeq:  e.g. ((Animation, 3), ...)
    //+sortBy(-_._2) Decreasing Order Sort: e.g. ((Animation, 3), (Unknown, 2), ...)
    //+map(_._1): create list from key: e.g. (Animation, Unknown, ...) (First elem occurs most)


    val featureMovies: HashMap[String, ListBuffer[String]] = HashMap[String, ListBuffer[String]]()

    data.movies.map { case(mindex, movie) => {
      for (mtype <- movie.mtypes) {
        if (featureMovies.contains(mtype)) {
          val buf: ListBuffer[String] = featureMovies(mtype)
          buf += movie.mid
        } else {
          val buf = new ListBuffer[String]()
          buf += movie.mid
          featureMovies.put(mtype, buf)
        }
      }
    }}

    val featureMoviesMap: Map[String, Seq[String]] =
    featureMovies.map { case(feature, buf) => {
      (feature, buf.toSeq)
    }}
    .toMap
    //e.g. Map(Michael Apted -> List(730, 619, 729, 621, 620, 1248), ...    


    // one model/classifier for each user in Naive Bayes
    val moviesSet = data.movies.keySet
    // Map from uid to mid that user bought
    val conversionsMap: Map[Int, Set[Int]] = data.ratings.groupBy(_.uindex)
      .mapValues(_.map(_.mindex).toSet)

    //println("CONVERSION: " + conversionsMap)
    //val rM: Map[Int, Set[Int]] = data.ratings.groupBy(_.mindex)
    //  .mapValues(_.map(_.uindex).toSet)
    //println("REVERse: " + rM)

    // mindex to feature counter map
    val movieFeaturesMap: Map[String, Counter[String, Double]] =
    data.movies.map { case(mindex, movie) => {
      val features = Counter[String, Double]()
      for (mtype <- movie.mtypes) {
        features(mtype) = 1.0
      }
      (data.movies(mindex).mid, features)
    }}
    .toMap

    val trainer = new NaiveBayes.Trainer[Boolean, String]

    val userClassifierMap: Map[String, NaiveBayes[Boolean, String]] =
    conversionsMap.map { case (uindex, mindicies) => {

      // Construct the iterable for training a model for this user
      val positiveExamples: Seq[Example[Boolean, Counter[String, Double]]] =
        mindicies.map { mindex => Example(label=true, features=movieFeaturesMap(data.movies(mindex).mid)) }.toSeq
      val negativeExamples: Seq[Example[Boolean, Counter[String, Double]]] =
        (moviesSet -- mindicies).filter(_ % 101 == uindex % 101).map { mindex => Example(label=false, features=movieFeaturesMap(data.movies(mindex).mid)) }.toSeq
      val examples = positiveExamples ++ negativeExamples

      // build the model
      (data.users(uindex).uid, trainer.train(examples.toIterable))
    }}
    .toMap

    FeatureBasedModel(
      featureCounts.keys.toArray,
      userClassifierMap,
      movieFeaturesMap,
      featureMoviesMap
    )
  }

  def predict(model: FeatureBasedModel, query: Query): Prediction = {
    val (movies, isOriginal): (Seq[(String, Double)], Boolean) = (
      if (model.userClassifierMap.contains(query.uid)) {
        var movies: Seq[(String, Double)] = null
        if (query.mids.size > 0) { // rank movies for a user
          movies = query.mids
            .map { mid => {
              if (model.movieFeaturesMap.contains(mid)) {
                (mid, model.userClassifierMap(query.uid).scores(model.movieFeaturesMap(mid))(true))
              } else {
                (mid, 0.0) // movie not found
              }
            }}
            .sortBy(-_._2)
        } else if (query.mtypes.size > 0) {// recommend movies based on features typed in
          
          var movieIds: ListBuffer[String] = new ListBuffer[String]()
          var mids: Seq[String] = Seq[String]()

          if (query.display.size == 0 || query.display.head == "Union") {
            var i = 0
            for (i <- 0 until query.mtypes.size) {
              
              try {
                movieIds = movieIds union model.featureMoviesMap(query.mtypes(i))//union == ++
              } catch {
                case e: NoSuchElementException => println("Exception on mtype parsing: " + movieIds.toString)
              }
            }
          } else {// if(query.display.head == "Intersect"){
            var i = 0 
            for (i <- 0 until query.mtypes.size) {
              
              try {
                if (i == 0) {
                  movieIds = movieIds union model.featureMoviesMap(query.mtypes(i))
                } else {
                  movieIds = movieIds intersect model.featureMoviesMap(query.mtypes(i))//union == ++
                }
              } catch {
                case e: NoSuchElementException => println("Exception on mtype parsing: " + movieIds.toString)
              }
            }       
          }

          mids = movieIds.toSeq
 
          if (mids.size > 0) {
            movies = mids
              .map { mid => {
                  (mid, model.userClassifierMap(query.uid).scores(model.movieFeaturesMap(mid))(true))
              }
            }         
            .sortBy(-_._2)

            // Get top ${top} movies
            if (query.top.size > 0) {
              movies = movies.take(query.top.head)
            }

          } else { // No movie in these categories or error mtypes
            movies = Seq(("No movie found", 0.0))
          }
          
        } else if (query.top.size > 0) { // get top ${top} movies
          movies =
            (for { mid <- model.movieFeaturesMap.keySet }
              yield (mid, model.userClassifierMap(query.uid).scores(model.movieFeaturesMap(mid))(true))
            )
            .toSeq
            .sortBy(-_._2)
            .take(query.top.head)
        }

        (movies, false)
      } else {
        // if user not found, use input order.
        (query.mids.map { mid => (mid, 0.0) }, true)
      }
    )
    new Prediction(movies, isOriginal)
  }
}