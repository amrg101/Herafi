package com.amrg.herafi.ui.screens.projects_screen

import com.amrg.herafi.domain.models.Project


sealed class ProjectsUiState {
    data class Success(val projects: List<Project>) : ProjectsUiState()
    object Loading : ProjectsUiState()
    object Error : ProjectsUiState()
}
