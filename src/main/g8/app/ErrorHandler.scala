import javax.inject.Singleton

import play.api.http.HttpErrorHandler
import play.api.http.Status._
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}
import uk.gov.hmrc.play.http.{HttpException, Upstream4xxResponse, Upstream5xxResponse}

import scala.concurrent.Future

@Singleton
class ErrorHandler extends HttpErrorHandler {

  case class ErrorResponse(statusCode: Int, message: String, xStatusCode: Option[String] = None, requested: Option[String] = None)

  implicit val erFormats: OFormat[ErrorResponse] = Json.format[ErrorResponse]

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      Status(statusCode)(Json.toJson(ErrorResponse(statusCode, message, requested = Some(request.uri))))
    )
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    val errorResponse = exception match {
      case e: HttpException => ErrorResponse(e.responseCode, e.getMessage)
      case e: Upstream4xxResponse => ErrorResponse(e.reportAs, e.getMessage)
      case e: Upstream5xxResponse => ErrorResponse(e.reportAs, e.getMessage)
      case e: Throwable => ErrorResponse(INTERNAL_SERVER_ERROR, e.getMessage)
    }
    Future.successful(new Status(errorResponse.statusCode)(Json.toJson(errorResponse)))
  }
}


