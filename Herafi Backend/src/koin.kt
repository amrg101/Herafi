package com.nooblabs

import com.nooblabs.service.*
import org.koin.dsl.module

val serviceKoinModule = module {
    single<IProjectService> { ProjectService(get()) }
    single<IAuthService> { AuthService(get()) }
    single<ICommentService> { CommentService(get()) }
    single<IProfileService> { ProfileService(get()) }
}

val databaseKoinModule = module {
    single<IDatabaseFactory> { DatabaseFactory() }
}