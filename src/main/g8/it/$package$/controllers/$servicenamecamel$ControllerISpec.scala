package $package$.controllers

import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core._

import scala.concurrent.duration._

class $servicenamecamel$ControllerISpec extends BaseISpec {

  private lazy val controller: $servicenamecamel$Controller = app.injector.instanceOf[$servicenamecamel$Controller]

  "$servicenamecamel$Controller" when {

    "GET /" should {
      "redirect to /start" in {
        val result = controller.root(FakeRequest())
        status(result) shouldBe 303
        val timeout = 2.seconds
        redirectLocation(result)(timeout).get should include("/start")
      }
    }

    "GET /start" should {
      "display start page" in {
        val result = controller.start(FakeRequest())
        status(result) shouldBe 200
        checkHtmlResultWithBodyText(result, htmlEscapedMessage("start.title"))
      }
    }

    "GET /$formpath$" should {

      "show form page for authorised Agent" in {
        val request = authorisedAsValidAgent(FakeRequest(), "ARN0001")
        val result = await(controller.show$formname$(request))
        status(result) shouldBe 200
        checkHtmlResultWithBodyText(result, htmlEscapedMessage("$formpath$.title"))
        verifyAuthoriseAttempt()
      }

      "redirect to Login Page for an Agent with other enrolments" in {
        an[InsufficientEnrolments] shouldBe thrownBy {
          await(controller.show$formname$()(authenticated(FakeRequest(), Enrolment("OtherEnrolment", "Key", "Value"), isAgent = true)))
        }
        verifyAuthoriseAttempt()
      }

      "redirect to Login Page for no Agent" in {
        an[AuthorisationException] shouldBe thrownBy {
          await(controller.show$formname$()(authenticated(FakeRequest(), Enrolment("OtherEnrolment", "Key", "Value"), isAgent = false)))
        }
        verifyAuthoriseAttempt()
      }

      "redirect to Login Page for not logged in user" in {
        givenUnauthorisedWith("MissingBearerToken")
        a[NoActiveSession] shouldBe thrownBy {
          await(controller.show$formname$()(FakeRequest()))
        }
        verifyAuthoriseAttempt()
      }
    }

  }

}
