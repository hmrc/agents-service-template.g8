package $package$.connectors

import javax.inject.{ Inject, Singleton }

import play.api.Configuration
import uk.gov.hmrc.play.audit.http.config.{ AuditingConfig, BaseUri, Consumer }
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

@Singleton
class FrontendAuditConnector @Inject() (configuration: Configuration) extends AuditConnector {

  override lazy val auditingConfig = loadAuditingConfig(configuration.getConfig("auditing"))

  private def loadAuditingConfig(config: Option[Configuration]) = config.map { c =>
    val enabled = c.getBoolean("enabled").getOrElse(true)
    if (enabled) {
      AuditingConfig(
        enabled = enabled,
        consumer = Some(c.getConfig("consumer").map { con =>
          Consumer(
            baseUri = con.getConfig("baseUri").map { uri =>
              BaseUri(
                host = uri.getString("host").getOrElse(throw new Exception("Missing consumer host for auditing")),
                port = uri.getInt("port").getOrElse(throw new Exception("Missing consumer port for auditing")),
                protocol = uri.getString("protocol").getOrElse("http")
              )
            }.getOrElse(throw new Exception("Missing consumer baseUri for auditing"))
          )
        }.getOrElse(throw new Exception("Missing consumer configuration for auditing")))
      )
    } else {
      AuditingConfig(consumer = None, enabled = false)
    }
  }.getOrElse(throw new Exception("Missing auditing configuration"))
}
