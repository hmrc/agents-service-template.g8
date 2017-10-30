import java.net.URL
import javax.inject.{ Inject, Provider, Singleton }

import com.google.inject.AbstractModule
import com.google.inject.name.{ Named, Names }
import org.slf4j.MDC
import play.api.{ Configuration, Environment, Logger }
import $package$.connectors.{ FrontendAuditConnector, FrontendAuthConnector }
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.http._
import uk.gov.hmrc.play.audit.http.HttpAuditing
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.config.inject.ServicesConfig
import uk.gov.hmrc.play.http.ws.WSHttp

class FrontendModule(val environment: Environment, val configuration: Configuration) extends AbstractModule with ServicesConfig {

  override val runModeConfiguration: Configuration = configuration

  def configure(): Unit = {
    val appName = configuration.getString("appName").getOrElse(throw new Exception("Missing 'appName' config property"))

    val loggerDateFormat: Option[String] = configuration.getString("logger.json.dateformat")
    Logger.info(s"Starting microservice : \$appName : in mode : \${environment.mode}")
    MDC.put("appName", appName)
    loggerDateFormat.foreach(str => MDC.put("logger.json.dateformat", str))

    bindProperty("appName")

    bind(classOf[HttpGet]).to(classOf[HttpVerbs])
    bind(classOf[HttpPost]).to(classOf[HttpVerbs])
    bind(classOf[AuthConnector]).to(classOf[FrontendAuthConnector])
    bind(classOf[AuditConnector]).to(classOf[FrontendAuditConnector])

    bindBaseUrl("auth")
    bindBaseUrl("$backendservicenamehyphen$")
  }

  private def bindBaseUrl(serviceName: String) =
    bind(classOf[URL]).annotatedWith(Names.named(s"\$serviceName-baseUrl")).toProvider(new BaseUrlProvider(serviceName))

  private class BaseUrlProvider(serviceName: String) extends Provider[URL] {
    override lazy val get = new URL(baseUrl(serviceName))
  }

  private def bindProperty(propertyName: String) =
    bind(classOf[String]).annotatedWith(Names.named(propertyName)).toProvider(new PropertyProvider(propertyName))

  private class PropertyProvider(confKey: String) extends Provider[String] {
    override lazy val get = configuration.getString(confKey)
      .getOrElse(throw new IllegalStateException(s"No value found for configuration property \$confKey"))
  }

}

@Singleton
class HttpVerbs @Inject() (val auditConnector: AuditConnector, @Named("appName") val appName: String)
  extends HttpGet with HttpPost with HttpPut with HttpPatch with HttpDelete with WSHttp
  with HttpAuditing {
  override val hooks = Seq(AuditingHook)
}
