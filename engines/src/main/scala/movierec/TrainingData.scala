package io.prediction.engines.movierec

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap

class TrainingData (
    val ratings: Seq[Rating],
    val users:   HashMap[Int, User],
    var movies:  HashMap[Int, Movie]
  ) extends Serializable {

  override def toString = s"TrainingData:" +
      s"U: [${users.size}] (${users.take(943)}...) " +
      s"M: [${movies.size}] (${movies.take(1628)}...) " +
      s"R: [${ratings.size}] (${ratings.take(100000)}...)"
}

// TODO: Determine if we need to prepare training data
// currently it's the same as training data
class PreparedData (
    val ratings: Seq[Rating],
    val users:   HashMap[Int, User],
    var movies:  HashMap[Int, Movie]
  ) extends Serializable {

  override def toString = s"PreparedData:" +
      s"U: [${users.size}] (${users.take(943)}...) " +
      s"M: [${movies.size}] (${movies.take(1628)}...) " +
      s"R: [${ratings.size}] (${ratings.take(100000)}...)"
}

class Rating(
    val uid:    Int,
    val mid:    Int,
    val rating: Float
  ) extends Serializable {
  override def toString() = "User: " + uid + " rates Movie: " + mid + " (" + rating + " / 5)\n"
}


class User(//UserTD
    val uid: Int,
    val age: Int,
    val gender: String,
    val occupation: String,
    val zip: String //Some zipcode is T8H1N
  ) extends Serializable {
  override def toString() = "UserID: " + uid + ", Age: " + age +
                            ", Gender: " + gender + ", Occupation: " +
                            occupation + ", Zip: " + zip + "\n"
}

// movie id | movie title | release date | video release date (TODO) | IMDb URL (TODO) |
//unknown | Action | Adventure | Animation | Children's | Comedy | Crime | Documentary |
//Drama | Fantasy |Film-Noir | Horror | Musical | Mystery | Romance | Sci-Fi |
//Thriller | War | Western |

 //directors | writers | actors | runtimes (in minutes) | countries | languages | certificates | plot

class Movie(//ItemTD
    val mid: Int,
    val title: String,
    val releaseDate: String, //TODO Date type
    val genre: Int,
    val itypes: Seq[String],
    val directors: String,// TODO separate directors, writers and actors into list/array...
    val writers: String,
    val actors: String,
    val runtimes: String, // in minutes TODO string for now due to Canada:108
    val countries: String,
    val languages: String,
    val certificates: String,
    val plot: String
  ) extends Serializable {
  override def toString() = "\nMovie: " + title + ", ID: " + mid +
                            ", ReleaseDate: " + releaseDate +
                            ", Genre: " + genre.toBinaryString +
                            ", Itypes: " + itypes +
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

  val itypes = Array("Unknown", "Action", "Adventure", "Animation",
                "Childrens", "Comedy", "Crime", "Documentary", "Drama",
                "Fantasy", "FilmNoir", "Horror", "Musical", "Mystery",
                "Romance", "SciFi", "Thriller", "War", "Western")

  val numGenres = itypes.size

  // if needed
  // val gmap = itypes.zipWithIndex.toMap
}

class Genre(binaryGenreList: Array[String]) {

  val (genreList: List[String], genreInt: Int) = {
    var gi = 0
    var gl = new ListBuffer[String]()
    var i = 0
    for(i <- 0 to Genre.itypes.size-1) {
      val bit = binaryGenreList(i).toInt & 1
      if (bit == 1) {
        gl += Genre.itypes(i)
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