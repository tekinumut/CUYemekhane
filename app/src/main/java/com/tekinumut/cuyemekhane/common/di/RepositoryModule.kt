package com.tekinumut.cuyemekhane.common.di

import com.tekinumut.cuyemekhane.common.data.repository.MonthlyMenuRepositoryImpl
import com.tekinumut.cuyemekhane.common.data.repository.TodayMenuRepositoryImpl
import com.tekinumut.cuyemekhane.common.domain.repository.MonthlyMenuRepository
import com.tekinumut.cuyemekhane.common.domain.repository.TodayMenuRepository
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
    abstract fun bindTodayMenuRepository(
        repositoryImpl: TodayMenuRepositoryImpl
    ): TodayMenuRepository

    @Binds
    @Singleton
    abstract fun bindMonthlyMenuRepository(
        repositoryImpl: MonthlyMenuRepositoryImpl
    ): MonthlyMenuRepository
}