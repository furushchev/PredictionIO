<<<<<<< HEAD
class TrainingData(
  val ratings: List[Rating]
  val users: List[User]
  val movies: List[Movie]
) extends Serializable

class Rating(
  val userId: Int
  val movieId: Int
  float rating: Float
) extends Serializable {
  override def toString = "(" + userId + "," + movieId + ")"
}

class Movie(
  val movieId: Int
  val movieTitle: Int
  val releaseDate: Date
  val videoReleaseDate: Date
  val imdbUrl: String
  // TODO: decide how to implement genre flags
) extends Serializable

class User(
  val userId: Int
  val age: Int
  val gender: Char
  val occupation: String
  val zipCode: Int
) extends Serializable
=======
package io.prediction.engines.movierec

import java.io.Serializable
//import java.util.List

case class TrainingData (
    val ratings: Seq[Rating]
  ) extends Serializable {

  override def toString(): String {
    var s: String

    if (ratings.size() > 20) {
      s = "TrainingData.size=" + ratings.size()
    } else {
      s = ratings.toString()
    }

    return s
  }

  case class Rating(
      val uid: Int
      val mid: Int
      val rating: Float
    ) extends Serializable {

    override def toString() = "(" + uid + ", " + mid + ", " + rating + ")"
  }
}

>>>>>>> 4d9d0f33df142a393cd1588a623b41eb05899f6e
