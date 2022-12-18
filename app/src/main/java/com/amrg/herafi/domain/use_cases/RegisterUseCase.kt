package com.amrg.herafi.domain.use_cases

import com.amrg.herafi.R
import com.amrg.herafi.data.remote.models.requests.RegisterRequest
import com.amrg.herafi.data.repositeries.AuthRepository
import com.amrg.herafi.shared.DataState
import com.amrg.herafi.shared.HerafiException
import com.amrg.herafi.shared.UiText
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, username: String, password: String) =
        flow<DataState<Nothing>> {
            emit(DataState.Loading())
            try {
//                delay(3000)
                authRepository.register(
                    RegisterRequest(
                        RegisterRequest.User(
                        email = email,
                        password = password,
                        username = username,
                        )
                 )
                )
                emit(DataState.SuccessWithoutData())
            } catch (ex: UnknownHostException) {
                emit(DataState.Error(UiText.ResourceText(R.string.can_not_reach_the_server)))
            } catch (ex: ConnectException) {
                emit(DataState.Error(UiText.ResourceText(R.string.bad_internet_connection)))
            } catch (ex: SocketTimeoutException) {
                emit(DataState.Error(UiText.ResourceText(R.string.bad_internet_connection)))
            } catch (ex: HerafiException) {
                emit(DataState.Error(UiText.ResourceText(resId = R.string.email_already_exists)))
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