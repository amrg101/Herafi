package config

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.nooblabs.util.*
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.statusPages() {
    exception<Throwable> { call, cause ->
        when (cause) {
            is AuthenticationException -> call.respond(HttpStatusCode.Unauthorized)
            is AuthorizationException -> call.respond(HttpStatusCode.Forbidden)
            is ValidationException -> call.respond(HttpStatusCode.UnprocessableEntity, mapOf("errors" to cause.params))
            is UserExists -> call.respond(
                HttpStatusCode.UnprocessableEntity,
                mapOf("errors" to "user exists")
            )
            is UserDoesNotExists, is ProjectDoesNotExist, is CommentNotFound -> call.respond(
                HttpStatusCode.NotFound,
                mapOf("errors" to "user not exists")
            )
            is MissingKotlinParameterException -> call.respond(
                HttpStatusCode.UnprocessableEntity,
                mapOf("errors" to mapOf(cause.parameter.name to listOf("can't be empty")))
            )

        }
    }
}