package io.prediction.engines.movierec

import io.prediction.engines.base

case class Query (
    val uid: String,
    val mids: Seq[String],
    val top: Seq[Int], // Int cannot be ommited in query
    val mtypes: Seq[String],
    val display: Seq[String] // Intersect or Union
  ) extends Serializable {
  override def toString() = s"[${uid}, ${mids}, ${top}, ${mtypes}, ${display}]"
}

case class Prediction (
    // the ranked mid with score
    val movies: Seq[(String, Double)],
    val isOriginal: Boolean
  ) extends Serializable {
  override def toString = s"${movies}"
}

case class Actual(
    // (uid, mid, action) - tuple
    //val actionTuples: Seq[(String, String, base.U2IActionTD)],
    //val servedIids: Vector[String]
    val ratings: Seq[Rating]
  ) extends Serializable {
  override def toString = s"${ratings}"
}