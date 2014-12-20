
package io.prediction.engines.movierec

import io.prediction.controller.EmptyParams
import io.prediction.controller.EngineParams
import io.prediction.controller.Workflow
import io.prediction.controller.WorkflowParams

import io.prediction.engines.base.AttributeNames
import io.prediction.engines.base.EventsSlidingEvalParams
import io.prediction.engines.base.BinaryRatingParams

import com.github.nscala_time.time.Imports._

object Runner {

  def main(args: Array[String]) {

    // Define params
    // ---------------------------------------------------------------
    val dsp = new MovieDataSourceParams(
      ratingsFilePath = "./src/main/scala/movierec/data/u.data",
      usersFilePath = "./src/main/scala/movierec/data/u.user",
      moviesFilePath = "./src/main/scala/movierec/data/u.item.tags"
    )

    val pp = new EmptyParams()

    val mp = new MovieRecMetricsParams(
      name = "",
      buckets = 10,
      ratingParams = new BinaryRatingParams(
        actionsMap = Map(),
        goodThreshold = 3),
      measureType = MeasureType.PrecisionAtK,
      measureK = 10
    ) 

    /*val mrp = new MovieRecAlgorithmParams(
      threshold = Double.MinPositiveValue
    )*/

    val fbp = new EmptyParams()


    val sp = new EmptyParams()

    val engineParams = new EngineParams(
      dataSourceParams = dsp,
      preparatorParams = pp,
      algorithmParamsList = Seq(
        ("featurebased", fbp)),
      servingParams = sp
    )

    // Run engine
    // ---------------------------------------------------------------
    val engine = MovieRecEngine()

    Workflow.runEngine(
      params = WorkflowParams(
        batch = "Imagine: Local MovieRec Engine",
        verbose = 0),
      engine = engine,
      engineParams = engineParams,
      metricsClassOpt = Some(classOf[MovieRecMetrics]),
      metricsParams = mp
    )
  }
}
