package com.tekinumut.cuyemekhane.common.di

import com.tekinumut.cuyemekhane.common.data.repository.MenuRepositoryImpl
import com.tekinumut.cuyemekhane.common.domain.repository.MenuRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMenuRepository(
        repositoryImpl: MenuRepositoryImpl
    ): MenuRepository
}