package com.amrg.herafi.data.local.repositeries

import androidx.datastore.core.DataStore
import com.amrg.herafi.domain.models.Settings
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val settingsDataStore: DataStore<Settings>
) {
    val settingsFlow = settingsDataStore.data

    suspend fun settings() = settingsDataStore.data.first()

    suspend fun update(transform: (Settings) -> Settings) {
        settingsDataStore.updateData(transform)
    }
}