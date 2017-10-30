package $package$.connectors

import java.net.URL
import javax.inject.{ Inject, Named, Singleton }

import com.codahale.metrics.MetricRegistry
import com.kenshoo.play.metrics.Metrics
import uk.gov.hmrc.agent.kenshoo.monitoring.HttpAPIMonitor
import $package$.models.$modelnamecamel$

import scala.concurrent.{ ExecutionContext, Future }
import uk.gov.hmrc.http.{ HeaderCarrier, HttpGet, HttpPost, HttpResponse }

@Singleton
class $backendservicenamecamel$Connector @Inject() (@Named("$backendservicenamehyphen$-baseUrl") baseUrl: URL, http: HttpGet with HttpPost, metrics: Metrics) extends HttpAPIMonitor {

  override val kenshooRegistry: MetricRegistry = metrics.defaultRegistry

  def getSmth()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    monitor(s"ConsumedAPI-$backendservicenamehyphen$-smth-GET") {
      http.GET[HttpResponse](new URL(baseUrl, "/$backendservicenamehyphen$/dosmth").toExternalForm)
    }
  }

  def postSmth(model: $modelnamecamel$)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    monitor(s"ConsumedAPI-$backendservicenamehyphen$-smth-POST") {
      http.POST[$modelnamecamel$, HttpResponse](new URL(baseUrl, "/$backendservicenamehyphen$/dosmth").toExternalForm, model)
    }
  }

}
