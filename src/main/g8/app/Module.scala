import java.net.{InetSocketAddress, URL}
import java.util.concurrent.TimeUnit.{MILLISECONDS, SECONDS}
import javax.inject.{Inject, Provider, Singleton}

import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.codahale.metrics.{MetricFilter, SharedMetricRegistries}
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import org.slf4j.MDC
import play.api.inject.ApplicationLifecycle
import play.api.{Configuration, Environment, Logger, Mode}
import uk.gov.hmrc.play.http.{HttpGet, HttpPost}
import wiring.WSVerbs

import scala.concurrent.{ExecutionContext, Future}

class Module(val environment: Environment, val configuration: Configuration) extends AbstractModule with ServicesConfig {

  def configure(): Unit = {
    lazy val appName = configuration.getString("appName").get
    lazy val loggerDateFormat: Option[String] = configuration.getString("logger.json.dateformat")

    Logger.info(s"Starting microservice : \$appName : in mode : \${environment.mode}")
    MDC.put("appName", appName)
    loggerDateFormat.foreach(str => MDC.put("logger.json.dateformat", str))

    bind(classOf[HttpGet]).toInstance(new WSVerbs)
    bind(classOf[HttpPost]).toInstance(new WSVerbs)
    bindBaseUrl("auth")
    bind(classOf[GraphiteStartUp]).asEagerSingleton()
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
class GraphiteStartUp @Inject()(val configuration: Configuration,
                                val environment: Environment,
                                lifecycle: ApplicationLifecycle,
                                implicit val ec: ExecutionContext) extends ServicesConfig {

  val metricsPluginEnabled: Boolean = getConfBool("metrics.enabled", default = false)
  val graphitePublisherEnabled: Boolean = getConfBool("microservice.metrics.graphite.enabled", default = false)

  if (metricsPluginEnabled && graphitePublisherEnabled) {
    val graphite = new Graphite(new InetSocketAddress(
      getConfString("microservice.metrics.graphite.host", "graphite"),
      getConfInt("microservice.metrics.graphite.port", 2003)))

    val prefix: String = getConfString("microservice.metrics.graphite.prefix", s"play.\${configuration.getString("appName").get}")

    val registryName: String = getConfString("metrics.name", "default")

    val reporter: GraphiteReporter = GraphiteReporter.forRegistry(
      SharedMetricRegistries.getOrCreate(registryName))
      .prefixedWith(s"\$prefix.\${java.net.InetAddress.getLocalHost.getHostName}")
      .convertRatesTo(SECONDS)
      .convertDurationsTo(MILLISECONDS)
      .filter(MetricFilter.ALL)
      .build(graphite)

    Logger.info("Graphite metrics enabled, starting the reporter")
    reporter.start(getConfInt("microservice.metrics.graphite.interval", 10).toLong, SECONDS)

    lifecycle.addStopHook { () =>
      Future successful reporter.stop()
    }
  } else {
    Logger.warn(s"Graphite metrics disabled, plugin = \$metricsPluginEnabled and publisher = \$graphitePublisherEnabled")
  }

}

trait ServicesConfig {

  def environment: Environment

  def configuration: Configuration

  val env: String = if (environment.mode == Mode.Test) "Test"
                    else configuration.getString("run.mode").getOrElse("Dev")

  private val rootServices = "microservice.services"
  private val envServices = s"\$env.microservice.services"
  private val playServices = s"govuk-tax.\$env.services"

  private val defaultProtocol: String =
    configuration.getString(s"\$rootServices.protocol")
      .getOrElse(configuration.getString(s"\$envServices.protocol")
        .getOrElse("http"))

  def baseUrl(serviceName: String) = {
    val protocol = getConfString(s"\$serviceName.protocol", defaultProtocol)
    val host = getConfString(s"\$serviceName.host", throw new RuntimeException(s"Could not find config \$serviceName.host"))
    val port = getConfInt(s"\$serviceName.port", throw new RuntimeException(s"Could not find config \$serviceName.port"))
    s"\$protocol://\$host:\$port"
  }

  private def keys(confKey: String) = Seq(
    s"\$rootServices.\$confKey",
    s"\$envServices.\$confKey",
    confKey,
    s"\$env.\$confKey",
    s"\$playServices.\$confKey"
  )

  private def read[A](confKey: String)(f: String => Option[A]): Option[A] =
    keys(confKey).foldLeft[Option[A]](None)((a, k) => a.orElse(f(k)))

  def getConfString(confKey: String, default: => String): String =
    read[String](confKey)(configuration.getString(_)).getOrElse(default)

  def getConfInt(confKey: String, default: => Int): Int =
    read[Int](confKey)(configuration.getInt(_)).getOrElse(default)

  def getConfBool(confKey: String, default: => Boolean): Boolean =
    read[Boolean](confKey)(configuration.getBoolean(_)).getOrElse(default)

}