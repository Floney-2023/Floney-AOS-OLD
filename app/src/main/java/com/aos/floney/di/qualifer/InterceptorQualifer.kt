package com.aos.floney.di.qualifer


import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Logger

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Auth