package $package$.connectors

import java.net.URL
import javax.inject.{ Inject, Named, Singleton }

import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.http.HttpPost
import uk.gov.hmrc.play.http.ws.WSPost
import com.typesafe.config.Config
import play.api.Configuration
import akka.actor.ActorSystem


@Singleton
class MicroserviceAuthConnector @Inject() (@Named("auth-baseUrl") baseUrl: URL, val config: Configuration, val _actorSystem: ActorSystem)
  extends PlayAuthConnector {

  override val serviceUrl = baseUrl.toString

  override def http = new HttpPost with WSPost {
    override val hooks = NoneRequired
    override protected def configuration: Option[Config] = Some(config.underlying)
    override protected def actorSystem: ActorSystem = _actorSystem
  }
}
