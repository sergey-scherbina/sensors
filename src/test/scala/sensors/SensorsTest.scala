package sensors

import java.nio.file._

import akka.actor.testkit.typed.scaladsl._
import cats.implicits._
import org.scalatest.funsuite._
import sensors.service.SensorsService
import sensors.service.impl.SensorsImpl._

import scala.concurrent.Future

class SensorsTest extends ScalaTestWithActorTestKit with AsyncFunSuiteLike {

  implicit val ec = testKit.system.executionContext

  lazy val sensors = SensorsService.impl

  test("data1") {
    sensors(Paths.get("src/test/resources/data1/"))
      .map(s => assert(s ==
        """
          |Num of processed files: 2
          |Num of processed measurements: 7
          |Num of failed measurements: 2
          |
          |Sensors with highest avg humidity:
          |
          |sensor-id,min,avg,max
          |s2,78,82,88
          |s1,10,54,98
          |s3,NaN,NaN,NaN"""
          .stripMargin))
  }

  test("empty") {
    sensors(Paths.get("src/test/resources/empty/"))
      .map(s => assert(s ==
        """
          |Num of processed files: 0
          |Num of processed measurements: 0
          |Num of failed measurements: 0
          |"""
          .stripMargin))
  }

  test("wrong") {
    sensors(Paths.get("src/test/resources/wrong/"))
      .map(s => assert(s ==
        """
          |Num of processed files: 1
          |Num of processed measurements: 0
          |Num of failed measurements: 0
          |"""
          .stripMargin))
  }

}
