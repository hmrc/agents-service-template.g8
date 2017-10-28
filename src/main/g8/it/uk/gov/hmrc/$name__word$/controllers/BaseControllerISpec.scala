package uk.gov.hmrc.$name;format="word"$.controllers

import org.scalatestplus.play.OneAppPerSuite
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.play.http.{HeaderCarrier, SessionKeys}
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.$name;format="word"$.stubs.AuthStubs
import uk.gov.hmrc.$name;format="word"$.support.WireMockSupport

class BaseControllerISpec extends UnitSpec with OneAppPerSuite with WireMockSupport with AuthStubs {

  override implicit lazy val app: Application = appBuilder.build()

  protected def appBuilder: GuiceApplicationBuilder = {
    new GuiceApplicationBuilder()
      .configure("microservice.services.auth.port" -> wireMockPort)
  }

  protected implicit val materializer = app.materializer

  protected def checkHtmlResultWithBodyText(result: Result, expectedSubstring: String): Unit = {
    status(result) shouldBe 200
    contentType(result) shouldBe Some("text/html")
    charset(result) shouldBe Some("utf-8")
    bodyOf(result) should include(expectedSubstring)
  }

  private val messagesApi = app.injector.instanceOf[MessagesApi]
  private implicit val messages: Messages = messagesApi.preferred(Seq.empty[Lang])

  protected def htmlEscapedMessage(key: String): String = HtmlFormat.escape(Messages(key)).toString

  implicit def hc(implicit request: FakeRequest[_]): HeaderCarrier = HeaderCarrier.fromHeadersAndSession(request.headers, Some(request.session))

}
