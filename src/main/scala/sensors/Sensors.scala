package sensors

import java.nio.file._

import akka.actor.ActorSystem
import akka.stream.Materializer
import cats.implicits._
import sensors.service.SensorsService

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Sensors {

  lazy val sensors = SensorsService.impl

  object Path {
    def unapply(arg: Array[String]): Option[Path] = arg match {
      case Array(p) => Some(Paths.get(p))
      case _ => None
    }
  }

  def help() = println(
    """
      |* Program takes one argument: a path to directory.
      |*
      |* Directory contains many CSV files (*.csv),
      |* each with a daily report from one group leader.
      |*
      |* Format of the file: 1 header line + many lines with measurements.
      |* Measurement line has sensor id and the humidity value.
      |* Humidity value is integer in range `[0, 100]` or `NaN` (failed measurement).
      |* The measurements for the same sensor id can be in the different files.
      |""".stripMargin)

  def wrongPath(path: Path) = {
    println(s"Error: '$path' is not a directory")
    help()
  }

  def main(args: Array[String]): Unit = args match {
    case Path(path) =>
      if (Files.isDirectory(path)) {
        implicit val system = ActorSystem("Sensors")
        import system.dispatcher
        try {
          implicit val materializer = Materializer(system)
          Await.ready(sensors(path).map(println), Duration.Inf)
        } finally system.terminate()
      } else wrongPath(path)
    case _ => help()
  }
}

