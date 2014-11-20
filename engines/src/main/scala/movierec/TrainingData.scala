package io.prediction.engines.movierec

import scala.collection.mutable.ListBuffer

class TrainingData (
    val ratings: List[Rating],
    val users:   Map[Int, User],
    var movies:  Map[Int, Movie]
  ) extends Serializable {

  override def toString(): String = {
    if (ratings.length > 20) {
      "TrainingData.size=" + ratings.length
    } else {
      ratings.toString
    }
  }
}

// TODO: Determine if we need to prepare training data
// currently it's the same as training data
class PreparedData (
    val ratings: List[Rating],
    val users:   Map[Int, User],
    var movies:  Map[Int, Movie]
  ) extends Serializable {

  override def toString(): String = {
    if (ratings.length > 20) {
      "PreparedData.size=" + ratings.length
    } else {
      ratings.toString
    }
  }
}

class Rating(
    val uid:    Int,
    val mid:    Int,
    val rating: Float
  ) extends Serializable {
  override def toString() = "User: " + uid + " rates Movie: " + mid + " (" + rating + " / 5)"
}


class User(//UserTD
    val uid: String,
    val age: Int,
    val gender: String,
    val occupation: String,
    val zip: String //Some zipcode is T8H1N
  ) extends Serializable {
  override def toString() = "UserID: " + uid + ", Age: " + age +
                            ", Gender: " + gender + ", Occupation: " +
                            occupation + ", Zip: " + zip
}

// movie id | movie title | release date | video release date (TODO) | IMDb URL (TODO) |
//unknown | Action | Adventure | Animation | Children's | Comedy | Crime | Documentary |
//Drama | Fantasy |Film-Noir | Horror | Musical | Mystery | Romance | Sci-Fi |
//Thriller | War | Western |

 //directors | writers | actors | runtimes (in minutes) | countries | languages | certificates | plot

class Movie(//ItemTD
    val mid: String,
    val title: String,
    val releaseDate: String, //TODO Date type
    val genre: Int,
    val mgenres: Seq[String],
    val directors: String,// @TODO separate directors, writers and actors into list/array...
    val writers: String,
    val actors: String,
    val runtimes: String, // in minutes TODO string for now due to Canada:108
    val countries: String,
    val languages: String,
    val certificates: String,
    val plot: String
  ) extends Serializable {
  override def toString() = ">>Movie: " + title + ", ID: " + mid +
                            ", ReleaseDate: " + releaseDate +
                            ", Genre: " + genre.toBinaryString +
                            ", Itypes: " + mgenres +
                            "\n\n Directors: " + directors +
                            ", Writers: " + writers +
                            ", Actors: " + actors +
                            "\n\n Runtimes: " + runtimes +
                            ", Countries: " + countries +
                            ", Languages: " + languages +
                            ", Certificates: " + certificates +
                            "\n\n Plot: " + plot + "\n"
}

object Genre {

  val mgenres = Array("Unknown", "Action", "Adventure", "Animation",
                "Childrens", "Comedy", "Crime", "Documentary", "Drama",
                "Fantasy", "FilmNoir", "Horror", "Musical", "Mystery",
                "Romance", "SciFi", "Thriller", "War", "Western")

  val numGenres = mgenres.size

  // if needed
  // val gmap = mgenres.zipWithIndex.toMap
}

class Genre(binaryGenreList: Array[String]) {

  val (genreList: List[String], genreInt: Int) = {
    var gi = 0
    var gl = new ListBuffer[String]()
    for(i <- 0 until Genre.mgenres.size) {
      val bit = binaryGenreList(i).toInt & 1
      if (bit == 1) {
        gl += Genre.mgenres(i)
      }
      gi |= bit << i
    }
    (gl.toList, gi)
  }

  def toBinaryString(): String = {
    genreInt.toBinaryString
  }

  def getGenreInt(): Int = {
    genreInt
  }

  def getGenreList(): List[String] = {
    genreList
  }
}