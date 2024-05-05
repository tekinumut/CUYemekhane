package com.tekinumut.cuyemekhane.common.domain.usecase.base

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class SingleUseCase<in Params, Type>(
    private val dispatcher: CoroutineDispatcher
) {
    abstract suspend fun getExecutable(params: Params): Resource<Type>

    suspend operator fun invoke(params: Params): Resource<Type> {
        return withContext(dispatcher) {
            getExecutable(params)
        }
    }
}