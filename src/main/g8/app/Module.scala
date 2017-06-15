import com.google.inject.AbstractModule
import org.slf4j.MDC
import play.api.{Configuration, Environment, Logger}

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  def configure(): Unit = {
    lazy val appName = configuration.getString("appName").get
    lazy val loggerDateFormat: Option[String] = configuration.getString("logger.json.dateformat")

    Logger.info(s"Starting microservice : $appName : in mode : ${environment.mode}")
    MDC.put("appName", appName)
    loggerDateFormat.foreach(str => MDC.put("logger.json.dateformat", str))
  }
}

