import javax.inject.{ Inject, Singleton, Named }

import play.api.http.Status._
import play.api.http.{ DefaultHttpErrorHandler, HttpErrorConfig }
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc.{ RequestHeader, Result }
import play.api.{ Configuration, Environment, Logger }
import uk.gov.hmrc.http.{ HttpException, Upstream4xxResponse, Upstream5xxResponse }
import uk.gov.hmrc.play.microservice.bootstrap.ErrorResponse
import uk.gov.hmrc.auth.core.{ InsufficientEnrolments, NoActiveSession }
import uk.gov.hmrc.http.{ JsValidationException, NotFoundException }
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.HeaderCarrierConverter
import uk.gov.hmrc.play.microservice.config.EventTypes.{ ResourceNotFound, ServerInternalError, ServerValidationError, TransactionFailureReason }
import uk.gov.hmrc.play.microservice.config.HttpAuditEvent

import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class ErrorHandler @Inject() (val env: Environment, val auditConnector: AuditConnector, @Named("appName") val appName: String)(implicit val config: Configuration, ec: ExecutionContext)
  extends DefaultHttpErrorHandler(HttpErrorConfig(showDevErrors = true, playEditor = None), None, None) with ErrorAuditing {

  implicit val erFormats = Json.format[ErrorResponse]

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    auditServerError(request, exception)
    val message = s"! Internal server error, for (\${request.method}) [\${request.uri}] -> "
    Logger.error(message, exception)
    Future.successful(resolveError(exception))
  }

  private def resolveError(ex: Throwable): Result = {
    val errorResponse = ex match {
      case e: HttpException => ErrorResponse(e.responseCode, e.getMessage)
      case e: Upstream4xxResponse => ErrorResponse(e.reportAs, e.getMessage)
      case e: Upstream5xxResponse => ErrorResponse(e.reportAs, e.getMessage)
      case e: Throwable => ErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage)
    }

    new Status(errorResponse.statusCode)(Json.toJson(errorResponse))
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful {
      val er = ErrorResponse(BAD_REQUEST, error)
      BadRequest(Json.toJson(er))
    }
  }

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    auditClientError(request, statusCode, message)
    super.onClientError(request, statusCode, message)
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