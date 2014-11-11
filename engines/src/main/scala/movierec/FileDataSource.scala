package io.prediction.engines.movierec

import io.prediction.controller.EmptyParams
import io.prediction.controller.Params
import io.prediction.controller.LDataSource
import scala.io.Source

class FileDataSourceParams(
    val ratingsFilePath: String,
    val usersFilePath:   String,
    val moviesFilePath:  String
  ) extends Params

class FileDataSource(params: FileDataSourceParams)
  extends LDataSource[
    FileDataSourceParams,
    EmptyParams,
    TrainingData,
    Query,
    Actual] {

  override
  def readTraining: TrainingData = {
    val delim = "[\t,]"
/*
    val ratings = Source.fromFile(params.ratingsFilePath).getLines()
      .toList.map { line =>
        val data = line.split(delim)
        new Rating(data(0).toInt, data(1).toInt, data(2).toInt)
      }

    val users = Source.fromFile(params.usersFilePath).getLines()
      .toList.map { line =>
        val data = line.split(delim)
        new User(data(0).toInt, data(1).toInt, data(2), data(3), data(4).toInt)
      }

    val movies = Source.fromFile(params.moviesFilePath).getLines()
      .toList.map { line =>
        val data = line.split(delim)
        new Movie(data(0).toInt, data(1), data(2).toInt, data(3), data(4).toInt)
      }

    new TrainingData(ratings, users, movies)
    */
    return null
  }
}
