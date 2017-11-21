package $package$.controllers

import org.scalatest.Suite
import org.scalatestplus.play.ServerProvider
import play.api.libs.json.Json
import play.api.libs.ws.{ WSClient, WSResponse }
import $package$.support.ServerBaseISpec

class $servicenamecamel$ControllerISpec extends ServerBaseISpec {

  this: Suite with ServerProvider =>

  val url = s"http://localhost:\$port/$servicenamehyphen$"

  val wsClient = app.injector.instanceOf[WSClient]

  def entity(): WSResponse = {
    wsClient.url(s"\$url/entities")
      .get()
      .futureValue
  }

  "$servicenamecamel$Controller" when {

    "GET /entities" should {
      "respond with some data" in {
        val result = entity()
        result.status shouldBe 200
        result.json shouldBe Json.obj("parameter1" -> "hello world")
      }
    }
  }
}
