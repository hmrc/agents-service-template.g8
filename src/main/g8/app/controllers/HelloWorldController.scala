package controllers

import javax.inject._

import play.api.mvc.{Action, AnyContent, Controller}

@Singleton
class HelloWorldController @Inject() extends Controller {

	def helloWorld(): Action[AnyContent] = Action {
		Ok
	}
}
