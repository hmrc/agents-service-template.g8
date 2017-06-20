import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}
import javax.inject.Inject

import akka.stream.Materializer
import com.kenshoo.play.metrics.MetricsFilter
import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import play.api.http.DefaultHttpFilters
import play.api.mvc._
import play.api.{Configuration, Environment, Logger}
import uk.gov.hmrc.play.audit.filters.AuditFilter
import uk.gov.hmrc.play.audit.http.config.LoadAuditingConfig
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.auth.controllers.AuthParamsControllerConfig
import uk.gov.hmrc.play.auth.microservice.connectors.AuthConnector
import uk.gov.hmrc.play.auth.microservice.filters.AuthorisationFilter
import uk.gov.hmrc.play.config.RunMode
import uk.gov.hmrc.play.config.inject.ServicesConfig

import scala.concurrent.{ExecutionContext, Future}

class Filters @Inject()(loggingFilter: LoggingFilter, auditFilter: MicroserviceAuditFilter, metricsFilter: MetricsFilter,
authFilter: MicroserviceAuthFilter)
  extends DefaultHttpFilters(loggingFilter, auditFilter, metricsFilter, authFilter)

class LoggingFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    val timestamp = LocalDateTime.now(ZoneId.of("Europe/London")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
    val startTime = System.currentTimeMillis

    nextFilter(requestHeader).map { result =>

      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime

      Logger.info(s"$timestamp ${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}")
      result
    }
  }
}

class MicroserviceAuditFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext,
                                        configuration: Configuration,
                                        val auditConnector: MicroserviceAuditConnector) extends AuditFilter {

  override def controllerNeedsAuditing(controllerName: String): Boolean = configuration.getBoolean(s"controllers.$controllerName.needsAuditing").getOrElse(true)

  override def appName: String = configuration.getString("appName").get
}

class MicroserviceAuditConnector @Inject() extends AuditConnector with RunMode {
  override lazy val auditingConfig = LoadAuditingConfig(s"auditing")
}

class MicroserviceAuthFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext,
					val authConnector: MicroserviceAuthConnector) extends AuthorisationFilter {

}

class MicroserviceAuthFilter @Inject()(implicit val mat: Materializer, configuration: Configuration,
                              val authConnector: AuthConn,
                              val authParamsConfig: AuthConfig) extends AuthorisationFilter {
  override def controllerNeedsAuth(controllerName: String): Boolean = configuration.getBoolean(s"controllers.$controllerName.needsAuth").getOrElse(false)
}

class AuthConn @Inject()(val environment: Environment) extends AuthConnector with ServicesConfig {
  override def authBaseUrl: String = baseUrl("auth")
}

class AuthConfig @Inject()(configuration: Configuration) extends AuthParamsControllerConfig {
  override def controllerConfigs: Config = configuration.underlying.as[Config]("controllers")
}

