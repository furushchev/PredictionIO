package io.prediction.engines.movierec


import java.io.Serializable
import java.util.Map
import org.apache.commons.math3.linear.RealVector

case class Model (
        val itemSimilarity: Map[Integer, RealVector]
        val userHistory: Map[Integer, RealVector]
    ) extends Serializable {
    
    override def toString(): String {
        var s: String

        if((itemSimilarity.size() > 20) || (userHistory.size() > 20)) {
            s = "Model: [itemSimilarity.size=" + itemSimilarity.size() + "]\n"
                   +"[userHistory.size=" + userHistory.size() + "]"
        } else {
            s = "Model: [itemSimilarity: " + itemSimilarity.toString() + "]\n"
                  +"[userHistory: " + userHistory.toString() + "]"
        }

        return s
    }
}