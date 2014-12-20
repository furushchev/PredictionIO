package io.prediction.engines.movierec

import io.prediction.controller.EmptyParams
import io.prediction.controller.Params
import io.prediction.controller.LDataSource
import scala.io.Source
import scala.collection.mutable.ListBuffer

class MovieDataSourceParams(
    // val appId: Int,
    // default None to include all itypes
    // val itypes: Option[Set[String]] = None, // train items with these itypes
    // actions for training
    // val actions: Set[String],
    // val startTime: Option[DateTime] = None, // event starttime
    // val untilTime: Option[DateTime] = None, // event untiltime
    // val attributeNames: base.AttributeNames,
    // override val slidingEval: Option[base.EventsSlidingEvalParams] = None,
    // val evalParams: Option[EvalParams] = None,
    val ratingsFilePath: String,
    val usersFilePath:   String,
    val moviesFilePath:  String,
    // e.g. if actualRatio=0.2
    // then we split all the data in to 20% actual and 80% training data
    val actualRatio: Option[Float] = None
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

  private val DEBUG: Boolean = true
  def log(str: String) = if (DEBUG) println(str)

  override
  def read(): Seq[(EmptyParams, TrainingData, Seq[(Query, Actual)])] = {
    val delim = "[\t|]"

    log("START READING FILES")

    val ratings = extractRatingsFromFile(params.ratingsFilePath, delim)

    log("DONE RATING FILE; here are the first 5 entries: ")
    log(ratings.take(4))

    val users = extractUsersFromFile(params.usersFilePath, delim)

    log("DONE USERS FILE; here are the first 5 entries: ")
    log(users.take(4))

    val movies = extractMoviesFromFile(params.moviesFilePath, delim)

    log("DONE MOVIES FILE; here are the first 5 entries: ")
    log(movies.take(4))

/*    if (params.actualRatio.isEmpty) {

    } else {

    }*/
    Seq((null.asInstanceOf[EmptyParams],
         new TrainingData(ratings, users, movies),
         Seq[(Query, Actual)]()))
  }

    /** Return a list of Query-Actual pair for evaluation.
    *
    * It constructs a list of Query-Actual pair using the list of actions.
    * For each user in the list, it creates a Query instance using all items in
    * actions, and creates an Actual instance with all actions associated with
    * the user. Note that it is the metrics job to decide how to interprete the
    * semantics of the actions.
    */
   /*
  def generateQueryActualSeq(
    users:  Map[Int, User],
    movies: Map[Int, Movie],
    ratings: Seq[Rating]): Seq[(Query, Actual)] = {

    val ui2uid: Map[Int, String] = users.mapValues(_.uid)
    val ii2iid: Map[Int, String] = items.mapValues(_.iid)

    val allIids = actions.map(_.iindex)
      .map(ii => ii2iid(ii))
      .distinct
      .sortBy(identity)

    val userActions: Map[Int, Seq[U2IActionTD]] =
      actions.groupBy(_.uindex)

    userActions.map { case (ui, actions) => {
      val uid = ui2uid(ui)
      val iids = actions.map(u2i => ii2iid(u2i.iindex))
      val actionTuples = iids.zip(actions).map(e => (uid, e._1, e._2))

      val query = Query(uid = uid, iids = allIids)
      val actual = Actual(actionTuples = actionTuples)
      (query, actual)
    }}
    .toSeq
  }
  */

  def extractRatingsFromFile (file:String, delim:String)
  : Seq[Rating] = {
    Source.fromFile(params.ratingsFilePath).getLines.toList.map { it =>
        val line = it.split(delim)
        new Rating(line(0).toInt, line(1).toInt, line(2).toFloat)
      }
  }

  def extractUsersFromFile (file:String, delim:String)
  : Map[Int, User] = {
    Source.fromFile(params.usersFilePath).getLines.toList.map { it =>
        val line = it.split(delim)
        val u = new User(line(0), line(1).toInt, line(2), line(3), line(4))
        (line(0).toInt, u)
      }.toMap
  }

  // MOVIE DATA SOURCE FORMAT:
  // movie id | movie title | release date | video release date (TODO)
  // | IMDb URL (TODO) | genre's binary list | directors | writers | actors
  // | runtimes (minutes) | countries | languages | certificates | plot
  def extractMoviesFromFile (file:String, delim:String)
  : Map[Int, Movie] = {
    val listDelim = ","
    // Use iso-8859-1 to avoid java.nio.charset.MalformedInputException
    Source.fromFile(file, "iso-8859-1").getLines.toList.map { it =>
        val line = it.split(delim)

        // starting position of other attributes after genre's binary list
        val pos = 5 + Genre.numGenres

        val genre = Genre(line.slice(5, pos))

        var movie: Movie = null
        try {
          movie = new Movie(
                line(0), line(1), line(2).split("-")(2), genre,
                line(pos).split(listDelim), line(pos + 1).split(listDelim),
                line(pos + 2).split(listDelim), line(pos + 3),
                line(pos + 4).split(listDelim), line(pos + 5).split(listDelim),
                line(pos + 6).split(listDelim), line(pos + 7),
                line(pos + 8).split(listDelim))
        }
        catch {
          // some movies might have missing fields
          case e: Exception =>
            println("DATA PARSING ERROR or Exception Caught: " + e)

            movie = new Movie(line(0), line(1), line(2), genre,
                          Seq(), Seq(), Seq(), null, Seq(), Seq(), Seq(), null, Seq())
        }

        (line(0).toInt, movie)
      }.toMap
  }
}
