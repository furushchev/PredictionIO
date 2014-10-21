package io.prediction.engines.movierec


import io.prediction.engines.base

case class PreparatorParams (
    /*
  // how to map selected actions into rating value
  // use None if use U2IActionTD.v field
  val actions: Map[String, Option[Int]], // ((view, 1), (rate, None))
  val seenActions: Set[String],
  val conflict: String // conflict resolution, "latest" "highest" "lowest"*/
) extends base.AbstractPreparatorParams


class MovieRecPreparator(pp: PreparatorParams)
  extends base.Preparator(pp)