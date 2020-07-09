package sensors.service

import java.nio.file._

import akka.stream.Materializer
import cats._
import cats.implicits._
import sensors.model._
import sensors.service.impl.SensorsImpl._

import scala.concurrent.{ExecutionContext, Future}

trait SensorsService[F[_]] {

  @inline def defaultParallelism: Int = Runtime.getRuntime.availableProcessors()

  def processFile(file: Path)(implicit m: Materializer,
                              e: ExecutionContext): F[Stat2]

  def processData(path: Path, parallelism: Int = defaultParallelism)
                 (implicit m: Materializer, e: ExecutionContext): F[Stat]

  def apply(path: Path, parallelism: Int = defaultParallelism)
           (implicit m: Materializer, e: ExecutionContext, M: Monad[F]): F[String] =
    processData(path, parallelism).map(_.show)
}

object SensorsService {
  @inline def apply[F[_] : SensorsService]: SensorsService[F] =
    implicitly[SensorsService[F]]

  lazy val impl = SensorsService[Future]
}
