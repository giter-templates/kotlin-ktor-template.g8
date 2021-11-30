package $package$.server

import $package$.server.route.pingRoute
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.metrics.micrometer.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.registerRoutes() {
    routing {
        pingRoute()
    }
}

fun Application.metrics(prometheusRegistry: PrometheusMeterRegistry) {
    install(MicrometerMetrics) {
        registry = prometheusRegistry
        meterBinders = listOf(
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics()
        )
    }

    routing {
        get("/metrics") {
            call.respond(prometheusRegistry.scrape())
        }
    }
}

fun Application.module() {
    val appMicrometerRegistry: PrometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(ContentNegotiation) {
        json()
    }

    metrics(appMicrometerRegistry)
    registerRoutes()
}
