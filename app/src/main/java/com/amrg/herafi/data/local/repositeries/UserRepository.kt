package com.amrg.herafi.data.local.repositeries

import androidx.datastore.core.DataStore
import com.amrg.herafi.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataStore: DataStore<User>
) {
    val userFlow = userDataStore.data
    suspend fun user() = userDataStore.data.first()

    private val _loggedInFlow = MutableStateFlow(value = false)
    val loggedInFlow: StateFlow<Boolean> = _loggedInFlow

    suspend fun triggerLoggedInValue(loggedIn: Boolean) {
        _loggedInFlow.emit(loggedIn)
    }

    suspend fun update(transform: (User) -> User) {
        userDataStore.updateData(transform)
    }
}