package io.prediction.engines.movierec

import java.io.Serializable

case class Query (
    val uid: Int // user ID
    val mid: Int // movie ID
  ) extends Serializable {

  override def toString() = "(" + uid + ", " + mid + ")"
}
