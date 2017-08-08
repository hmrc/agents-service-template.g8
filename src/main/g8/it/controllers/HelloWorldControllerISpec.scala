package controllers

import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest}

class HelloWorldControllerISpec extends BaseControllerISpec {
  private lazy val controller: HelloWorldController = app.injector.instanceOf[HelloWorldController]
}
