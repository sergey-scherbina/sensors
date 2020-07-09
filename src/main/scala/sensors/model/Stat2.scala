package sensors.model

import cats._
import cats.implicits._

case class Stat2(stat: Stat1, files: Files = 1)

object Stat2 {
  implicit val monoid: Monoid[Stat2] = Monoid.instance(
    Stat2(Monoid[Stat1].empty, Monoid[Files].empty),

    (a, b) => Stat2(a.stat |+| b.stat, a.files |+| b.files)
  )
}
