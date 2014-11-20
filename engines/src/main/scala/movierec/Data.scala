package io.prediction.engines.movierec

import io.prediction.engines.base

case class Query (
    val uid: String,
    val iids: Seq[String]
  ) extends Serializable {
  override def toString() = s"[${uid}, ${iids}]"
}

case class Prediction (
    // the ranked iid with score
    val items: Seq[(String, Double)],
    val isOriginal: Boolean = false
  ) extends Serializable {
  override def toString = s"${items}"
}

case class Actual(
    // (uid, iid, action) - tuple
    val actionTuples: Seq[(String, String, base.U2IActionTD)],
    val servedIids: Vector[String]
  ) extends Serializable {
  override def toString = s"Actual: [$actionTuples.size]"
}