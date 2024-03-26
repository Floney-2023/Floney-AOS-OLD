package com.aos.floney.domain.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveSocialToken(socialAccessToken: String, socialRefreshToken: String)

    suspend fun getSocialAccessToken(): Flow<String?>

    suspend fun saveAccessToken(accessToken: String = "", refreshToken: String = "")
    suspend fun getDeviceToken(): Flow<String>?

    suspend fun getStringValue(key: Preferences.Key<String>): Flow<String?>

    suspend fun getAccessToken(): Flow<String?>

    suspend fun getRefreshToken(): Flow<String?>

    suspend fun saveDeviceToken(deviceToken: String)

    suspend fun clearDataStore()

}