package com.amrg.herafi.domain.use_cases

import com.amrg.herafi.R
import com.amrg.herafi.data.local.repositeries.UserRepository
import com.amrg.herafi.data.remote.models.requests.EditProfileRequest
import com.amrg.herafi.data.remote.models.responses.toUser
import com.amrg.herafi.data.repositeries.AuthRepository
import com.amrg.herafi.shared.DataState
import com.amrg.herafi.shared.HerafiException
import com.amrg.herafi.shared.UiText
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class EditProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        username: String? = null,
        oldPassword: String? = null,
        password: String? = null,
    ) = flow<DataState<Nothing>> {
        emit(DataState.Loading())
        try {
            val user = authRepository.editProfile(
                editProfileRequest = EditProfileRequest(
                    user = EditProfileRequest.User(
                        username = username,
                        oldPassword = oldPassword,
                        password = password
                    )
                ),
                token = userRepository.user().token
            ).toUser()
            userRepository.update {
                it.copy(name = user.name, email = user.email, token = user.token)
            }
            emit(DataState.SuccessWithoutData())
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