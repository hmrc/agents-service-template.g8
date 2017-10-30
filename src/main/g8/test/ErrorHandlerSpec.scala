import org.scalatestplus.play.OneAppPerSuite
import play.api.i18n.MessagesApi
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.http.BadGatewayException
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.Future

class ErrorHandlerSpec extends UnitSpec with OneAppPerSuite {

  val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]
  val handler: ErrorHandler = app.injector.instanceOf[ErrorHandler]

  "ErrorHandler should show the error page" when {
    "a server error occurs" in {
      val result = handler.onServerError(FakeRequest(), new BadGatewayException(""))

      status(result) shouldBe INTERNAL_SERVER_ERROR
      contentType(result) shouldBe Some(HTML)
      checkIncludesMessages(result, "global.error.500.title", "global.error.500.heading", "global.error.500.message")
    }

    "a client error (400) occurs" in {
      val result = handler.onClientError(FakeRequest(), BAD_REQUEST, "")

      status(result) shouldBe BAD_REQUEST
      contentType(result) shouldBe Some(HTML)
      checkIncludesMessages(result, "global.error.400.title", "global.error.400.heading", "global.error.400.message")
    }

    "a client error (404) occurs" in {
      val result = handler.onClientError(FakeRequest(), NOT_FOUND, "")

      status(result) shouldBe NOT_FOUND
      contentType(result) shouldBe Some(HTML)
      checkIncludesMessages(result, "global.error.404.title", "global.error.404.heading", "global.error.404.message")
    }
  }

  private def checkIncludesMessages(result: Future[Result], messageKeys: String*): Unit = {
    messageKeys.foreach { messageKey =>
      messagesApi.isDefinedAt(messageKey) shouldBe true
      contentAsString(result) should include(HtmlFormat.escape(messagesApi(messageKey)).toString)
    }
  }
}
