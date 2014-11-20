package io.prediction.engines.movierec

import io.prediction.controller.IEngineFactory
import io.prediction.controller.Engine

object MovieRecEngine extends IEngineFactory {
  def apply() = {
    new Engine(
      //classOf[EventsDataSource],
      classOf[MovieDataSource],
      classOf[MovieRecPreparator],
      Map(
        "rand" -> classOf[RandomAlgorithm],
        "movieRecAlgorithm" -> classOf[MovieRecAlgorithm],
        "featurebased" -> classOf[FeatureBasedAlgorithm]
        ),
      classOf[MovieRecServing]
    )
  }
}
