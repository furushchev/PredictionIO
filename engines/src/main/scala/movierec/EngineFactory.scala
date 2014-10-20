import io.prediction.controller.IEngineFactory
import io.prediction.controller.Engine

object EngineFactory extends IEngineFactory {
  def apply() = {
    new Engine(
      classOf[MovieDataSource],
      classOf[MovieRecPreparator],
      Map(
        "ncMahoutItemBased" -> classOf[NCItemBasedAlgorithm]),
      classOf[MovieRecServing]
    )
  }
}