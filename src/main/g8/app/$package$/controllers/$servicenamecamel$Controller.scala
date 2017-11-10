package $package$.controllers

import javax.inject.{ Inject, Singleton }

import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.libs.json.Json.toJson
import play.api.mvc._
import play.api.{ Configuration, Environment }
import $package$.connectors.MicroserviceAuthConnector
import $package$.models.$modelname$
import uk.gov.hmrc.play.microservice.controller.BaseController

import scala.concurrent.Future

@Singleton
class $servicenamecamel$Controller @Inject() (
                                            override val messagesApi: MessagesApi,
                                            val authConnector: MicroserviceAuthConnector,
                                            val env: Environment)(implicit val configuration: Configuration)
  extends BaseController with I18nSupport with AuthActions {

  val entity: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(toJson($modelname$("hello world", None, None, None))))
  }
}
