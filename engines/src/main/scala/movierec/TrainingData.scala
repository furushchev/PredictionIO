package io.prediction.engines.movierec

import java.io.Serializable
//import java.util.List

case class TrainingData (
        val ratings: Seq[Rating]
    ) extends Serializable {
    override def toString(): String {
        var s: String

        if (ratings.size() > 20) {
          s = "TrainingData.size=" + ratings.size()
        } else {
          s = ratings.toString()
        }

        return s;
    }

     case class Rating(
            val uid: Int
            val iid: Int
            val rating: Float
        ) extends Serializable {
         
         override def toString() = "(" + uid + ", " + iid + ", " + rating + ")"
     }

}

