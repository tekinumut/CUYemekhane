package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.common.data.model.mappers.MainPageMapper
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.MainPageUIModel
import com.tekinumut.cuyemekhane.common.domain.repository.MainPageRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Umut Tekin on 15.01.2023.
 */
class MainPageUseCase @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val mainPageRepository: MainPageRepository,
    private val mainPageMapper: MainPageMapper
) : FlowUseCase<Unit, Resource<MainPageUIModel>>(dispatcher) {

    override suspend fun getExecutable(params: Unit): Flow<Resource<MainPageUIModel>> {
        return flow {
            val mappedResponse = when (val response = mainPageRepository.getMainPage()) {
                is Resource.Success -> Resource.Success(mainPageMapper.mapToUIModel(response.value))
                is Resource.Failure -> Resource.Failure(response.cuError)
            }
            emit(mappedResponse)
        }
    }
}