package uk.gov.hmrc.$name;format="word"$.controllers

import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core._

import scala.concurrent.duration._

class $name;format="Camel"$ControllerISpec extends BaseControllerISpec {

  private lazy val controller: $name;format="Camel"$Controller = app.injector.instanceOf[$name;format="Camel"$Controller]

  "$name;format="Camel"$Controller" when {

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

    "GET /myform" should {

      "show form page for authorised Agent" in {
        val request = authorisedAsValidAgent(FakeRequest(), "ARN0001")
        val result = await(controller.showMyForm(request))
        status(result) shouldBe 200
        checkHtmlResultWithBodyText(result, htmlEscapedMessage("myform.title"))
        verifyAuthoriseAttempt()
      }

      "redirect to Login Page for an Agent with other enrolments" in {
        an[InsufficientEnrolments] shouldBe thrownBy {
          await(controller.showMyForm()(authenticated(FakeRequest(), Enrolment("OtherEnrolment", "Key", "Value"), isAgent = true)))
        }
        verifyAuthoriseAttempt()
      }

      "redirect to Login Page for no Agent" in {
        an[AuthorisationException] shouldBe thrownBy {
          await(controller.showMyForm()(authenticated(FakeRequest(), Enrolment("OtherEnrolment", "Key", "Value"), isAgent = false)))
        }
        verifyAuthoriseAttempt()
      }

      "redirect to Login Page for not logged in user" in {
        givenUnauthorisedWith("MissingBearerToken")
        a[NoActiveSession] shouldBe thrownBy {
          await(controller.showMyForm()(FakeRequest()))
        }
        verifyAuthoriseAttempt()
      }
    }

  }

}
