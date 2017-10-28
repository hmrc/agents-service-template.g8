package uk.gov.hmrc.$name;format="word"$.controllers

import play.api.mvc.{Request, Result}
import uk.gov.hmrc.agentmtdidentifiers.model.Arn
import uk.gov.hmrc.auth.core.AuthorisedFunctions
import uk.gov.hmrc.auth.core.authorise.{AffinityGroup, Enrolment, EnrolmentIdentifier}
import uk.gov.hmrc.auth.core.retrieve.AuthProvider.GovernmentGateway
import uk.gov.hmrc.auth.core.retrieve.AuthProviders
import uk.gov.hmrc.auth.core.retrieve.Retrievals.authorisedEnrolments
import uk.gov.hmrc.play.http.HeaderCarrier

import scala.concurrent.Future

trait AuthActions extends AuthorisedFunctions {

  protected def withAuthorisedAsAgent[A](body: Arn => Future[Result])
                                        (implicit request: Request[A], hc: HeaderCarrier): Future[Result] =
    withEnrolledFor("HMRC-AS-AGENT","AgentReferenceNumber")(body)

  protected def withEnrolledFor[A](serviceName: String, identifierKey: String)
                                  (body: Arn => Future[Result])
                                  (implicit request: Request[A], hc: HeaderCarrier): Future[Result] = {
    authorised(
      AffinityGroup.Agent
        and Enrolment(serviceName)
        and AuthProviders(GovernmentGateway)
    )
      .retrieve(authorisedEnrolments) { enrolments =>
        val idOpt: Option[EnrolmentIdentifier] = enrolments.getEnrolment(serviceName)
          .flatMap(e =>
            e.getIdentifier(identifierKey)
          )
        body(Arn(idOpt.get.value))
      }
  }

}
