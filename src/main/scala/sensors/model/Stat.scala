package sensors.model

import cats._
import cats.implicits._

case class Stat(stat: List[OutputStat], files: Files,
                processed: Processed, failed: Failed)

object Stat {
  val avgOrd = Ordering[Option[Total]].reverse

  implicit val show: Show[Stat] = Show.show(st =>
    s"""
       |Num of processed files: ${st.files}
       |Num of processed measurements: ${st.processed}
       |Num of failed measurements: ${st.failed}
       |""".stripMargin +
      showData(st.stat)
  )

  def showData(stat: List[OutputStat]): String = stat match {
    case List() => ""
    case _ =>
      s"""
         |Sensors with highest avg humidity:
         |
         |sensor-id,min,avg,max
         |""".stripMargin +
        stat.map(_.show).mkString("\n")
  }

  def apply(st: Stat2): Stat = Stat(outputStat(st.stat),
    st.files, st.stat.processed, st.stat.failed)

  def outputStat(stat: Stat1): List[OutputStat] = stat
    .stat.toList.sortBy(getAvg)(avgOrd)

  def getAvg(stat: OutputStat): Option[Total] =
    stat._2.map(_._2.avg)

}
