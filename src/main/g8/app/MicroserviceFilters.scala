import javax.inject.Inject

import akka.stream.Materializer
import com.kenshoo.play.metrics.MetricsFilter
import play.api.Configuration
import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.microservice.filters._

import scala.concurrent.ExecutionContext

class MicroserviceFilters @Inject() (
                                      loggingFilter: LoggingFilter,
                                      auditFilter: MicroserviceAuditFilter,
                                      metricsFilter: MetricsFilter) extends HttpFilters {

  override def filters: Seq[EssentialFilter] = Seq(
    Some(metricsFilter),
    Some(auditFilter),
    Some(loggingFilter),
    Some(NoCacheFilter),
    Some(RecoveryFilter)).flatten
}

class LoggingFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext, configuration: Configuration)
  extends FrontendLoggingFilter {

  override def controllerNeedsLogging(controllerName: String): Boolean = configuration.getBoolean(s"controllers.\$controllerName.needsLogging").getOrElse(true)
}

class MicroserviceAuditFilter @Inject() (implicit val auditConnector: AuditConnector, val mat: Materializer, ec: ExecutionContext, configuration: Configuration)
  extends AuditFilter {

  override def controllerNeedsAuditing(controllerName: String): Boolean = configuration.getBoolean(s"controllers.\$controllerName.needsAuditing").getOrElse(true)

  override val appName: String = "TODO: PUT $servicenamehyphen$ IN TEMPLATE"
}

