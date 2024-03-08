package com.aos.floney.di

import com.aos.floney.data.service.CalendarService
import com.aos.floney.data.service.MypageService
import com.aos.floney.data.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideCalendarService(retrofit: Retrofit): CalendarService =
        retrofit.create(CalendarService::class.java)

    @Provides
    @Singleton
    fun provideMypageService(retrofit: Retrofit): MypageService =
        retrofit.create(MypageService::class.java)
}