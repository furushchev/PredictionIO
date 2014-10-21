package io.prediction.engines.movierec


import io.prediction.controller.LServing
import io.prediction.controller.EmptyParams

// Only return first prediction
class MovieRecServing extends LServing[EmptyParams, Query, Prediction] {
  override def serve(query: Query, predictions: Seq[Prediction]): Prediction = {
    predictions.head
  }
}