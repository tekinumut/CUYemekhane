package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.mappers.MonthlyMenuMapper
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.repository.MenuRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.FlowUseCase
import com.tekinumut.cuyemekhane.monthlymenu.events.MonthlyMenuEvent
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MonthlyMenuUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
    private val menuDetailUseCase: MenuDetailUseCase,
    private val monthlyMenuMapper: MonthlyMenuMapper,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, MonthlyMenuEvent>(dispatcher) {

    override suspend fun getExecutable(params: Unit): Flow<MonthlyMenuEvent> {
        return flow {
            val mappedResponse = when (val response = menuRepository.getMonthlyMenu()) {
                is Resource.Success -> {
                    val monthlyMenuUIModel = monthlyMenuMapper.mapToUIModel(response.value)
                    if (monthlyMenuUIModel.dailyMenuList.isNotEmpty()) {
                        MonthlyMenuEvent.Success(monthlyMenuUIModel)
                    } else {
                        MonthlyMenuEvent.EmptyList
                    }
                }
                is Resource.Failure -> MonthlyMenuEvent.Failure(response.cuError)
            }
            emit(mappedResponse)
        }
    }
}