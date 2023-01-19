package com.amrg.herafi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrg.herafi.data.local.repositeries.SettingsRepository
import com.amrg.herafi.data.local.repositeries.UserRepository
import com.amrg.herafi.domain.use_cases.ValidateTokenUseCase
import com.amrg.herafi.shared.DataState
import com.amrg.herafi.shared.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    validateTokenUseCase: ValidateTokenUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    val darkModeFlow = settingsRepository.settingsFlow.map { it.darkMode }

    var loading: Boolean = true

    private val _uiEvent: Channel<UiEvent> = Channel()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    suspend fun settings() = settingsRepository.settings()

    val loggedIn = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            if (userRepository.user().firstLaunch) {
                userRepository.update {
                    it.copy(firstLaunch = false)
                }
            }
            //delay(1000)

            loggedIn.emit(userRepository.loggedInFlow.first())

            validateTokenUseCase.invoke().collect { dataState ->
                when (dataState) {
                    !is DataState.Loading -> {
                        loggedIn.emit(userRepository.loggedInFlow.first())
                        delay(500)
                        loading = false
                    }
                    else -> Unit
                }
            }
        }
    }
}