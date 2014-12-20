package io.prediction.engines.movierec

class MovieRecMetrics (

  ) extends Params {}

object MeasureType extends Enumeration {
  type = Value
  val PrecisionAtK = Value
}

class MetricsUnit (
  val q: Query,
  val p: Prediction,
  val a: Actual,
  val score: Double,
  val baseline: Double, 
  val uidHash: Int = 0
  ) extends Serializable

abstract class MovieRecMeasure extends Serializable {
  def calculate(query: Query, prediction: Prediction, actual: Actual): Double
}

class MovieRecPrecision(val k: Int, val ratingParams: BinaryRatingParams) 
  extends MovieRecMeasure {
  override def toString(): String = s"Precision@$k"
  
  def calculate(query: Query, prediction: Prediction, actual: Actual)
  : Double = {
    val kk = (if (k == -1) query.n else k)

    // Returns a set of string
    val actualItems = MetricsHelper.actions2GoodIids(
      actual.actionTuples, ratingParams)

    val relevantCount = prediction.movies.take(kk)
      .map(_._1)
      .filter(mid => actualItems(mid))
      .size

    val denominator = Seq(kk, prediction.movies.size, actualItems.size).min

    relevantCount.toDouble / denominator
  }
}


class MovieRecMetrics(params: MovieRecMetricsParams) 
  extends Metrics[MovieRecMetricsParams, HasName,
      Query, Prediction, Actual,
      MetricsUnit, Seq[MetricsUnit], MetricsOutput] {
  
  val measure: MovieRecMeasure = params.measureType match {
    case MeasureType.PrecisionAtK => {
      new MovieRecPrecision(params.measureK, params.ratingParams)
    }
    case _ => {
      throw new NotImplementedError(
        s"MeasureType ${params.measureType} not implemented")
    }
  }

  
  override def computeUnit(query: Query, prediction: Prediction, actual: Actual)
  : MetricsUnit = {
    val score = measure.calculate(query, prediction, actual)

    val uidHash: Int = MurmurHash3.stringHash(query.uid)

    // Create a baseline by randomly picking query.n items from served items.
    
    val rand = new Random(seed = uidHash)
    //val iidSet = HashSet[String]()

    val servedIids = actual.servedIids

    //val n = math.min(query.n, servedIids.size)

    val randMids = rand.shuffle(
      (if (query.n > servedIids.size) {
          // If too few items where served, use only the served items
          servedIids.toSet
        } else {
          val tempSet = MHashSet[String]()
          while (tempSet.size < query.n) {
            tempSet.add(servedIids(rand.nextInt(query.n)))
          }
          tempSet.toSet
        }
      )
      .toSeq
      .sorted
    )

    println(randMids.mkString("Randmid: [", ", ", "]"))

    // TODO(yipjustin): Implement baseline
    val baseline = measure.calculate(
      query,
      Prediction(randIids.map(mid => (mid, 0.0))),
      actual)

    new MetricsUnit(
      q = query,
      p = prediction,
      a = actual,
      score = score,
      baseline = baseline,
      uidHash = uidHash
    )
  }
  


  override def computeSet(dataParams: HasName,
    metricUnits: Seq[MetricsUnit]): Seq[MetricsUnit] = metricUnits
  
  override def computeMultipleSets(
    rawInput: Seq[(HasName, Seq[MetricsUnit])]): MetricsOutput = {
    // Precision is undefined when the relevant items is a empty set. need to
    // filter away cases where there is no good items.
    val input = rawInput.map { case(name, mus) => {
      (name, mus.filter(mu => (!mu.score.isNaN && !mu.baseline.isNaN)))
    }}

    val allUnits: Seq[MetricsUnit] = input.flatMap(_._2)
    
    val overallStats = (
      "Overall",
      MetricsHelper.calculateResample(
        values = allUnits.map(mu => (mu.uidHash, mu.score)),
        buckets = params.buckets),
      MetricsHelper.calculateResample(
        values = allUnits.map(mu => (mu.uidHash, mu.baseline)),
        buckets = params.buckets)
      )

    val runsStats: Seq[(String, Stats, Stats)] = input
    .map { case(dp, mus) =>
      (dp.name,
        MetricsHelper.calculateResample(
          values = mus.map(mu => (mu.uidHash, mu.score)),
          buckets = params.buckets),
        MetricsHelper.calculateResample(
          values = mus.map(mu => (mu.uidHash, mu.baseline)),
          buckets = params.buckets))
    }
    .sortBy(_._1)
    
    val aggregateByActualGoodSize = aggregateMU(
      allUnits,
      mu => MetricsHelper.groupByRange(
        Array(0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144), "%.0f")(
          base.MetricsHelper.actions2GoodIids(mu.a.actionTuples, params.ratingParams).size))

    val outputData = MetricsOutput (
      name = params.name,
      metricsName = "ItemRecMetrics",
      measureType = measure.toString,
      algoMean = overallStats._2.average,
      algoStats = overallStats._2,
      //heatMap = heatMap,
      runs = Seq(overallStats) ++ runsStats,
      aggregations = Seq[(String, Seq[(String, Stats)])](
        ("By # of Positively Rated Items", aggregateByActualGoodSize)
      )
    )

    // FIXME: Use param opt path
    /*
    params.optOutputPath.map { path =>
      MV.save(outputData, path)
    }
    */

    outputData
  }

  def aggregateMU(units: Seq[MetricsUnit], groupByFunc: MetricsUnit => String)
  : Seq[(String, Stats)] = {
    MetricsHelper.aggregate[MetricsUnit](units, _.score, groupByFunc)
  }
}
