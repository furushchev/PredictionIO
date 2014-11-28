package io.prediction.engines.movierec

import scala.collection.mutable.ListBuffer

class Rating(
    val uindex: Int,
    val mindex: Int,
    val rating: Float
  ) extends Serializable {
  override def toString = s"User: ${uindex} rates Movie: ${mindex} (${rating} / 5)"
}

class User(
    val uid: String,
    val age: Int,
    val gender: String,
    val occupation: String,
    val zip: String
  ) extends Serializable {
  override def toString = s"UserID: ${uid}, Age: ${age}, Gender: ${gender}" +
                          s", Occupation: ${occupation}, Zip: ${zip}"
}

class Movie(
    val mid: String,
    val title: String,
    val year: String,
    val genre: Genre,
    val directors: Seq[String],
    val writers: Seq[String],
    val actors: Seq[String],
    val runtimes: String,  // TODO: in minutes string for now due to Canada:108
    val countries: Seq[String],
    val languages: Seq[String],
    val certificates: Seq[String],
    val plot: String
  ) extends Serializable {
  override def toString = s"Movie: ${title}, ID: ${mid}, Year: ${year}" +
                          s", Genre: ${genre}" +
                          s"\n\n\tDirectors: ${directors}" +
                          s",\n\tWriters: ${writers}" +
                          s",\n\tActors: ${actors}" +
                          s"\n\n\tRuntimes: ${runtimes}" +
                          s", Countries: ${countries}" +
                          s", Languages: ${languages}" +
                          s", Certificates: ${certificates}" +
                          s"\n\n\tPlot: ${plot}\n"
}

class TrainingData (
    val ratings: Seq[Rating],
    val users:   Map[Int, User],
    var movies:  Map[Int, Movie]
  ) extends Serializable {

  override def toString(): String = {
    if (ratings.length > 20) {
      s"TrainingData.size=${ratings.length}"
    } else {
      ratings.toString
    }
  }
}

// Prepared Movie data for training
// mtypes contain Movie's features/properties
class PreparedMovie (
  val mid: String,
  val mtypes: Seq[String]
  ) extends Serializable {
  override def toString = s"${mid}"
}

class PreparedData (
    val ratings: Seq[Rating],
    val users:   Map[Int, User],
    var movies:  Map[Int, PreparedMovie]
  ) extends Serializable {

  override def toString(): String = {
    if (ratings.length > 20) {
      s"PreparedData.size=${ratings.length}"
    } else {
      ratings.toString
    }
  }
}

object Genre {

  val genreList = Seq("Unknown", "Action", "Adventure", "Animation",
                "Childrens", "Comedy", "Crime", "Documentary", "Drama",
                "Fantasy", "FilmNoir", "Horror", "Musical", "Mystery",
                "Romance", "SciFi", "Thriller", "War", "Western")

  val numGenres = genreList.size

  // if needed
  // val gmap = genreList.zipWithIndex.toMap

  // Genre factory method
  def apply(binaryGenreList: Seq[String]):Genre = {
    var gi = 0
    var gl = new ListBuffer[String]()
    for(i <- 0 until numGenres) {
      val bit = binaryGenreList(i).toInt & 1
      if (bit == 1) {
        gl += genreList(i)
      }
      gi |= bit << i
    }
    new Genre(gl, gi)
  }
}

class Genre(
    val genreList: Seq[String],
    val genreInt: Int
  ) extends Serializable {
  override def toString = genreInt.toBinaryString
}