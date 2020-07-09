package sensors

import cats._
import cats.implicits._

import scala.language.implicitConversions

package object model {

  type SensorId = String
  type Humidity = Byte
  type Total = BigInt
  type Processed = Total
  type Failed = Total
  type Files = Total

  type ResultData = (Min, Avg, Max)
  type ResultStat = Map[SensorId, Option[ResultData]]
  type OutputStat = (SensorId, Option[ResultData])

  implicit val showOutputStat: Show[OutputStat] = Show.show {
    case (id, Some((min, avg, max))) => s"$id,${min.show},${avg.show},${max.show}"
    case (id, None) => s"$id,NaN,NaN,NaN"
  }

  implicit def humidityTotal(h: Humidity): Total = BigInt(h)

  case class Max(max: Total) extends AnyVal

  object Max {
    implicit val show: Show[Max] = Show.show(_.max.toString())
    implicit val semigroup: Semigroup[Max] = Semigroup.instance(
      (a, b) => Max(a.max max b.max))
  }

  case class Min(min: Total) extends AnyVal

  object Min {
    implicit val show: Show[Min] = Show.show(_.min.toString())
    implicit val semigroup: Semigroup[Min] = Semigroup.instance(
      (a, b) => Min(a.min min b.min))
  }

  case class Avg(sum: Total, count: Total = 1) {
    lazy val avg: Total = sum / count
  }

  object Avg {
    implicit val show: Show[Avg] = Show.show(_.avg.toString())
    implicit val semigroup: Semigroup[Avg] = Semigroup.instance(
      (a, b) => Avg(a.sum |+| b.sum, a.count |+| b.count))
  }

}
