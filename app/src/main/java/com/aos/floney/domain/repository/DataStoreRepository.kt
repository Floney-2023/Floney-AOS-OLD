package com.aos.floney.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun getDeviceToken(): Flow<String>?

    suspend fun saveDeviceToken(deviceToken: String)

    suspend fun clearDataStore()
}