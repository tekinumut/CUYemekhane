package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.mappers.MonthlyMenuMapper
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.MonthlyMenuUIModel
import com.tekinumut.cuyemekhane.common.domain.repository.MonthlyMenuRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.FlowUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MonthlyMenuUseCase @Inject constructor(
    private val monthlyMenuRepository: MonthlyMenuRepository,
    private val monthlyMenuMapper: MonthlyMenuMapper,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Resource<MonthlyMenuUIModel>>(dispatcher) {

    override suspend fun getExecutable(params: Unit): Flow<Resource<MonthlyMenuUIModel>> {
        return flow {
            val mappedResponse = when (val response = monthlyMenuRepository.getMonthlyMenu()) {
                is Resource.Success -> {
                    val monthlyMenuUIModel = monthlyMenuMapper.mapToUIModel(response.value)
                    Resource.Success(monthlyMenuUIModel)
                }
                is Resource.Failure -> Resource.Failure(response.cuError)
            }
            emit(mappedResponse)
        }
    }
}