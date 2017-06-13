package controllers

import javax.inject.Inject


@Singleton
class HelloWorldController extends Controller {
	
	def helloWorld(): Action[AnyContent] = Action { 
		Ok 
	}
}
