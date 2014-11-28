package io.prediction.engines.movierec

import io.prediction.controller.LPreparator
import io.prediction.controller.Params
import io.prediction.controller.EmptyParams
import scala.collection.mutable.ListBuffer

class MovieRecPreparator()
    extends LPreparator[EmptyParams, TrainingData, PreparedData] {

  override
  def prepare(td: TrainingData): PreparedData = {

    val preparedMovies: Map[Int, PreparedMovie] = td.movies
      .map{ case(mid, movie) =>
        var mtypes: ListBuffer[String] = new ListBuffer[String]()
        mtypes += movie.year
        mtypes ++= movie.genre.genreList
        mtypes ++= movie.directors
        mtypes ++= movie.writers
        mtypes ++= movie.actors
        mtypes ++= movie.countries
        mtypes ++= movie.languages
        mtypes = mtypes.distinct

        //TODO: we call prepare 5 times currectly, can we eliminate times?
        //println(movie.mid + " " + mtypes.toString)

        (mid, new PreparedMovie(movie.mid, mtypes))
      }

    new PreparedData(td.ratings, td.users, preparedMovies)
  }
}