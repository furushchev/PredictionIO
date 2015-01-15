package io.prediction.engines.movierec

import io.prediction.controller.{LAlgorithm, Params, IEngineFactory, Engine}

object MovieRecEngine extends IEngineFactory {
  def apply() = {
    new Engine(
      //classOf[EventsDataSource],
      classOf[MovieDataSource],
      classOf[MovieRecPreparator],
      Map[String, Class[_ <: LAlgorithm[_ <: Params, PreparedData, _, Query, Prediction]]] (
        "rand" -> classOf[RandomAlgorithm],
        "movierec" -> classOf[MovieRecAlgorithm],
        "featurebased" -> classOf[FeatureBasedAlgorithm]
        ),
      classOf[MovieRecServing]
    )
  }
}
