package io.prediction.engines.movierec

class TrainingData (
    val ratings: Seq[Rating],
    val users:   Seq[User],
    var movies:  Seq[Movie]
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
    val ratings: Seq[Rating],
    val users:   Seq[User],
    var movies:  Seq[Movie]
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
    val uid:    Int,
    val mid:    Int,
    val rating: Int
  ) extends Serializable {
  override def toString() = "(" + uid + ", " + mid + ", " + rating + ")"
}

class Movie(
    val mid: Int,
    val title: String,
    val releaseDate: Int,
    //val videoReleaseDate: Date
    val imdbUrl: String,
    val genre: Int
  ) extends Serializable {
  override def toString() = "Movie: " + title + " ID: " + mid +
                            " ReleaseDate: " + releaseDate +
                            " ImdbUrl: " + imdbUrl
}

class User(
    val uid: Int,
    val age: Int,
    val gender: String,
    val occupation: String,
    val zip: Int
  ) extends Serializable {
  override def toString() = "UserID: " + uid + " Age: " + age +
                            " Gender: " + gender + " Occupation: " +
                            occupation + " zip: " + zip
}