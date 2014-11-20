package io.prediction.engines.movierec

import io.prediction.controller.Params
import io.prediction.controller.LAlgorithm

import scala.util.Random

class RandomAlgoParams(
    val seed: Int
  ) extends Params

class RandomModel() extends Serializable

class RandomAlgorithm(params: RandomAlgoParams)
  extends LAlgorithm[RandomAlgoParams, PreparedData, RandomModel,
  Query, Prediction] {

  override
  def train(preparedData: PreparedData): RandomModel = {
    new RandomModel()
  }

  override
  def predict(model: RandomModel, query: Query): Prediction = {
    val rand = new Random(params.seed)

    new Prediction (
      movies = Seq(("Random", rand.nextInt))
    )
  }
}