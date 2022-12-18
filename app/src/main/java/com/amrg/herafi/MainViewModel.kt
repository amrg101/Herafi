package com.amrg.herafi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrg.herafi.data.local.repositeries.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val loggedIn = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            if(userRepository.user().firstLaunch) {
                userRepository.update {
                    it.copy(firstLaunch = false)
                }
            }
            delay(100)
            loggedIn.emit(userRepository.loggedInFlow.first())
        }
    }
}