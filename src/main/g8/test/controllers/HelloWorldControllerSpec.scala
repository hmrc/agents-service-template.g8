package controllers

import connectors.BackendConnector
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Application, Configuration}
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.http.HeaderCarrier

class HelloWorldControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerSuite with BeforeAndAfterEach {

  implicit override lazy val app: Application = new GuiceApplicationBuilder().
    disable[com.kenshoo.play.metrics.PlayModule].build()

  implicit val mockMessages: MessagesApi = mock[MessagesApi]
  implicit val mockConfig: Configuration = mock[Configuration]
  implicit val backendConnector: BackendConnector = mock[BackendConnector]

  val mockHelloWorldController = new HelloWorldController()

  implicit val hc = new HeaderCarrier

  "HelloWorldController" should {
    "return Status: OK Body: empty" in {
      val response = mockHelloWorldController.helloWorld()(FakeRequest("GET", "/hello-world"))

      status(response) mustBe OK
    }
  }
}


