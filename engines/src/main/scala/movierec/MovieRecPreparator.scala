package io.prediction.engines.movierec

import io.prediction.controller.LPreparator
import io.prediction.controller.Params

case class PreparatorParams (
  val placeholder: Int
) extends Params

class MovieRecPreparator(pp: PreparatorParams)
  extends LPreparator[PreparatorParams, TrainingData, PreparedData] {

  override def prepare(trainingData: TrainingData): PreparedData = {

/*    new PreparedData(
    )*/
    return null
  }
}