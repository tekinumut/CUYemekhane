package com.tekinumut.cuyemekhane.common.di

import com.tekinumut.cuyemekhane.common.helpers.resource.ResourceHelper
import com.tekinumut.cuyemekhane.common.helpers.resource.ResourceHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ResourceHelperModule {

    @Binds
    fun bindResourceHelper(
        resourceHelperImpl: ResourceHelperImpl
    ): ResourceHelper
}