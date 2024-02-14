package com.aos.floney.di

import com.aos.floney.data.repository.CalendarRepositoryImpl
import com.aos.floney.data.repository.DataStoreRepositoryImpl
import com.aos.floney.data.repository.UserRepositoryImpl
import com.aos.floney.domain.repository.CalendarRepository
import com.aos.floney.domain.repository.DataStoreRepository
import com.aos.floney.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun bindsDataStoreRepository(
        dataStoreRepositoryImpl: DataStoreRepositoryImpl
    ): DataStoreRepository

    @Singleton
    @Binds
    abstract fun bindsCalendarRepository(
        calendarRepositoryImpl: CalendarRepositoryImpl
    ): CalendarRepository
}