package $package$.controllers

import javax.inject.{ Inject, Singleton, Provider}

import play.api.libs.json.Json.toJson
import play.api.mvc._
import play.api.{ Configuration, Environment }
import $package$.connectors.MicroserviceAuthConnector
import $package$.models.$modelname$
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import uk.gov.hmrc.agentmtdidentifiers.model.Utr

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class $servicenamecamel$Controller @Inject() (
  val authConnector: MicroserviceAuthConnector,
  val env: Environment,
  ecp: Provider[ExecutionContext])(implicit val configuration: Configuration)
  extends BaseController with AuthActions {

  implicit val ec: ExecutionContext = ecp.get

  def entities: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(toJson($modelname$("hello world", None, None, None))))
  }

  def entitiesByUtr(utr: Utr): Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(toJson($modelname$(s"hello \$utr", None, None, None))))
  }

}
