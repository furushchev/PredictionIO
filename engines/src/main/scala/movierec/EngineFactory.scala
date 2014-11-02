package io.prediction.engines.movierec

import io.prediction.controller.IEngineFactory
import io.prediction.controller.Engine

object MovieRecEngine extends IEngineFactory {
  def apply() = {
    new Engine(
      classOf[DataSource],
      classOf[MovieRecPreparator],
      Map(
        "MovieRecAlgorithm" -> classOf[MovieRecAlgorithm]
        ),
      classOf[MovieRecServing]
    )
  }
}