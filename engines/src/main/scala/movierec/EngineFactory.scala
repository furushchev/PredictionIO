package io.prediction.engines.movierec

import io.prediction.controller.IEngineFactory
import io.prediction.controller.Engine

object MovieRecEngine extends IEngineFactory {
  def apply() = {
    new Engine(
      //classOf[EventsDataSource],
      classOf[FileDataSource],
      classOf[MovieRecPreparator],
      Map(
        "rand" -> classOf[RandomAlgorithm],
        "movieRecAlgorithm" -> classOf[MovieRecAlgorithm]
        ),
      classOf[MovieRecServing]
    )
  }
}
