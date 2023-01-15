package com.tekinumut.cuyemekhane.common.di

import com.tekinumut.cuyemekhane.common.data.repository.MainPageRepositoryImpl
import com.tekinumut.cuyemekhane.common.domain.repository.MainPageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Umut Tekin on 15.01.2023.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMainPageRepository(
        repositoryImpl: MainPageRepositoryImpl
    ): MainPageRepository
}