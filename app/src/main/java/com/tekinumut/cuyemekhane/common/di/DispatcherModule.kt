package com.tekinumut.cuyemekhane.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Created by Umut Tekin on 15.01.2023.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Singleton
    @Provides
    @IODispatcher
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}