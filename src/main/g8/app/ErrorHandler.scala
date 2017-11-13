import javax.inject.{ Inject, Singleton }

import com.google.inject.name.Named
import play.api.http.HeaderNames.CACHE_CONTROL
import play.api.http.HttpErrorHandler
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.Results._
import play.api.mvc.{ RequestHeader, Result }
import play.api.{ Configuration, Environment, Mode }
import uk.gov.hmrc.auth.core.{ InsufficientEnrolments, NoActiveSession }
import uk.gov.hmrc.http.{ JsValidationException, NotFoundException }
import $package$.views.html.error_template
import uk.gov.hmrc.play.HeaderCarrierConverter
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.frontend.config.EventTypes.{ ResourceNotFound, ServerInternalError, ServerValidationError, TransactionFailureReason }
import uk.gov.hmrc.play.frontend.config.{ AuthRedirects, HttpAuditEvent }

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class ErrorHandler @Inject() (
                               val env: Environment,
                               val messagesApi: MessagesApi,
                               val auditConnector: AuditConnector,
                               @Named("appName") val appName: String)(implicit val config: Configuration, ec: ExecutionContext)
  extends HttpErrorHandler with I18nSupport with AuthRedirects with ErrorAuditing {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    auditClientError(request, statusCode, message)
    Future successful
      Status(statusCode)(error_template(
        Messages(s"global.error.\$statusCode.title"),
        Messages(s"global.error.\$statusCode.heading"),
        Messages(s"global.error.\$statusCode.message")))
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    auditServerError(request, exception)
    val response = exception match {
      case _: NoActiveSession => toGGLogin(if (env.mode.equals(Mode.Dev)) s"http://\${request.host}\${request.uri}" else s"\${request.uri}")
      case _: InsufficientEnrolments => Forbidden
      case _ => InternalServerError(error_template(
        Messages("global.error.500.title"),
        Messages("global.error.500.heading"),
        Messages("global.error.500.message"))).withHeaders(CACHE_CONTROL -> "no-cache")
    }
    Future.successful(response)
  }
}

trait ErrorAuditing extends HttpAuditEvent {

  def auditConnector: AuditConnector

  private val unexpectedError = "Unexpected error"
  private val notFoundError = "Resource Endpoint Not Found"
  private val badRequestError = "Request bad format exception"

  def auditServerError(request: RequestHeader, ex: Throwable)(implicit ec: ExecutionContext): Unit = {
    val eventType = ex match {
      case _: NotFoundException => ResourceNotFound
      case _: JsValidationException => ServerValidationError
      case _ => ServerInternalError
    }
    val transactionName = ex match {
      case _: NotFoundException => notFoundError
      case _ => unexpectedError
    }
    auditConnector.sendEvent(dataEvent(eventType, transactionName, request, Map(TransactionFailureReason -> ex.getMessage))(HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))))
  }

  def auditClientError(request: RequestHeader, statusCode: Int, message: String)(implicit ec: ExecutionContext): Unit = {
    import play.api.http.Status._
    statusCode match {
      case NOT_FOUND => auditConnector.sendEvent(dataEvent(ResourceNotFound, notFoundError, request, Map(TransactionFailureReason -> message))(HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))))
      case BAD_REQUEST => auditConnector.sendEvent(dataEvent(ServerValidationError, badRequestError, request, Map(TransactionFailureReason -> message))(HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))))
      case _ =>
    }
  }
}