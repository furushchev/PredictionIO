package io.prediction.engines.movierec

import java.util.Map
import org.apache.commons.math3.linear.RealVector

import io.prediction.controller.Params
import io.prediction.controller.LAlgorithm

class MovieRecModel (
    val itemSimilarity: Map[Integer, RealVector],
    val userHistory: Map[Integer, RealVector]
  ) extends Serializable {

  override def toString(): String = {
    if ((itemSimilarity.size() > 20) || (userHistory.size() > 20)) {
      "MovieRecModel: [itemSimilarity.size=" + itemSimilarity.size() + "]\n" +
      "[userHistory.size=" + userHistory.size() + "]"
    } else {
      "MovieRecModel: [itemSimilarity: " + itemSimilarity.toString() + "]\n" +
      "[userHistory: " + userHistory.toString() + "]"
    }
  }
}

class MovieRecAlgorithmParams (
    val threshold: Double
  ) extends Params

class MovieRecAlgorithm (params: MovieRecAlgorithmParams)
  extends LAlgorithm[
      MovieRecAlgorithmParams, PreparedData, MovieRecModel, Query, Prediction] {

  override
  def train(pd: PreparedData): MovieRecModel = {
    return null//using PreparedData to construct model
  }

  override
  def predict(model: MovieRecModel, query: Query): Prediction = {
    return new Prediction(
      movies = Seq(("Testing", 1))
      )
  }
}