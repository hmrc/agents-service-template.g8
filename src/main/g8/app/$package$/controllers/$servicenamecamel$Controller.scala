package $package$.controllers

import javax.inject.{ Inject, Singleton }

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc._
import play.api.{ Configuration, Environment }
import $package$.connectors.{ $backendservicenamecamel$Connector, FrontendAuthConnector }
import $package$.models.$modelname$
import $package$.views.html
import uk.gov.hmrc.play.frontend.controller.FrontendController

import scala.concurrent.Future

@Singleton
class $servicenamecamel$Controller @Inject() (
  override val messagesApi: MessagesApi,
  $backendservicenamesmall$Connector:         $backendservicenamecamel$Connector,
  val authConnector:        FrontendAuthConnector,
  val env:                  Environment
)(implicit val configuration: Configuration)
  extends FrontendController with I18nSupport with AuthActions {

  import $servicenamecamel$Controller._

  val root: Action[AnyContent] = Action { implicit request =>
    Redirect(routes.$servicenamecamel$Controller.start().url)
  }

  val start: Action[AnyContent] = Action {
    implicit request =>
      Ok(html.start())
  }

  val show$formname$: Action[AnyContent] = Action.async {
    implicit request =>
      withAuthorisedAsAgent { arn =>
        Future.successful(
          Ok(html.$formnamesmall$($formname$))
        )
      }
  }

  val $formnamesmall$ = Action.async {
    implicit request =>
      withAuthorisedAsAgent { arn =>
        $formname$.bindFromRequest().fold(
          formWithErrors => {
            Future.successful(Ok(html.$formnamesmall$(formWithErrors)))
          },
          data => {
            Future.successful(Ok(html.summary($formname$.fill(data))))
          }
        )
      }
  }

}

object $servicenamecamel$Controller {

  import $package$.controllers.FieldMappings._

  val $formname$ = Form[$modelname$](
    mapping(
      "parameter1" -> validName,
      "parameter2" -> optional(postcode),
      "telephoneNumber" -> telephoneNumber,
      "emailAddress" -> emailAddress
    )($modelname$.apply)($modelname$.unapply)
  )
}
