package com.amrg.herafi.ui.screens.edit_name_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrg.herafi.data.local.repositeries.UserRepository
import com.amrg.herafi.domain.use_cases.EditProfileUseCase
import com.amrg.herafi.shared.DataState
import com.amrg.herafi.shared.UiEvent
import com.amrg.herafi.shared.UiText
import com.amrg.herafi.utils.Validation
import com.amrg.herafi.ui.screens.edit_profile_screen.EditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNameViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {
    private val _uiState = mutableStateOf<EditUiState>(EditUiState.Initial)
    val uiState: State<EditUiState>
        get() = _uiState

    private val _uiEvent: Channel<UiEvent> = Channel()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    val fullNameFieldText = mutableStateOf("")
    val isErrorNameField = mutableStateOf(false)
    val nameFieldErrorMsg = mutableStateOf<UiText>(UiText.DynamicText(""))

    init {
        viewModelScope.launch {
            fullNameFieldText.value = userRepository.user().name
        }
    }

    fun onSubmit() {
        val validateFullNameResult = validateFullName()
        if (validateFullNameResult) {
            viewModelScope.launch {
                editProfileUseCase(
                    username = fullNameFieldText.value,
                ).collect { dataState ->
                    when (dataState) {
                        is DataState.Loading -> {
                            _uiState.value = EditUiState.Loading
                        }
                        is DataState.Error -> {
                            _uiState.value = EditUiState.Initial
                            _uiEvent.send(
                                UiEvent.ShowSnackBar(
                                    message = dataState.message,
                                )
                            )
                        }
                        is DataState.SuccessWithoutData -> {
                            _uiState.value = EditUiState.Initial
                            _uiEvent.send(UiEvent.PopBackStack)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun validateFullName(): Boolean {
        return when (val fullNameResId: Int? =
            Validation.validateFullName(fullNameFieldText.value)
        ) {
            null -> {
                isErrorNameField.value = false
                nameFieldErrorMsg.value = UiText.DynamicText("")
                true
            }
            else -> {
                isErrorNameField.value = true
                nameFieldErrorMsg.value = UiText.ResourceText(fullNameResId)
                false
            }
        }
    }
}