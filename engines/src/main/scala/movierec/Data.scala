package io.prediction.engines.movierec

import io.prediction.engines.base

case class Query (
    val uid: String,
    val mids: Seq[String],
    val top: Seq[Int] // Int cannot be ommited in query
  ) extends Serializable {
  override def toString() = s"[${uid}, ${mids}, ${top}]"
}

case class Prediction (
    // the ranked iid with score
    val movies: Seq[(String, Double)],
    val isOriginal: Boolean = false
  ) extends Serializable {
  override def toString = s"${movies}"
}

case class Actual(
    // (uid, iid, action) - tuple
    val actionTuples: Seq[(String, String, base.U2IActionTD)],
    val servedIids: Vector[String]
  ) extends Serializable {
  override def toString = s"Actual: [$actionTuples.size]"
}