package io.prediction.engines.movierec

import io.prediction.controller.EmptyDataParams
import io.prediction.engines.base
import io.prediction.engines.base.HasName
import org.joda.time.DateTime
import io.prediction.controller.Params
import io.prediction.engines.base.DataParams

import java.util.List
import java.util.Scanner
import scala.io._


case class MovieDataSourceParams(
    val appId: Int,
    // default None to include all itypes
    val itypes: Option[Set[String]] = None, // train items with these itypes
    // actions for training
    val actions: Set[String],
    val startTime: Option[DateTime] = None, // event starttime
    val untilTime: Option[DateTime] = None, // event untiltime
    val attributeNames: base.AttributeNames,
    override val slidingEval: Option[base.EventsSlidingEvalParams] = None,
    val evalParams: Option[EvalParams] = None,
    val ratingsFilePath: String,
    val userFilePath: String,
    val movieFilePath: String
  ) extends base.AbstractEventsDataSourceParams

case class EvalParams(
    // The variable n in Query, i.e. the number of items requested from the
    // ItemRec prediction engine. Default value is -1, it will use the same number
    // as actions of that user.
    val queryN: Int = -1
  )

class MovieDataSource(dsp: MovieDataSourceParams)
  extends base.EventsDataSource[DataParams, Query, Actual](dsp) {

  def readTrainingData(): TrainingData = {
    val delim = "[\t,]"
    val ratings = Source.fromFile(dsp.ratingsFilePath).getLines()
        .toList.map { it =>
            val line = it.split(delim)
            new Rating(line(0).toInt, line(1).toInt, line(2).toFloat)
        }

    val users = Source.fromFile(dsp.userFilePath).getLines()
        .toList.map { it =>
            val line = it.split(delim)
            new User(line(0).toInt, line(1).toInt, line(2), line(3), line(4).toInt)
        }
    val movies = Source.fromFile(dsp.movieFilePath).getLines()
        .toList.map { it =>
            val line = it.split(delim)// TODO Genre parsing
            new Movie(line(0).toInt, line(1), line(2).toInt, line(3), line(4).toInt)
        }
 

    val data = new TrainingData(ratings, users, movies);
    return data;
  }

  /*override def generateQueryActualSeq(
    users: Map[Int, base.UserTD],
    items: Map[Int, base.ItemTD],
    actions: Seq[base.U2IActionTD],
    trainUntil: DateTime,
    evalStart: DateTime,
    evalUntil: DateTime): (DataParams, Seq[(Query, Actual)]) = {

    require(
      !dsp.evalParams.isEmpty,
      "EventsDataSourceParams.evalParams must not be empty")

    val evalParams = dsp.evalParams.get

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
