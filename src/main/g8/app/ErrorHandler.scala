import javax.inject.{Inject, Singleton}

import play.api.{Configuration, Environment, Mode}
import play.api.http.HeaderNames.CACHE_CONTROL
import play.api.http.HttpErrorHandler
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}
import uk.gov.hmrc.$name;format="word"$.views.html.error_template
import uk.gov.hmrc.auth.core.{InsufficientEnrolments, NoActiveSession}
import uk.gov.hmrc.auth.frontend.Redirects

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject()(implicit val config: Configuration, val env: Environment, val messagesApi: MessagesApi)
  extends HttpErrorHandler with I18nSupport with Redirects {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future successful
      Status(statusCode)(error_template(
        Messages(s"global.error.\$statusCode.title"),
        Messages(s"global.error.\$statusCode.heading"),
        Messages(s"global.error.\$statusCode.message")))
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
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