import javax.inject.{ Inject, Named }

import akka.stream.Materializer
import com.kenshoo.play.metrics.MetricsFilter
import org.joda.time.Duration
import play.api.Configuration
import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
import play.filters.csrf.CSRFFilter
import play.filters.headers.SecurityHeadersFilter
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.frontend.filters._

import scala.concurrent.ExecutionContext

class FrontendFilters @Inject() (
  loggingFilter:         LoggingFilter,
  auditFilter:           AuditFilter,
  metricFilter:          MetricsFilter,
  csrfFilter:            CSRFFilter,
  configuration:         Configuration,
  auditConnector:        AuditConnector,
  securityHeadersFilter: SecurityHeadersFilter,
  @Named("appName") appName: String
) extends HttpFilters {

  val enableSecurityHeaderFilter: Boolean = configuration.getBoolean("security.headers.filter.enabled").getOrElse(true)

  val deviceIdCookieFilter = DeviceIdCookieFilter(appName, auditConnector)

  val csrfExceptionsFilter: CSRFExceptionsFilter = {
    val uriWhiteList = configuration
      .getStringSeq("csrfexceptions.whitelist")
      .getOrElse(Seq.empty).toSet

    new CSRFExceptionsFilter(uriWhiteList)
  }

  val sessionTimeoutFilter: SessionTimeoutFilter = {
    val defaultTimeout = Duration.standardMinutes(15)
    val timeoutDuration = configuration
      .getLong("session.timeoutSeconds")
      .map(Duration.standardSeconds)
      .getOrElse(defaultTimeout)

    val wipeIdleSession = configuration
      .getBoolean("session.wipeIdleSession")
      .getOrElse(true)

    val additionalSessionKeysToKeep = configuration
      .getStringSeq("session.additionalSessionKeysToKeep")
      .getOrElse(Seq.empty).toSet

    new SessionTimeoutFilter(
      timeoutDuration = timeoutDuration,
      additionalSessionKeysToKeep = additionalSessionKeysToKeep,
      onlyWipeAuthToken = !wipeIdleSession)
  }

  val frontendFilters: Seq[EssentialFilter] = Seq(
    metricFilter,
    HeadersFilter,
    SessionCookieCryptoFilter,
    deviceIdCookieFilter,
    loggingFilter,
    auditFilter,
    sessionTimeoutFilter,
    csrfExceptionsFilter,
    csrfFilter,
    CacheControlFilter.fromConfig("caching.allowedContentTypes"),
    RecoveryFilter
  )

  override def filters: Seq[EssentialFilter] = if (enableSecurityHeaderFilter) Seq(securityHeadersFilter) ++ frontendFilters
  else frontendFilters

}

class LoggingFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext, configuration: Configuration)
  extends FrontendLoggingFilter {

  override def controllerNeedsLogging(controllerName: String): Boolean = configuration.getBoolean(s"controllers.\$controllerName.needsLogging").getOrElse(true)
}

class AuditFilter @Inject() (implicit val auditConnector: AuditConnector, val mat: Materializer, ec: ExecutionContext, configuration: Configuration)
  extends FrontendAuditFilter {

  override val maskedFormFields = Seq("password")

  override def controllerNeedsAuditing(controllerName: String): Boolean = configuration.getBoolean(s"controllers.\$controllerName.needsAuditing").getOrElse(true)

  override val appName: String = "$servicenamehyphen$"

  override val applicationPort: Option[Int] = None
}

