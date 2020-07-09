package sensors.model

import cats._
import cats.implicits._

case class Stat1(stat: ResultStat,
                 processed: Processed = 1,
                 failed: Failed = 0)

object Stat1 {
  implicit val monoid: Monoid[Stat1] = Monoid.instance(
    Stat1(Monoid[ResultStat].empty,
      Monoid[Processed].empty,
      Monoid[Failed].empty),

    (a, b) => Stat1(a.stat |+| b.stat,
      a.processed |+| b.processed,
      a.failed |+| b.failed)
  )

  def apply(id: SensorId, h: Option[Humidity]): Stat1 =
    h.fold(failed(id))(processed(id, _))

  def failed(id: SensorId): Stat1 =
    Stat1(Map(id -> None), failed = 1)

  def processed(id: SensorId, h: Humidity): Stat1 =
    Stat1(Map(id -> Some((Min(h), Avg(h), Max(h)))))
}
