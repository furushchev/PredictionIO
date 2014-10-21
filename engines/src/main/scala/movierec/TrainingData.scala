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

  case class Movie(
      val mid: Int
      val title: Int
      val releaseDate: Date
      //val videoReleaseDate: Date
      val imdbUrl: String
      // TODO: decide how to implement genre flags
    ) extends Serializable {

    override def toString() = "Movie: " + title + " ID: " + mid
                              + " ReleaseDate: " + releaseDate
                              + " ImdbUrl: " + imdbUrl
  }

  case class User(
      val uid: Int
      val age: Int 
      val gender: Char
      val occupation: String
      val zip: Int
    ) extends Serializable {

    override def toString(): String = "UserID: " + uid + " Age: " + age
                                      + " Gender: " + gender + " Occupation: "
                                      + occupation + " zip: " + zip
  }
}

