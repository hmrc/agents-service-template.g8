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
import uk.gov.hmrc.auth.core.PlayAuthConnector
import uk.gov.hmrc.auth.filter.{AuthorisationFilter, FilterConfig}
import uk.gov.hmrc.play.audit.filters.AuditFilter
import uk.gov.hmrc.play.audit.http.config.LoadAuditingConfig
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.config.inject.{DefaultServicesConfig, RunMode}
import uk.gov.hmrc.play.http.ws._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Defines the filters that are added to the application by extending the default Play filters
  * @param loggingFilter - used to log details of any http requests hitting the service
  * @param auditFilter - used to call the datastream microservice and publish auditing events
  * @param metricsFilter - used to collect metrics and statistics relating to the service
  * @param authFilter - used to add authorisation to endpoints in the service if required
  */
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
                                        configuration: Configuration, val auditConnector: MicroserviceAuditConnector) extends AuditFilter {

  override def controllerNeedsAuditing(controllerName: String): Boolean = configuration.getBoolean(s"controllers.$controllerName.needsAuditing").getOrElse(true)

  override def appName: String = configuration.getString("appName").get
}

class MicroserviceAuditConnector @Inject()(val environment: Environment) extends AuditConnector with RunMode {
  override lazy val auditingConfig = LoadAuditingConfig(s"auditing")
}

class MicroserviceAuthFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext,
                                       configuration: Configuration, val connector: AuthConn) extends AuthorisationFilter {
  override def config: FilterConfig = FilterConfig(configuration.underlying.as[Config]("controllers"))
}

class AuthConn @Inject()(defaultServicesConfig: DefaultServicesConfig,
			 val http: WsVerbs) extends PlayAuthConnector {

  override val serviceUrl: String = defaultServicesConfig.baseUrl("auth")
}

class WsVerbs extends WSHttp {
  override val hooks = NoneRequired
}


