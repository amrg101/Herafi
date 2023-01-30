package com.amrg.herafi.ui.screens.project_screen

import androidx.lifecycle.ViewModel
import com.amrg.herafi.data.local.repositeries.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    val userLoggedInFlow = userRepository.loggedInFlow
}