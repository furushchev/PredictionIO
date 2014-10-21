package io.prediction.engines.movierec

import io.prediction.controller.EmptyDataParams
import io.prediction.engines.base
import io.prediction.engines.base.HasName
import org.joda.time.DateTime
import io.prediction.controller.Params
import io.prediction.engines.base.DataParams
import java.util.List

case class EventsDataSourceParams(
  /*val appId: Int,
  // default None to include all itypes
  val itypes: Option[Set[String]] = None, // train items with these itypes
  // actions for training
  val actions: Set[String],
  val startTime: Option[DateTime] = None, // event starttime
  val untilTime: Option[DateTime] = None, // event untiltime
  val attributeNames: base.AttributeNames,
  override val slidingEval: Option[base.EventsSlidingEvalParams] = None,
  val evalParams: Option[EvalParams] = None*/
  val filePath: String
) extends base.AbstractEventsDataSourceParams

case class EvalParams(
  // The variable n in Query, i.e. the number of items requested from the
  // ItemRec prediction engine. Default value is -1, it will use the same number
  // as actions of that user.
  val queryN: Int = -1
)

class EventsDataSource(dsp: EventsDataSourceParams)
  extends base.EventsDataSource[DataParams, Query, Actual](dsp) {

  override def read() {
      File ratingFile = new File(dsp.filePath)
      Scanner = null

      try {
          sc = new Scanner(ratingFile)
      } catch (FileNotFoundException e){
          println("Caught FileNotFoundException " + e.getMessage())
          System.exit(1)
      }

      List[TrainingData#Rating] ratings = new ArrayList[TrainingData#Rating]()

      while (sc.hasNext()) {
        var line = sc.nextLine()
        var tokens: String[] = line.split("[\t,]")

        try {
          TrainingData#Rating rating = new TrainingData#Rating(
              Integer.parseInt(tokens[0]),
              Integer.parseInt(tokens[1]),
              Float.parseFloat(tokens[2]))
          ratings.add(rating)
        } catch (Exception e) {
          println("Can't parse rating file. Caught Exception: " + e.getMessage())
          System.exit(1)
        }
      }

//??????????????????
      /*List<Tuple3<Object, TrainingData, Iterable<Tuple2<Query, Object>>>> data =
        new ArrayList<Tuple3<Object, TrainingData, Iterable<Tuple2<Query, Object>>>>();

      data.add(new Tuple3<Object, TrainingData, Iterable<Tuple2<Query, Object>>>(
        null,
        new TrainingData(ratings),
        new ArrayList<Tuple2<Query, Object>>()
      ));*/

      val data = TrainingData(ratings);
 
      return data;
  }

 /* override def generateQueryActualSeq(
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