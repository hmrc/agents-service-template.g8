package $package$.services

import javax.inject.Inject

import com.google.inject.Singleton
import play.api.mvc.Request
import uk.gov.hmrc.agentmtdidentifiers.model.Arn
import $package$.models.$modelnamecamel$
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.AuditExtensions._
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model.DataEvent
import uk.gov.hmrc.play.http.logging.MdcLoggingExecutionContext._

import scala.concurrent.Future
import scala.util.Try

object $servicenamecamel$Event extends Enumeration {
  val $servicenamecamel$SomethingHappened = Value
  type $servicenamecamel$Event = Value
}

@Singleton
class AuditService @Inject() (val auditConnector: AuditConnector) {

  import $servicenamecamel$Event._

  def send$servicenamecamel$SomethingHappened(model: $modelnamecamel$, agentReference: Arn)(implicit hc: HeaderCarrier, request: Request[Any]): Unit = {

    auditEvent($servicenamecamel$Event.$servicenamecamel$SomethingHappened, "$servicenamehyphen$-frontend-something-happened",
      Seq(
        "agentReference" -> agentReference.value,
        "parameter1" -> model.parameter1,
        "telephoneNumber" -> model.telephoneNumber.getOrElse(""),
        "emailAddress" -> model.emailAddress.getOrElse("")
      ))
  }

  private[services] def auditEvent(event: $servicenamecamel$Event, transactionName: String, details: Seq[(String, Any)] = Seq.empty)(implicit hc: HeaderCarrier, request: Request[Any]): Future[Unit] = {
    send(createEvent(event, transactionName, details: _*))
  }

  private[services] def createEvent(event: $servicenamecamel$Event, transactionName: String, details: (String, Any)*)(implicit hc: HeaderCarrier, request: Request[Any]): DataEvent = {

    val detail = hc.toAuditDetails(details.map(pair => pair._1 -> pair._2.toString): _*)
    val tags = hc.toAuditTags(transactionName, request.path)
    DataEvent(
      auditSource = "$servicenamehyphen$-frontend",
      auditType = event.toString,
      tags = tags,
      detail = detail
    )
  }

  private[services] def send(events: DataEvent*)(implicit hc: HeaderCarrier): Future[Unit] = {
    Future {
      events.foreach { event =>
        Try(auditConnector.sendEvent(event))
      }
    }
  }

}
