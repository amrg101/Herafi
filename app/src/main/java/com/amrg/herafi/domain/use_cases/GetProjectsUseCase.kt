package com.amrg.herafi.domain.use_cases

import com.amrg.herafi.R
import com.amrg.herafi.data.local.repositeries.UserRepository
import com.amrg.herafi.data.repositeries.ProjectsRepository
import com.amrg.herafi.shared.DataState
import com.amrg.herafi.shared.HerafiException
import com.amrg.herafi.shared.UiText
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class GetProjectsUseCase @Inject constructor(
    private val projectsRepository: ProjectsRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(tag: String? = null) = flow {
        emit(DataState.Loading())
        try {
            val (articles, articlesCount) = projectsRepository.getProjects(userRepository.user(), tag = tag)
            emit(DataState.Success(articles))
        } catch (ex: UnknownHostException) {
            emit(DataState.Error(UiText.ResourceText(R.string.can_not_reach_the_server)))
        } catch (ex: ConnectException) {
            emit(DataState.Error(UiText.ResourceText(R.string.bad_internet_connection)))
        } catch (ex: SocketTimeoutException) {
            emit(DataState.Error(UiText.ResourceText(R.string.bad_internet_connection)))
        } catch (ex: HerafiException) {
            emit(DataState.Error(UiText.DynamicText(ex.message)))
        } catch (ex: Exception) {
            emit(
                DataState.Error(
                    if (ex.message != null)
                        UiText.DynamicText(ex.message!!)
                    else
                        UiText.ResourceText(R.string.something_went_wrong)
                )
            )
        }
    }
}