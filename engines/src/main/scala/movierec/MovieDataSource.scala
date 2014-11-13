package io.prediction.engines.movierec

//import io.prediction.controller.EmptyDataParams
//import io.prediction.engines.base
//import io.prediction.engines.base.HasName
//import org.joda.time.DateTime
import io.prediction.controller.EmptyParams
import io.prediction.controller.Params
import io.prediction.controller.LDataSource
import scala.io.Source



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
  //) extends base.AbstractEventsDataSourceParams
  ) extends Params

case class EvalParams(
    // The variable n in Query, i.e. the number of items requested from the
    // ItemRec prediction engine. Default value is -1, it will use the same number
    // as actions of that user.
    val queryN: Int = -1
  )

class MovieDataSource(params: MovieDataSourceParams)
  //extends base.EventsDataSource[DataParams, Query, Actual](params) {
  extends LDataSource[
    MovieDataSourceParams,
    EmptyParams,
    TrainingData,
    Query,
    Actual] {

  // TODO Maybe We need to trim()?
  override def read(): Seq[(EmptyParams, TrainingData, Seq[(Query, Actual)])] = {
    val delim = "[\t|]"
    println("START READING FILES")

    //def getCurrentDirectory = new java.io.File( "." ).getCanonicalPath //Used to print curr directory
    //println(getCurrentDirectory)///home/beth/MovieLens/PredictionIO/engines

    val ratings = Source.fromFile(params.ratingsFilePath).getLines()
        .toList.map { it =>
            val line = it.split(delim)
            println(new Rating(line(0).toInt, line(1).toInt, line(2).toFloat).toString())
            new Rating(line(0).toInt, line(1).toInt, line(2).toFloat)
        }
    println("DONE RATING FILE")
    
    val users = Source.fromFile(params.usersFilePath).getLines()
        .toList.map { it =>
            val line = it.split(delim)
            println(new User(line(0).toInt, line(1).toInt, line(2), line(3), line(4)).toString())
            new User(line(0).toInt, line(1).toInt, line(2), line(3), line(4))
        }
    println("DONE USERS FILE")

    // movie id | movie title | release date | video release date (TODO) | IMDb URL (TODO) | 
    //unknown 5 | Action | Adventure | Animation | Children's | Comedy | Crime | Documentary |
    //Drama | Fantasy |Film-Noir | Horror | Musical | Mystery | Romance | Sci-Fi |
    //Thriller | War | Western |

    val movies = Source.fromFile(params.moviesFilePath, "iso-8859-1").getLines()//To avoid java.nio.charset.MalformedInputException
        .toList.map { it =>
            val line = it.split(delim)// TODO Genre parsing and Data parsing
            var genre: Int = 0
            var i = 0
            
            for(i <- 0 to Genre.values.size-1){
              //println("genre="+Genre.values(i))
              genre = genre | ((line(5+i).toInt & 1) << Genre.values(i))
              //println(genre.toBinaryString)
            }
            i = Genre.values.size + 5 
            //println("end of genre") 
            //5+i directors | writers | actors | runtimes (in minutes) | countries | languages | certificates | plot
            if(i+7 < line.size){

            println(new Movie(line(0).toInt, line(1), line(2), genre, line(i), line(i+1), 
                      line(i+2), line(i+3), line(i+4), line(i+5), line(i+6), line(i+7)).toString())
            new Movie(line(0).toInt, line(1), line(2), genre, line(i), line(i+1), 
                      line(i+2), line(i+3), line(i+4), line(i+5), line(i+6), line(i+7))
            }else{
              // Current data is not done (missing data), so in order to compile and run
              i=2           
              println(new Movie(line(0).toInt, line(1), line(2), genre, line(i), line(i), 
                      line(i), line(i), line(i), line(i), line(i), line(i)).toString())
            new Movie(line(0).toInt, line(1), line(2), genre, line(i), line(i), 
                      line(i), line(i), line(i), line(i), line(i), line(i))

            }
        }
    println("DONE MOVIES FILE. FINISHED ALL")


    val data = new TrainingData(ratings, users, movies);
    return Seq((null.asInstanceOf[EmptyParams], data, Seq[(Query, Actual)]()))
  }

  /*override def generateQueryActualSeq(
    users: Map[Int, base.UserTD],
    items: Map[Int, base.ItemTD],
    actions: Seq[base.U2IActionTD],
    trainUntil: DateTime,
    evalStart: DateTime,
    evalUntil: DateTime): (DataParams, Seq[(Query, Actual)]) = {

    require(
      !params.evalParams.isEmpty,
      "EventsDataSourceParams.evalParams must not be empty")

    val evalParams = params.evalParams.get

    val ui2uid: Map[Int, String] = users.mapValues(_.uid)
    val ii2iid: Map[Int, String] = items.mapValues(_.iid)

    val userActions: Map[Int, Seq[base.U2IActionTD]] =
      actions.groupBy(_.uindex)

    val allIids: Vector[String]  = actions.map(_.iindex)
      .map(ii => ii2iid(ii))
      .distinct
      .sortBy(identity)
      .toVector

    val qaSeq: Seq[(Query, Actual)] = userActions.map { case (ui, actions) => {
      val uid = ui2uid(ui)
      val iids = actions.map(u2i => ii2iid(u2i.iindex))
      val actionTuples = iids.zip(actions).map(e => (uid, e._1, e._2))
      val n = (if (evalParams.queryN == -1) iids.size else evalParams.queryN)
      val query = Query(uid = uid, n = n)
      val actual = Actual(actionTuples = actionTuples, servedIids = allIids)
      (query, actual)
    }}
    .toSeq

    (new DataParams(trainUntil, evalStart, evalUntil), qaSeq)
  }*/
}
