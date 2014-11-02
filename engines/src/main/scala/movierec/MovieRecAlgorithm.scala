package io.prediction.engines.movierec

import java.io.Serializable
import java.util.Map
import org.apache.commons.math3.linear.RealVector

import io.prediction.controller.Params
import io.prediction.controller.LAlgorithm

class Model (
    val itemSimilarity: Map[Integer, RealVector],
    val userHistory: Map[Integer, RealVector]
  ) extends Serializable {

  override def toString(): String = {
    if ((itemSimilarity.size() > 20) || (userHistory.size() > 20)) {
      "Model: [itemSimilarity.size=" + itemSimilarity.size() + "]\n" +
      "[userHistory.size=" + userHistory.size() + "]"
    } else {
      "Model: [itemSimilarity: " + itemSimilarity.toString() + "]\n" +
      "[userHistory: " + userHistory.toString() + "]"
    }
  }
}

class MovieRecAlgorithmParams (
    val threshold: Double
  ) extends Params

class MovieRecAlgorithm (
  ) extends LAlgorithm[MovieRecAlgorithmParams, TrainingData, Model, Query, Prediction] {

  def train(td: TrainingData): Model = {

    return null
  }

  // from tutorial
  def predict(model: Model, query: Query): Prediction = {
/*    var itemVector = model.itemSimilarity.get(query.mid);
    var userVector = model.userHistory.get(query.uid);

    if (itemVector == null) {
      return new Prediction(Float.NaN);
    }
    else if (userVector == null) {
      return new Prediction(Float.Nan);
    }
    else {
      var accum = 0.0
      var accumSim = 0.0
      var i = 0
      for (i <- 0 to itemVector.getDimension()) {
        var weight = itemVector.getEntry(i);
        var rating = userVector.getEntry(i);
        if ((weight != 0) && (rating != 0)) {
          accum += weight * rating;
          accumSim += Math.abs(weight);
        }
      }
    }

    if (accumSim == 0.0) {
        return new Prediction(Float.NaN);
      } else {
        return new Prediction((Float) (accum / accumSim));
      }*/
    return new Prediction(Float.NaN)
  }

}