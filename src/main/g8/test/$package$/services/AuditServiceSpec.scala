package $package$.services

import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.concurrent.Eventually
import org.scalatest.mockito.MockitoSugar
import org.scalatest.time.{ Millis, Span }
import play.api.test.FakeRequest
import uk.gov.hmrc.agentmtdidentifiers.model.Arn
import $package$.models.$modelnamecamel$
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.{ Authorization, RequestId, SessionId }
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model.DataEvent
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.ExecutionContext

class AuditServiceSpec extends UnitSpec with MockitoSugar with Eventually {

  override implicit val patienceConfig = PatienceConfig(
    timeout = scaled(Span(500, Millis)),
    interval = scaled(Span(200, Millis))
  )

  "auditService" should {

    "send an $servicenamecamel$SomethingHappened event with the correct fields" in {
      val mockConnector = mock[AuditConnector]
      val service = new AuditService(mockConnector)

      val hc = HeaderCarrier(
        authorization = Some(Authorization("dummy bearer token")),
        sessionId = Some(SessionId("dummy session id")),
        requestId = Some(RequestId("dummy request id"))
      )

      val model = $modelnamecamel$(
        parameter1 = "John Smith",
        parameter2 = None,
        telephoneNumber = Some("12313"),
        emailAddress = Some("john.smith@email.com")
      )

      await(service.send$servicenamecamel$SomethingHappened(model, Arn("ARN0001"))(hc, FakeRequest("GET", "/path")))

      eventually {
        val captor = ArgumentCaptor.forClass(classOf[DataEvent])
        verify(mockConnector).sendEvent(captor.capture())(any[HeaderCarrier], any[ExecutionContext])
        val sentEvent = captor.getValue.asInstanceOf[DataEvent]

        sentEvent.auditType shouldBe "$servicenamecamel$SomethingHappened"
        sentEvent.auditSource shouldBe "$servicenamehyphen$-frontend"
        sentEvent.detail("agentReference") shouldBe "ARN0001"
        sentEvent.detail("parameter1") shouldBe "John Smith"
        sentEvent.detail("telephoneNumber") shouldBe "12313"
        sentEvent.detail("emailAddress") shouldBe "john.smith@email.com"

        sentEvent.tags.contains("Authorization") shouldBe false
        sentEvent.detail("Authorization") shouldBe "dummy bearer token"

        sentEvent.tags("transactionName") shouldBe "$servicenamehyphen$-frontend-something-happened"
        sentEvent.tags("path") shouldBe "/path"
        sentEvent.tags("X-Session-ID") shouldBe "dummy session id"
        sentEvent.tags("X-Request-ID") shouldBe "dummy request id"
      }
    }
  }
}
