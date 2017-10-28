package uk.gov.hmrc.$name;format="word"$.controllers

import java.net.URL
import javax.inject.{Inject, Named, Singleton}

import scala.concurrent.Future
import play.api.{Configuration, Environment, Logger, Mode}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.agentmtdidentifiers.model.Arn
import uk.gov.hmrc.auth.core.authorise.{AffinityGroup, Enrolment, EnrolmentIdentifier}
import uk.gov.hmrc.auth.core.retrieve.AuthProvider.GovernmentGateway
import uk.gov.hmrc.auth.core.retrieve.AuthProviders
import uk.gov.hmrc.auth.core.retrieve.Retrievals.authorisedEnrolments
import uk.gov.hmrc.auth.core.{AuthorisedFunctions, InsufficientEnrolments, NoActiveSession}
import uk.gov.hmrc.auth.frontend.Redirects
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.$name;format="word"$.views.html
import uk.gov.hmrc.$name;format="word"$.connectors.AuthConnector
import uk.gov.hmrc.$name;format="word"$.connectors.BackendConnector
import uk.gov.hmrc.$name;format="word"$.models.Model

@Singleton
class $name;format="Camel"$Controller @Inject()(override val messagesApi: MessagesApi,
                                                backendConnector: BackendConnector,
                                                val authConnector: AuthConnector,
                                                val env: Environment
                                               ) (implicit val configuration: Configuration)
extends FrontendController with I18nSupport with AuthActions {

  import $name;format="Camel"$Controller._

  val root: Action[AnyContent] = Action { implicit request =>
    Redirect(routes.$name;format="Camel"$Controller.start().url)
  }

  val start: Action[AnyContent] = Action {
    implicit request =>
      Ok(html.start())
  }

  val showMyForm: Action[AnyContent] = Action.async {
    implicit request =>
      withAuthorisedAsAgent { arn =>
        Future.successful(
          Ok(html.myForm(MyForm))
        )
    }
  }

  val myForm = Action.async {
    implicit request =>
      withAuthorisedAsAgent { arn =>
        MyForm.bindFromRequest ().fold (
          formWithErrors => {
            Future.successful (Ok (html.myForm (formWithErrors) ) )
          },
          data => {
            Future.successful (Ok (html.summary (MyForm.fill (data) ) ) )
          }
        )
      }
  }


}

object $name;format="Camel"$Controller {

  import uk.gov.hmrc.$name;format="word"$.controllers.FieldMappings._

  val MyForm = Form[Model](
    mapping(
      "parameter1" -> validName,
      "parameter2" -> optional(postcode),
      "telephoneNumber" -> telephoneNumber,
      "emailAddress" -> emailAddress
    )(Model.apply)(Model.unapply)
  )
}
