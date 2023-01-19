package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.common.data.model.mainpage.MainPageResponse
import com.tekinumut.cuyemekhane.common.data.model.mappers.MainPageMapper
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.MonthlyMenuUIModel
import com.tekinumut.cuyemekhane.common.domain.repository.MainPageRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Umut Tekin on 15.01.2023.
 */
class MonthlyMenuUseCase @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val mainPageRepository: MainPageRepository,
    private val mainPageMapper: MainPageMapper
) : FlowUseCase<Unit, Resource<MonthlyMenuUIModel>>(dispatcher) {

    override suspend fun getExecutable(params: Unit): Flow<Resource<MonthlyMenuUIModel>> {
        return flow {
            emit(Resource.Loading)
            val response = mainPageRepository.getMainPage()
            emit(getMappedResponse(response))
        }
    }

    private fun getMappedResponse(resource: Resource<MainPageResponse>): Resource<MonthlyMenuUIModel> {
        return when (resource) {
            is Resource.Failure -> Resource.Failure(resource.cuError)
            Resource.Loading -> Resource.Loading
            is Resource.Success -> {
                val mainPageUIModel = mainPageMapper.mapToUIModel(resource.value)
                Resource.Success(mainPageUIModel.monthlyMenu)
            }
        }
    }
}