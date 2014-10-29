package io.prediction.engines.movierec

import io.prediction.controller._
import scala.io.Source

case class DataSourceParams(
  val ratingsFilePath: String,
  val usersFilePath:   String,
  val moviesFilePath:  String
) extends Params

case class DataSource(
  val params: DataSourceParams
) extends LDataSource[DataSourceParams, EmptyDataParams,
                      TrainingData, Query, EmptyActual] {

  override def readTraining: TrainingData = {
    val delim = "[\t,]"
    val ratings = Source.fromFile(params.ratingsFilePath).getLines()
      .toList.map { line =>
        val data = line.split(delim)
        new Rating(data(0).toInt, data(1).toInt, data(2).toFloat)
      }

    val users = Source.fromFile(params.usersFilePath).getLines()
      .toList.map { line =>
        val data = line.split(delim)
        new User(data(0), data(1), data(2), data(3), data(4))
      }

    val movies = Source.fromFile(params.moviesFilePath).getLines()
      .toList.map { line =>
        val data = line.split(delim)
        new Movie(data(0), data(1), data(2), data(3))
      }

    new TrainingData(ratings, users, movies)
  }
}
