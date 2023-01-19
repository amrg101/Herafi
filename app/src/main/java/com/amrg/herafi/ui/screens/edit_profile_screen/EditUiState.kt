package com.amrg.herafi.ui.screens.edit_profile_screen

sealed class EditUiState {
    object Initial : EditUiState()
    object Loading : EditUiState()
}
