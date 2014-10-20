class TrainingData(
  val ratings: List[Rating]
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
