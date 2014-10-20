import io.prediction.controller.Params

import scala.io.Source

case class DataSourceParams(
  val filePath: String
) extends Params

case class DataSource(
  val params: DataSourceParams
) extends LDataSource[DataSourceParams, EmptyDataParams,
                      TrainingData, Query, EmptyActual] {
  
  override def readTraining: TrainingData = {
    val ratings = Source.fromFile(params.filePath).getLines()
      .toList.map { line => 
        val data = line.split("[\t,]")
        new Rating(data(0), data(1), data(2))
      }

    val users = Source.fromFile(params.filePath).getLines()
      .toList.map { line =>
        val data = line.split("[\t,]")
        new User(data(0), data(1), data(2), data(3), data(4))
      }

    val movies = Source.fromFile(params.filePath).getLines()
      .toList.map { line =>
        val data = line.split("[\t,]")
        new Movie(data(0), data(1), data(2), data(3), data(4))
      }

    new TrainingData(ratings, users, movies)
  }
}
  
