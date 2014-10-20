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
