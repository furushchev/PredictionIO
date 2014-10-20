package io.prediction.engines.movierec

import java.io.Serializable

case class Query (
        val uid: Int // user ID
        val iid: Int // item ID
    ) extends Serializable {

    override def toString() = "(" + uid + ", " + iid + ")"
}
