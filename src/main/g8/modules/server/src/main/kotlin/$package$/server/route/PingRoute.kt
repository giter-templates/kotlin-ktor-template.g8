package $package$.server.route

import $package$.server.model.*
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

fun Route.pingRoute() {
    route("/ping") {
        get {
            call.respondText(ContentType.Application.Json, HttpStatusCode.OK, suspend {
                Json.encodeToString(Ping("pong"))
            })
        }
    }
}
