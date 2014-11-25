package io.prediction.engines.movierec

import io.prediction.controller.LPreparator
import io.prediction.controller.Params
import io.prediction.controller.EmptyParams

class MovieRecPreparator()
    extends LPreparator[EmptyParams, TrainingData, PreparedData] {

  override def prepare(td: TrainingData): PreparedData = {

    val preparedMovies: Map[Int, PreparedMovie] = td.movies
      .map{ case(mid, movie) =>
        var mtypes: Seq[String] =
            Seq(Seq(movie.year), movie.genre.getGenreList,
                movie.directors, movie.writers,
                //movie.actors, movie.countries,
                movie.languages).flatten
        //TODO: we call prepare 5 times currectly, can we eliminate times?
        mtypes = mtypes.distinct

        //println(movie.mid + " " + mtypes.toString)

        (mid, new PreparedMovie(movie.mid, mtypes))
      }

    new PreparedData(td.ratings, td.users, preparedMovies)
  }
}