/*
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
    val dsp = DataSourceParams(
      ratingsFilePath = "",
      usersFilePath = "",
      moviesFilePath = ""
    )

    val mp = new EmptyParams()

    val pp = new EmptyParams()

    val mrp = new MovieRecAlgorithmParams(
      threshold = Double.MinPositiveValue
    )

    val sp = new EmptyParams()

    val engineParams = new EngineParams(
      dataSourceParams = dsp,
      preparatorParams = pp,
      algorithmParamsList = Seq(
        ("MovieRecAlgorithm", mrp)),
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
      engineParams = engineParams
      //metricsClassOpt = Some(classOf[MovieRecMetrics]),
      //metricsParams = mp
    )
  }
}
*/