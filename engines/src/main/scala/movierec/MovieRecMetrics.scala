package io.prediction.engines.movierec

import io.prediction.engines.base
import io.prediction.engines.base.BinaryRatingParams
import io.prediction.engines.base.MetricsHelper
import io.prediction.engines.base.MetricsOutput
import io.prediction.controller.Metrics
import io.prediction.controller.Params
import io.prediction.controller.EmptyParams
import io.prediction.engines.base.HasName


import com.github.nscala_time.time.Imports._
import scala.math.BigDecimal
import breeze.stats.{ mean, meanAndVariance }

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.{read, write}
import org.json4s.native.Serialization

import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.FileInputStream
import java.io.ObjectInputStream

import scala.io.Source
import java.io.PrintWriter
import java.io.File

import io.prediction.engines.util.{ MetricsVisualization => MV }

import scala.util.hashing.MurmurHash3
import scala.collection.immutable.NumericRange
import scala.collection.immutable.Range
import io.prediction.engines.base.Stats

import scala.util.Random
import scala.collection.mutable.{ HashSet => MHashSet }
import scala.collection.immutable.HashSet

class MovieRecMetricsParams (
  val name: String = "",
  val buckets: Int = 10,
  val ratingParams: BinaryRatingParams, //only need goodThreshold though
  val measureType: MeasureType.Type = MeasureType.PrecisionAtK,
  val measureK: Int = -1
) extends Params {}

object MeasureType extends Enumeration {
  type Type = Value
  val PrecisionAtK = Value
}

class MetricsUnit (
  val q: Query,
  val p: Prediction,
  val a: Actual,
  val score: Double,
  val baseline: Double, 
  val uidHash: Int = 0
  ) extends Serializable

abstract class MovieRecMeasure extends Serializable {
  def calculate(query: Query, prediction: Prediction, actual: Actual): Double
}

class MovieRecPrecision(val k: Int, val ratingParams: BinaryRatingParams) 
  extends MovieRecMeasure {
  override def toString(): String = s"Precision@$k"
  
  def calculate(query: Query, prediction: Prediction, actual: Actual)
  : Double = {
    val kk = (if (k == -1) query.mids.size else k)

    // Returns a set of string which is mid of the rating (actionTuple's second string)
    val actualItems = 
        actual.ratings
          .filter{
            r => {
              r.rating >= ratingParams.goodThreshold
            }
          }
          .map(_.mindex)
          .toSet
      //MetricsHelper.actions2GoodIids(
      //actual.actionTuples, ratingParams)

    // See how many good movies(mid) exist in actual are in prediction (in first kk movies)
    val relevantCount = prediction.movies.take(kk)
      .map(_._1)
      .filter(mid => actualItems(mid.toInt))
      .size

    // The min value of the three values in the seq
    val denominator = Seq(kk, prediction.movies.size, actualItems.size).min

    // Score
    relevantCount.toDouble / denominator
  }
}


class MovieRecMetrics(params: MovieRecMetricsParams) 
  extends Metrics[MovieRecMetricsParams, HasName,
      Query, Prediction, Actual,
      MetricsUnit, Seq[MetricsUnit], MetricsOutput] {
  
  val measure: MovieRecMeasure = params.measureType match {
    case MeasureType.PrecisionAtK => {
      new MovieRecPrecision(params.measureK, params.ratingParams)
    }
    case _ => {
      throw new NotImplementedError(
        s"MeasureType ${params.measureType} not implemented")
    }
  }

  
  override def computeUnit(query: Query, prediction: Prediction, actual: Actual)
  : MetricsUnit = {
    val score = measure.calculate(query, prediction, actual)

    // Compute the hash of a string
    val uidHash: Int = MurmurHash3.stringHash(query.uid)

////////////////////////////////TODO
    // Create a baseline by randomly picking query.n(query.mids.size) items from served items.
    
    val rand = new Random(seed = uidHash)
    //val iidSet = HashSet[String]()

    val servedMids = actual.servedMids

    //val n = math.min(query.n, servedIids.size)

    val randMids = rand.shuffle(
      (if (query.mids.size > servedMids.size) {
          // If too few items where served, use only the served items
          servedMids.toSet
        } else {
          val tempSet = MHashSet[String]()
          while (tempSet.size < query.mids.size) {
            tempSet.add(servedMids(rand.nextInt(query.mids.size)))
          }
          tempSet.toSet
        }
      )
      .toSeq
      .sorted
    )

    println(randMids.mkString("Randmid: [", ", ", "]"))

    // TODO Implement baseline
    val baseline = measure.calculate(
      query,
      Prediction(randMids.map(mid => (mid, 0.0)), false),
      actual)

    new MetricsUnit(
      q = query,
      p = prediction,
      a = actual,
      score = score,
      baseline = baseline,
      uidHash = uidHash
    )
  }
  
////////////////////////code from ItemRec

  override def computeSet(dataParams: HasName,
    metricUnits: Seq[MetricsUnit]): Seq[MetricsUnit] = metricUnits
  
  override def computeMultipleSets(
    rawInput: Seq[(HasName, Seq[MetricsUnit])]): MetricsOutput = {
    // Precision is undefined when the relevant items is a empty set. need to
    // filter away cases where there is no good items.
    val input = rawInput.map { case(name, mus) => {
      (name, mus.filter(mu => (!mu.score.isNaN && !mu.baseline.isNaN)))
    }}

    val allUnits: Seq[MetricsUnit] = input.flatMap(_._2)
    
    val overallStats = (
      "Overall",
      MetricsHelper.calculateResample(
        values = allUnits.map(mu => (mu.uidHash, mu.score)),
        buckets = params.buckets),
      MetricsHelper.calculateResample(
        values = allUnits.map(mu => (mu.uidHash, mu.baseline)),
        buckets = params.buckets)
      )

    val runsStats: Seq[(String, Stats, Stats)] = input
    .map { case(dp, mus) =>
      (dp.name,
        MetricsHelper.calculateResample(
          values = mus.map(mu => (mu.uidHash, mu.score)),
          buckets = params.buckets),
        MetricsHelper.calculateResample(
          values = mus.map(mu => (mu.uidHash, mu.baseline)),
          buckets = params.buckets))
    }
    .sortBy(_._1)
    

    val aggregateByActualGoodSize = aggregateMU(
      allUnits,
      mu => MetricsHelper.groupByRange(
        Array(0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144), "%.0f")(
          mu.a.ratings.filter {
            r => {
              r.rating >= params.ratingParams.goodThreshold 
            }
          }
          .map(_.mindex)
          .toSet
          .size))


      //MetricsHelper.actions2GoodIids(
      //actual.actionTuples, ratingParams)



    val outputData = MetricsOutput (
      name = params.name,
      metricsName = "ItemRecMetrics",
      measureType = measure.toString,
      algoMean = overallStats._2.average,
      algoStats = overallStats._2,
      //heatMap = heatMap,
      runs = Seq(overallStats) ++ runsStats,
      aggregations = Seq[(String, Seq[(String, Stats)])](
        ("By # of Positively Rated Items", aggregateByActualGoodSize)
      )
    )

    // FIXME: Use param opt path
    /*
    params.optOutputPath.map { path =>
      MV.save(outputData, path)
    }
    */

    outputData
  }

  def aggregateMU(units: Seq[MetricsUnit], groupByFunc: MetricsUnit => String)
  : Seq[(String, Stats)] = {
    MetricsHelper.aggregate[MetricsUnit](units, _.score, groupByFunc)
  }
}
