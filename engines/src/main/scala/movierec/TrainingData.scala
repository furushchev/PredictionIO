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
    val zip: String,
    val gender: String,
    val occupation: String
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
                          s", Genre: ${genre.toString}" +
                          s"\n\n\tDirectors: ${directors.toString}" +
                          s",\n\tWriters: ${writers.toString}" +
                          s",\n\tActors: ${actors.toString}" +
                          s"\n\n\tRuntimes: ${runtimes}" +
                          s", Countries: ${countries.toString}" +
                          s", Languages: ${languages.toString}" +
                          s", Certificates: ${certificates.toString}" +
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

  val genreList = Array("Unknown", "Action", "Adventure", "Animation",
                "Childrens", "Comedy", "Crime", "Documentary", "Drama",
                "Fantasy", "FilmNoir", "Horror", "Musical", "Mystery",
                "Romance", "SciFi", "Thriller", "War", "Western")

  val numGenres = genreList.size

  // if needed
  // val gmap = genreList.zipWithIndex.toMap
}

class Genre(
    binaryGenreList: Seq[String]
  ) extends Serializable {

  val (genreList: Seq[String], genreInt: Int) = {
    var gi = 0
    var gl = new ListBuffer[String]()
    for(i <- 0 until Genre.genreList.size) {
      val bit = binaryGenreList(i).toInt & 1
      if (bit == 1) {
        gl += Genre.genreList(i)
      }
      gi |= bit << i
    }
    (gl.toSeq, gi)
  }

  def getGenreInt(): Int = {
    genreInt
  }

  def getGenreList(): Seq[String] = {
    genreList
  }

  override def toString = genreInt.toBinaryString
}