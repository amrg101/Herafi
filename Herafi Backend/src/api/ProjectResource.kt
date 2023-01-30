package com.nooblabs.api

import com.nooblabs.models.*
import com.nooblabs.service.IProjectService
import com.nooblabs.util.param
import com.nooblabs.util.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.article(articleService: IProjectService) {

    authenticate {

        /*
            Feed Articles
            GET /api/articles/feed
         */
        get("/projects/feed") {
            val params = call.parameters
            val filter = mapOf(
                "limit" to params["limit"],
                "offset" to params["offset"]
            )
            val articles = articleService.getFeedProjects(call.userId(), filter)
            call.respond(MultipleProjectsResponse(articles, articles.size))
        }

        /*
            Create Article
            POST /api/articles
         */
        post("/projects") {
            val newArticle = call.receive<NewProject>()
            val article = articleService.createProject(call.userId(), newArticle)
            call.respond(article)
        }

        /*
            Update Article
            PUT /api/articles/:slug
         */
        put("/projects/{slug}") {
            val slug = call.param("slug")
            val updateArticle = call.receive<UpdateProject>()
            val article = articleService.updateProject(call.userId(), slug, updateArticle)
            call.respond(article)
        }

        /*
            Favorite Article
            POST /api/articles/:slug/favorite
         */
        post("/projects/{slug}/favorite") {
            val slug = call.param("slug")
            val article = articleService.changeFavorite(call.userId(), slug, favorite = true)
            call.respond(article)
        }

        /*
            Unfavorite Article
            DELETE /api/articles/:slug/favorite
         */
        delete("/projects/{slug}/favorite") {
            val slug = call.param("slug")
            val article = articleService.changeFavorite(call.userId(), slug, favorite = false)
            call.respond(article)
        }

        /*
            Delete Article
            DELETE /api/articles/:slug
         */
        delete("/projects/{slug}") {
            val slug = call.param("slug")
            articleService.deleteProject(call.userId(), slug)
            call.respond(HttpStatusCode.OK)
        }
    }

    authenticate(optional = true) {

        /*
            List Articles
            GET /api/articles
         */
        get("/projects") {
            val userId = call.principal<UserIdPrincipal>()?.name
            val params = call.parameters
            val filter = mapOf(
                "tag" to params["tag"],
                "author" to params["author"],
                "favorited" to params["favorited"],
                "limit" to params["limit"],
                "offset" to params["offset"]
            )
            val articles = articleService.getProjects(userId, filter)
            call.respond(MultipleProjectsResponse(articles, articles.size))
        }

    }

    /*
        Get Article
        GET /api/articles/:slug
     */
    get("/projects/{slug}") {
        val slug = call.param("slug")
        val article = articleService.getProject(slug)
        call.respond(article)
    }

    /*
        Get Tags
        GET /api/tags
     */
    get("/tags") {
        call.respond(articleService.getAllTags())
    }

}