package io.prediction.engines.movierec

import io.prediction.engines.base

case class Query (
    val uid: Int, // user ID
    val mid: Int  // movie ID
  ) extends Serializable {
  override def toString() = s"[${uid}, ${mid}]"
}

case class Prediction (
    // the ranked iid with score
    val items: Seq[(String, Double)]
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