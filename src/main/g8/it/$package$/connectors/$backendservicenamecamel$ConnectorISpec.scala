package $package$.connectors

import java.net.URL

import com.github.tomakehurst.wiremock.client.WireMock._
import com.kenshoo.play.metrics.Metrics
import play.api.http.Status
import play.api.libs.json.Json
import $package$.controllers.BaseISpec
import $package$.models.$modelnamecamel$
import $package$.support.MetricsTestSupport
import uk.gov.hmrc.http._

import scala.concurrent.ExecutionContext.Implicits.global

class $backendservicenamecamel$ConnectorISpec extends BaseISpec with MetricsTestSupport {
  private implicit val hc = HeaderCarrier()

  private lazy val connector: $backendservicenamecamel$Connector = new $backendservicenamecamel$Connector(
    new URL(s"http://localhost:\$wireMockPort"),
    app.injector.instanceOf[HttpGet with HttpPost],
    app.injector.instanceOf[Metrics]
  )

  private val model = $modelnamecamel$(
    "Dave Agent",
    Some("AA1 1AA"),
    Some("0123456789"),
    Some("email@test.com")
  )

  "$backendservicenamecamel$Connector" when {

    "getSmth" should {

      "return 200" in {
        givenCleanMetricRegistry()
        stubFor(get(urlEqualTo(s"/$backendservicenamehyphen$/dosmth"))
          .willReturn(
            aResponse()
              .withStatus(Status.OK)
              .withBody(Json.obj("foo" -> "bar").toString())
          ))

        val response: HttpResponse = await(connector.getSmth())
        response.status shouldBe 200
        verifyTimerExistsAndBeenUpdated("ConsumedAPI-$backendservicenamehyphen$-smth-GET")
      }

      "throw an exception if no connection was possible" in {
        stopServer()
        intercept[BadGatewayException] {
          await(connector.getSmth())
        }
      }

      "throw an exception if the response is 400" in {
        stubFor(get(urlEqualTo(s"/$backendservicenamehyphen$/dosmth"))
          .willReturn(
            aResponse()
              .withStatus(Status.BAD_REQUEST)
          ))

        intercept[BadRequestException] {
          await(connector.getSmth())
        }
      }
    }

    "postSmth" should {

      "return 201" in {
        givenCleanMetricRegistry()
        stubFor(post(urlEqualTo(s"/$backendservicenamehyphen$/dosmth"))
          .willReturn(
            aResponse()
              .withStatus(Status.CREATED)
          ))

        val response: HttpResponse = await(connector.postSmth(model))
        response.status shouldBe 201
        verifyTimerExistsAndBeenUpdated("ConsumedAPI-$backendservicenamehyphen$-smth-POST")
      }

      "throw an exception if no connection was possible" in {
        stopServer()
        intercept[BadGatewayException] {
          await(connector.postSmth(model))
        }
      }

      "throw an exception if the response is 400" in {
        stubFor(post(urlEqualTo(s"/$backendservicenamehyphen$/dosmth"))
          .willReturn(
            aResponse()
              .withStatus(Status.BAD_REQUEST)
          ))

        intercept[BadRequestException] {
          await(connector.postSmth(model))
        }
      }
    }
  }

}
