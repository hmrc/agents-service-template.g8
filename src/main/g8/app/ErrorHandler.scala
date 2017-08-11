import javax.inject.{Inject, Singleton}

import play.api.Configuration
import play.api.http.HeaderNames.CACHE_CONTROL
import play.api.http.HttpErrorHandler
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}
import uk.gov.hmrc.agentepayeregistrationfrontend.views.html.error_template

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject()(implicit configuration: Configuration, val messagesApi: MessagesApi) extends HttpErrorHandler with I18nSupport {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future successful
      Status(statusCode)(error_template(
        Messages(s"global.error.$statusCode.title"),
        Messages(s"global.error.$statusCode.heading"),
        Messages(s"global.error.$statusCode.message")))
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future successful InternalServerError(error_template(
      Messages("global.error.500.title"),
      Messages("global.error.500.heading"),
      Messages("global.error.500.message"))).withHeaders(CACHE_CONTROL -> "no-cache")
  }
}