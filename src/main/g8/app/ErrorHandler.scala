import javax.inject.{ Inject, Singleton }

import play.api.http.Status._
import play.api.http.{ DefaultHttpErrorHandler, HttpErrorConfig }
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc.{ RequestHeader, Result }
import play.api.{ Configuration, Environment, Logger }
import uk.gov.hmrc.http.{ HttpException, Upstream4xxResponse, Upstream5xxResponse }
import uk.gov.hmrc.play.microservice.bootstrap.ErrorResponse

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject() (val config: Configuration, val env: Environment)
  extends DefaultHttpErrorHandler(HttpErrorConfig(showDevErrors = true, playEditor = None), None, None) {

  implicit val erFormats = Json.format[ErrorResponse]

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
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
}