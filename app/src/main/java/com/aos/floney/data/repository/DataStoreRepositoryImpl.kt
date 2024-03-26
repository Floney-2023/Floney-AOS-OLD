package com.aos.floney.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import com.aos.floney.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    override suspend fun getDeviceToken(): Flow<String>? {
        return dataStore.data
            .map { preferences ->
                preferences[DEVICE_TOKEN]?.let { flowOf(it) }
            }.firstOrNull()
    }

    override suspend fun saveDeviceToken(deviceToken: String) {
        dataStore.edit {
            it[DEVICE_TOKEN] = deviceToken
        }
    }

    override suspend fun getSocialAccessToken(): Flow<String?> {
        return getStringValue(SOCIAL_ACCESS_TOKEN)
    }
    override suspend fun saveSocialToken(socialAccessToken: String, socialRefreshToken: String) {
        dataStore.edit {
            it[SOCIAL_ACCESS_TOKEN] = socialAccessToken
            it[SOCIAL_REFRESH_TOKEN] = socialRefreshToken
        }
    }

    override suspend fun getRefreshToken(): Flow<String?> {
        return getStringValue(REFRESH_TOKEN)
    }
    override suspend fun saveAccessToken(accessToken: String, refreshToken: String) {
        dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun getAccessToken(): Flow<String?> {
        return getStringValue(ACCESS_TOKEN)
    }
    override suspend fun getStringValue(key: Preferences.Key<String>): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {
                it[key]
            }
    }
    override suspend fun clearDataStore() {
        dataStore.edit { it.clear() }
    }

    companion object PreferencesKeys {
        private val SOCIAL_ACCESS_TOKEN: Preferences.Key<String> =
            stringPreferencesKey("social_access_token")
        private val SOCIAL_REFRESH_TOKEN: Preferences.Key<String> =
            stringPreferencesKey("social_refresh_token")
        private val ACCESS_TOKEN: Preferences.Key<String> = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN: Preferences.Key<String> = stringPreferencesKey("refresh_token")
        private val DEVICE_TOKEN: Preferences.Key<String> = stringPreferencesKey("device_token")
        private val USER_ID: Preferences.Key<Int> = intPreferencesKey("user_id")
        private val USER_INFO: Preferences.Key<String> = stringPreferencesKey("user_info")
    }
}