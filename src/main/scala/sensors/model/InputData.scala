package sensors.model

import akka.util.ByteString
import cats.implicits._

import scala.util.Try

object InputData {

  type InputData = (SensorId, Option[Humidity])

  def sensorId(id: ByteString): SensorId = id.utf8String

  def humidity(h: ByteString): Option[Humidity] =
    Try(h.utf8String.toByte).toOption

  def unapply(m: Map[String, ByteString]): Option[InputData] =
    (m.get("sensor-id"), m.get("humidity"))
      .mapN((id, h) => (sensorId(id), humidity(h)))

}
