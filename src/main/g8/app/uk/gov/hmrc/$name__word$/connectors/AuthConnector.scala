package uk.gov.hmrc.$name;format="word"$.connectors

import java.net.URL
import javax.inject.{Inject, Named, Singleton}

import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.play.http.{HttpGet, HttpPost}

@Singleton
class AuthConnector @Inject()(@Named("auth-baseUrl") baseUrl: URL, val http: HttpGet with HttpPost) extends PlayAuthConnector {

  val serviceUrl = baseUrl.toString
}
