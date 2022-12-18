package com.amrg.herafi.ui.screens.register_screen

import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrg.herafi.HerafiApplication
import com.amrg.herafi.R
import com.amrg.herafi.domain.use_cases.RegisterUseCase
import com.amrg.herafi.shared.DataState
import com.amrg.herafi.shared.UiEvent
import com.amrg.herafi.shared.UiText
import com.amrg.herafi.ui.navigation.Screen
import com.amrg.herafi.ui.screens.register_screen.RegisterUiState
import com.amrg.herafi.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) : ViewModel() {
    private val _uiState = mutableStateOf<RegisterUiState>(RegisterUiState.Initial)
    val uiState: State<RegisterUiState>
        get() = _uiState

    private val _uiEvent: Channel<UiEvent> = Channel()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    val emailFieldText = mutableStateOf("")
    val nameFieldText = mutableStateOf("")
    val passwordFieldText = mutableStateOf("")
    val confirmPasswordFieldText = mutableStateOf("")
    val phoneNumberFieldText = mutableStateOf("")

    val isErrorEmailField = mutableStateOf(false)
    val isErrorNameField = mutableStateOf(false)
    val isErrorPasswordField = mutableStateOf(false)
    val isErrorConfirmPasswordField = mutableStateOf(false)
    val isErrorPhoneNumberField = mutableStateOf(false)

    val passwordVisible = mutableStateOf(false)
    val confirmPasswordVisible = mutableStateOf(false)

    val emailFieldErrorMsg = mutableStateOf<UiText>(UiText.DynamicText(""))
    val nameFieldErrorMsg = mutableStateOf<UiText>(UiText.DynamicText(""))
    val passwordFieldErrorMsg = mutableStateOf<UiText>(UiText.DynamicText(""))
    val confirmPasswordFieldErrorMsg = mutableStateOf<UiText>(UiText.DynamicText(""))
    val phoneNumberFieldErrorMsg = mutableStateOf<UiText>(UiText.DynamicText(""))


    fun onSubmit() {
        val validateEmailResult = validateEmail()
        val validateFullNameResult = validateFullName()
        val validatePasswordResult = validatePassword()
        val validateConfirmPasswordResult = validateConfirmPassword()
        if (validateEmailResult && validateFullNameResult
            && validatePasswordResult && validateConfirmPasswordResult
        ) {
            viewModelScope.launch {
                registerUseCase(
                    email = emailFieldText.value,
                    username = nameFieldText.value,
                    password = passwordFieldText.value,
                ).collect { dataState ->
                    when (dataState) {
                        is DataState.Loading -> {
                            _uiState.value = RegisterUiState.Loading
                        }
                        is DataState.Error -> {
                            _uiState.value = RegisterUiState.Initial
                            _uiEvent.send(
                                UiEvent.ShowSnackBar(
                                    message = dataState.message,
                                )
                            )
                        }
                        is DataState.SuccessWithoutData -> {
                            _uiState.value = RegisterUiState.Initial
                            _uiEvent.send(UiEvent.Navigate(route = Screen.LoginScreen.route))
                            Toast.makeText(HerafiApplication.applicationContext, "Register Success", Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun validateEmail(): Boolean {
        return when (val emailResId: Int? = Validation.validateEmail(emailFieldText.value)
        ) {
            null -> {
                isErrorEmailField.value = false
                emailFieldErrorMsg.value = UiText.DynamicText("")
                true
            }
            else -> {
                isErrorEmailField.value = true
                emailFieldErrorMsg.value = UiText.ResourceText(emailResId)
                false
            }
        }
    }

    private fun validateFullName(): Boolean {
        return when (val fullNameResId: Int? =
            Validation.validateFullName(nameFieldText.value)
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

    private fun validatePassword(): Boolean {
        return when (val passwordResId: Int? =
            Validation.validateRegisterPassword(passwordFieldText.value)
        ) {
            null -> {
                isErrorPasswordField.value = false
                passwordFieldErrorMsg.value = UiText.DynamicText("")
                true
            }
            else -> {
                isErrorPasswordField.value = true
                passwordFieldErrorMsg.value = UiText.ResourceText(passwordResId)
                false
            }
        }
    }

    private fun validateConfirmPassword(): Boolean {
        return when (val confirmPasswordResId: Int? =
            Validation.validateConfirmPassword(confirmPasswordFieldText.value)
        ) {
            null -> {
                if (confirmPasswordFieldText.value == passwordFieldText.value) {
                    isErrorConfirmPasswordField.value = false
                    confirmPasswordFieldErrorMsg.value = UiText.DynamicText("")
                    true
                } else {
                    isErrorConfirmPasswordField.value = true
                    confirmPasswordFieldErrorMsg.value =
                        UiText.ResourceText(R.string.passwords_not_identical)
                    false
                }
            }
            else -> {
                isErrorConfirmPasswordField.value = true
                confirmPasswordFieldErrorMsg.value = UiText.ResourceText(confirmPasswordResId)
                false
            }
        }
    }

}