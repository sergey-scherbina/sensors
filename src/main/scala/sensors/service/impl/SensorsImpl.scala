package sensors.service.impl

import java.nio.file._

import akka.stream.Materializer
import akka.stream.alpakka.csv.scaladsl._
import akka.stream.scaladsl.{FileIO, Source}
import cats._
import cats.implicits._
import sensors.model._
import sensors.service.SensorsService

import scala.concurrent.{ExecutionContext, Future}

object SensorsImpl {

  implicit object akkaStreamsSensorsService extends SensorsService[Future] {

    def processFile(file: Path)(implicit m: Materializer,
                                e: ExecutionContext) =
      FileIO.fromPath(file)
        .via(CsvParsing.lineScanner())
        .via(CsvToMap.toMap())
        .collect { case InputData(id, h) => Stat1(id, h) }
        .runFold(Monoid[Stat1].empty)(_ |+| _)
        .map(Stat2(_))

    def processData(path: Path, parallelism: Int = defaultParallelism)
                   (implicit m: Materializer, e: ExecutionContext) =
      Source.fromJavaStream(() => Files.list(path)
        .filter(f => f.getFileName.toString.endsWith(".csv") && f.toFile.isFile))
        .mapAsyncUnordered(parallelism)(processFile)
        .runFold(Monoid[Stat2].empty)(_ |+| _)
        .map(Stat(_))

  }

}
