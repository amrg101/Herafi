package com.nooblabs.service

import com.nooblabs.models.*
import com.nooblabs.util.ProjectDoesNotExist
import com.nooblabs.util.AuthorizationException
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.Instant

interface IProjectService {
    suspend fun createProject(userId: String, newProject: NewProject): ProjectResponse

    suspend fun updateProject(userId: String, slug: String, updateProject: UpdateProject): ProjectResponse

    suspend fun getProject(slug: String): ProjectResponse

    suspend fun getProjects(userId: String? = null, filter: Map<String, String?>): List<ProjectResponse.Project>

    suspend fun getFeedProjects(userId: String, filter: Map<String, String?>): List<ProjectResponse.Project>

    suspend fun changeFavorite(userId: String, slug: String, favorite: Boolean): ProjectResponse

    suspend fun deleteProject(userId: String, slug: String)

    suspend fun getAllTags(): TagResponse
}

class ProjectService(private val databaseFactory: IDatabaseFactory) : IProjectService {

    override suspend fun createProject(userId: String, newProject: NewProject): ProjectResponse {
        return databaseFactory.dbQuery {
            val user = getUser(userId)
            val project = Project.new {
                title = newProject.project.title
                slug = Project.generateSlug(newProject.project.title)
                description = newProject.project.description
                body = newProject.project.body
                author = user.id
            }
            val tags = newProject.project.tagList.map { tag -> getOrCreateTag(tag) }
            project.tags = SizedCollection(tags)
            getProjectResponse(project, user)
        }
    }

    override suspend fun updateProject(userId: String, slug: String, updateProject: UpdateProject): ProjectResponse {
        return databaseFactory.dbQuery {
            val user = getUser(userId)
            val project = getProjectBySlug(slug)
            if (!isProjectAuthor(project, user)) throw AuthorizationException()
            if (updateProject.project.title != null) {
                project.slug = Project.generateSlug(updateProject.project.title)
                project.title = updateProject.project.title
                project.updatedAt = Instant.now()
            }
            getProjectResponse(project, user)
        }
    }

    override suspend fun getProject(slug: String): ProjectResponse {
        return databaseFactory.dbQuery {
            val project = getProjectBySlug(slug)
            getProjectResponse(project)
        }
    }

    override suspend fun getProjects(userId: String?, filter: Map<String, String?>): List<ProjectResponse.Project> {
        return databaseFactory.dbQuery {
            val user = if (userId != null) getUser(userId) else null
            getAllProjects(
                currentUser = user,
                tag = filter["tag"],
                authorUserName = filter["author"],
                favoritedByUserName = filter["favorited"],
                limit = filter["limit"]?.toInt() ?: 20,
                offset = filter["offset"]?.toInt() ?: 0
            )
        }
    }

    override suspend fun getFeedProjects(userId: String, filter: Map<String, String?>): List<ProjectResponse.Project> {
        return databaseFactory.dbQuery {
            val user = getUser(userId)
            getAllProjects(
                currentUser = user,
                limit = filter["limit"]?.toInt() ?: 20,
                offset = filter["offset"]?.toInt() ?: 0,
                follows = true
            )
        }
    }

    override suspend fun changeFavorite(userId: String, slug: String, favorite: Boolean): ProjectResponse {
        return databaseFactory.dbQuery {
            val user = getUser(userId)
            val project = getProjectBySlug(slug)
            if (favorite) {
                favoriteProject(project, user)
            } else {
                unfavoriteProject(project, user)
            }
            getProjectResponse(project, user)
        }
    }

    override suspend fun deleteProject(userId: String, slug: String) {
        databaseFactory.dbQuery {
            val user = getUser(userId)
            val project = getProjectBySlug(slug)
            if (!isProjectAuthor(project, user)) throw AuthorizationException()
            project.delete()
        }
    }

    override suspend fun getAllTags(): TagResponse {
        return databaseFactory.dbQuery {
            val tags = Tag.all().map { it.tag }
            TagResponse(tags)
        }
    }

    private fun getAllProjects(
        currentUser: User? = null,
        tag: String? = null,
        authorUserName: String? = null,
        favoritedByUserName: String? = null,
        limit: Int = 20,
        offset: Int = 0,
        follows: Boolean = false
    ): List<ProjectResponse.Project> {
        val author = if (authorUserName != null) getUserByUsername(authorUserName) else null
        val projects = Project.find {
            if (author != null) (Projects.author eq author.id) else Op.TRUE
        }.limit(limit, offset.toLong()).orderBy(Projects.createdAt to SortOrder.DESC)
        val filteredProjects = projects.filter { project ->
            if (favoritedByUserName != null) {
                val favoritedByUser = getUserByUsername(favoritedByUserName)
                isFavoritedProject(project, favoritedByUser)
            } else {
                true
            }
                    &&
                    if (tag != null && tag != "all") {
                        project.tags.any { it.tag == tag }
                    } else {
                        true
                    }
                    &&
                    if (follows) {
                        val projectAuthor = getUser(project.author.toString())
                        isFollower(projectAuthor, currentUser)
                    } else {
                        true
                    }
        }
        return filteredProjects.map {
            getProjectResponse(it, currentUser).project
        }
    }

    private fun favoriteProject(project: Project, user: User) {
        if (!isFavoritedProject(project, user)) {
            project.favoritedBy = SizedCollection(project.favoritedBy.plus(user))
        }
    }

    private fun unfavoriteProject(project: Project, user: User) {
        if (isFavoritedProject(project, user)) {
            project.favoritedBy = SizedCollection(project.favoritedBy.minus(user))
        }
    }
}

fun isFavoritedProject(project: Project, user: User?) =
    if (user != null) project.favoritedBy.any { it == user } else false

fun getProjectBySlug(slug: String) =
    Project.find { Projects.slug eq slug }.firstOrNull() ?: throw ProjectDoesNotExist(slug)

fun getOrCreateTag(tagName: String) =
    Tag.find { Tags.tagName eq tagName }.firstOrNull() ?: Tag.new { this.tag = tagName }

fun getProjectResponse(project: Project, currentUser: User? = null): ProjectResponse {
    val author = getUser(project.author.toString())
    val tagList = project.tags.map { it.tag }
    val favoriteCount = project.favoritedBy.count()
    val favorited = isFavoritedProject(project, currentUser)
    val following = isFollower(author, currentUser)
    val authorProfile = getProfileByUser(getUser(project.author.toString()), following).profile!!
    return ProjectResponse(
        project = ProjectResponse.Project(
            slug = project.slug,
            title = project.title,
            description = project.description,
            body = project.body,
            tagList = tagList,
            createdAt = project.createdAt.toString(),
            updatedAt = project.updatedAt.toString(),
            favorited = favorited,
            favoritesCount = favoriteCount.toInt(),
            author = authorProfile
        )
    )
}

fun isProjectAuthor(project: Project, user: User) = project.author == user.id