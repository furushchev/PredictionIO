package io.prediction.engines.movierec

import io.prediction.controller.EmptyParams
import io.prediction.controller.Params
import io.prediction.controller.LDataSource
import scala.io.Source
import scala.collection.mutable.ListBuffer

class MovieDataSourceParams(
    /*val appId: Int,
    // default None to include all itypes
    val itypes: Option[Set[String]] = None, // train items with these itypes
    // actions for training
    val actions: Set[String],
    val startTime: Option[DateTime] = None, // event starttime
    val untilTime: Option[DateTime] = None, // event untiltime
    val attributeNames: base.AttributeNames,
    override val slidingEval: Option[base.EventsSlidingEvalParams] = None,
    val evalParams: Option[EvalParams] = None,*/
    val ratingsFilePath: String,
    val usersFilePath: String,
    val moviesFilePath: String
  ) extends Params

case class EvalParams(
    // The variable n in Query, i.e. the number of items requested from the
    // ItemRec prediction engine. Default value is -1, it will use the same number
    // as actions of that user.
    val queryN: Int = -1
  )

class MovieDataSource(params: MovieDataSourceParams)
  extends LDataSource[
    MovieDataSourceParams,
    EmptyParams,
    TrainingData,
    Query,
    Actual] {

  private var logcount: Int  = 0
  private val DEBUG: Boolean = false
  private val LOG_MAX: Int = 6
  def log(str: String) {
    if (DEBUG & logcount < LOG_MAX) {
      println(str)
      logcount += 1
    }
  }

  // TODO: Maybe We need to trim()?
  override def read(): Seq[(EmptyParams, TrainingData, Seq[(Query, Actual)])] = {
    val delim = "[\t|]"
    val subDelim = ","
    log("START READING FILES")

    logcount = 0
    val ratings = Source.fromFile(params.ratingsFilePath).getLines()
        .toList.map { it =>
            val line = it.split(delim)
            val r = new Rating(line(0).toInt, line(1).toInt, line(2).toFloat)
            log(r.toString)
            r
        }
    logcount = 0
    log("DONE RATING FILE")

    val users = Source.fromFile(params.usersFilePath).getLines()
        .toList.map { it =>
            val line = it.split(delim)
            val u = new User(line(0), line(1).toInt, line(2), line(3), line(4))
            log(u.toString)
            (line(0).toInt, u)
        }.toMap
    logcount = 0
    log("DONE USERS FILE")


    // MOVIE DATA SOURCE FORMAT:
    // movie id | movie title | release date | video release date (TODO)
    // | IMDb URL (TODO) | genre's binary list | directors | writers | actors
    // | runtimes (minutes) | countries | languages | certificates | plot

    // To avoid java.nio.charset.MalformedInputException
    val movies = Source.fromFile(params.moviesFilePath, "iso-8859-1").getLines()
        .toList.map { it =>
            val line = it.split(delim)

            // starting position of other attributes after genre's binary list
            var pos = 5 + Genre.numGenres

            val genre = Genre(line.slice(5, pos))

            var movie: Movie = null
            try {
              movie = new Movie(
                    line(0), line(1), line(2).split("-")(2), genre,
                    line(pos).split(subDelim), line(pos + 1).split(subDelim),
                    line(pos + 2).split(subDelim), line(pos + 3),
                    line(pos + 4).split(subDelim), line(pos + 5).split(subDelim),
                    line(pos + 6).split(subDelim), line(pos + 7), 
                    line(pos + 8).split(subDelim))
            }
            catch {
              // some movies might have missing fields
              case e: Exception =>
                println("DATA PARSING ERROR or Exception Caught: " + e)

                movie = new Movie(line(0), line(1), line(2), genre,
                              Seq(), Seq(), Seq(), null, Seq(), Seq(), Seq(), null, Seq())
            }

            log(movie.toString)

            (line(0).toInt, movie)
        }.toMap

    logcount = 0
    log("DONE MOVIES FILE. FINISHED ALL")

    Seq((null.asInstanceOf[EmptyParams],
         new TrainingData(ratings, users, movies),
         Seq[(Query, Actual)]()))
  }
}
