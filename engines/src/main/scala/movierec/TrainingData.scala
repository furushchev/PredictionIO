package io.prediction.engines.movierec

import scala.collection.mutable.ListBuffer

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

// TODO: Determine if we need to prepare training data
// currently it's the same as training data
class PreparedData (
    val ratings: Seq[Rating],
    val users:   Map[Int, User],
    var items:   Map[Int, Movie]
  ) extends Serializable {

  override def toString(): String = {
    if (ratings.length > 20) {
      s"PreparedData.size=${ratings.length}"
    } else {
      ratings.toString
    }
  }
}

class Rating(
    val uid: Int,
    val mid: Int,
    val rating: Float
  ) extends Serializable {
  override def toString = s"User: ${uid} rates Movie: ${mid} (${rating} / 5)"
}

class User(
    val uid: String,
    val age: Int,
    val zip: String,
    val gender: String,
    val occupation: String
  ) extends Serializable {
  override def toString = s">>UserID: ${uid}, Age: ${age}, Gender: ${gender}" +
                          s", Occupation: ${occupation}, Zip: ${zip}"
}

// TODO: Put most of the features into a Map
class Movie(
    val mid: String,
    val title: String,
    val genre: Int,
    val genreList: Seq[String],
    val year: String, // TODO: Date type
    val writers: String,
    val actors: String,
    val directors: String, // TODO: separate directors, writers and actors into list/array...
    val runtimes: String,  // TODO: in minutes string for now due to Canada:108
    val countries: String,
    val languages: String,
    val certificates: String,
    val plot: String
  ) extends Serializable {
  override def toString = s">>Movie: ${title}, ID: ${mid}" +
                          s", ReleaseDate: ${releaseDate}" +
                          s", Genre: ${genre.toBinaryString}" +
                          s", Itypes: ${genreList}" +
                          s"\n\n Directors: ${directors}" +
                          s", Writers: ${writers}" +
                          s", Actors: ${actors}" +
                          s"\n\n Runtimes: ${runtimes}" +
                          s", Countries: ${countries}" +
                          s", Languages: ${languages}" +
                          s", Certificates: ${certificates}" +
                          s"\n\n Plot: ${plot}\n"
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

class Genre(binaryGenreList: Seq[String]) {

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

  def toBinaryString(): String = {
    genreInt.toBinaryString
  }

  def getGenreInt(): Int = {
    genreInt
  }

  def getGenreSeq(): Seq[String] = {
    genreList
  }
}