package com.nooblabs.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.*

object Projects : UUIDTable() {
    val slug = varchar("slug", 255)
    var title = varchar("title", 255)
    val description = varchar("description", 255)
    val body = varchar("body", 255)
    val author = reference("author", Users)
    val createdAt = timestamp("createdAt").default(Instant.now())
    val updatedAt = timestamp("updatedAt").default(Instant.now())
}

object Tags : UUIDTable() {
    val tagName = varchar("tagName", 255).uniqueIndex()
}

object ProjectTags : Table() {
    val project = reference(
        "project",
        Projects,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val tag = reference(
        "tag",
        Tags,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    override val primaryKey = PrimaryKey(project, tag)
}

object FavoriteProject : Table() {
    val project = reference("project", Projects)
    val user = reference("user", Users)

    override val primaryKey = PrimaryKey(project, user)
}

class Tag(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Tag>(Tags)

    var tag by Tags.tagName
}

class Project(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Project>(Projects) {
        fun generateSlug(title: String) = title.lowercase(Locale.US).replace(" ", "-")
    }

    var slug by Projects.slug
    var title by Projects.title
    var description by Projects.description
    var body by Projects.body
    var tags by Tag via ProjectTags
    var author by Projects.author
    var favoritedBy by User via FavoriteProject
    var createdAt by Projects.createdAt
    var updatedAt by Projects.updatedAt
    var comments by Comment via ProjectComment
}

data class NewProject(val project: Project) {
    data class Project(
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String> = emptyList()
    )
}

data class UpdateProject(val project: Project) {
    data class Project(
        val title: String? = null,
        val description: String? = null,
        val body: String? = null
    )
}

data class ProjectResponse(val project: Project) {
    data class Project(
        val slug: String,
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String>,
        val createdAt: String,
        val updatedAt: String,
        val favorited: Boolean = false,
        val favoritesCount: Int = 0,
        val author: ProfileResponse.Profile
    )
}

data class MultipleProjectsResponse(val projects: List<ProjectResponse.Project>, val projectsCount: Int)

data class TagResponse(val tags: List<String>)