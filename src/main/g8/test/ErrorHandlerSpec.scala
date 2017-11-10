import org.scalatestplus.play.OneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.BadGatewayException
import uk.gov.hmrc.play.test.UnitSpec

class ErrorHandlerSpec extends UnitSpec with OneAppPerSuite {

  val handler: ErrorHandler = app.injector.instanceOf[ErrorHandler]

  "ErrorHandler should show the error page" when {
    "a server error occurs" in {
      val result = handler.onServerError(FakeRequest(), new BadGatewayException("bad gateway message"))

      status(result) shouldBe BAD_GATEWAY
      contentType(result) shouldBe Some(JSON)

      (contentAsJson(result) \ "statusCode").as[Int] shouldBe BAD_GATEWAY
      (contentAsJson(result) \ "message").as[String] shouldBe "bad gateway message"
    }

    "a client error (400) occurs" in {
      val result = handler.onClientError(FakeRequest(), BAD_REQUEST, "bad request message")

      status(result) shouldBe BAD_REQUEST
      contentType(result) shouldBe Some(JSON)

      (contentAsJson(result) \ "statusCode").as[Int] shouldBe BAD_REQUEST
      (contentAsJson(result) \ "message").as[String] shouldBe "bad request message"
    }

    "a client error (404) occurs" in {
      val result = handler.onClientError(FakeRequest(), NOT_FOUND, "")

      status(result) shouldBe NOT_FOUND
      contentType(result) shouldBe Some(HTML)
    }
  }
}
