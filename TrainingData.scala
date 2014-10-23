package io.prediction.engines.movierec

import java.io.Serializable
//import java.util.List

class TrainingData (
    val ratings: Seq[Rating],
    val users: Seq[User],
    var movies: Seq[Movie]
  ) extends Serializable {

  override def toString(): String = {
    if (ratings.length > 20) {
      "TrainingData.size=" + ratings.length
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

override def toString() = "(" + uid + ", " + mid + ", " + rating + ")"
}

class Movie(
  val mid: Int,
  val title: Int,
  val releaseDate: Date,
  //val videoReleaseDate: Date
  val imdbUrl: String
  // TODO: decide how to implement genre flags
) extends Serializable {

override def toString() = "Movie: " + title + " ID: " + mid
                          + " ReleaseDate: " + releaseDate
                          + " ImdbUrl: " + imdbUrl
}

class User(
  val uid: Int,
  val age: Int,
  val gender: Char,
  val occupation: String,
  val zip: Int
) extends Serializable {

override def toString() = "UserID: " + uid + " Age: " + age
                                  + " Gender: " + gender + " Occupation: "
                                  + occupation + " zip: " + zip
}
