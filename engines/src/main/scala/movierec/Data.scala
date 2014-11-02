package io.prediction.engines.movierec

import java.io.Serializable

case class Query (
    val uid: Int, // user ID
    val mid: Int // movie ID
  ) extends Serializable {

  override def toString() = "(" + uid + ", " + mid + ")"
}

case class Prediction (
  // the ranked iid with score
   // val items: Seq[(String, Double)]
    val result: Float
  ) extends Serializable {
  override def toString = s"${result}"
}

case class Actual (
  ) extends Serializable {
}