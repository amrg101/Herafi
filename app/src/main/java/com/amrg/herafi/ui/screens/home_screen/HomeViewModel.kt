package com.amrg.herafi.ui.screens.home_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrg.herafi.data.local.repositeries.UserRepository
import com.amrg.herafi.domain.use_cases.LogoutUseCase
import com.amrg.herafi.shared.DataState
import com.amrg.herafi.shared.UiEvent
import com.amrg.herafi.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    userRepository: UserRepository,
) : ViewModel() {

    val userLoggedInFlow = userRepository.loggedInFlow

    private val _uiEvent: Channel<UiEvent> = Channel()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    val loggedInFlow = userRepository.loggedInFlow

    val logOutPopUpDialogState = mutableStateOf<Boolean>(false)
    val loadingDialogState = mutableStateOf<Boolean>(false)

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().collect { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                        loadingDialogState.value = true
                    }
                    is DataState.Error -> {
                        loadingDialogState.value = false
                        _uiEvent.send(
                            UiEvent.ShowSnackBar(
                                message = dataState.message,
                            )
                        )
                    }
                    is DataState.SuccessWithoutData -> {
                        loadingDialogState.value = false
                        _uiEvent.send(
                            UiEvent.Navigate(route = Screen.LoginScreen.route)
                        )
                    }
                    else -> Unit
                }
            }
        }
    }
}