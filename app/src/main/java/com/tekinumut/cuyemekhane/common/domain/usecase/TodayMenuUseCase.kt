package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.mappers.TodayMenuMapper
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.repository.MenuRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.FlowUseCase
import com.tekinumut.cuyemekhane.todaymenu.events.TodayMenuEvent
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TodayMenuUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
    private val todayMenuMapper: TodayMenuMapper,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, TodayMenuEvent>(dispatcher) {

    override suspend fun getExecutable(params: Unit): Flow<TodayMenuEvent> {
        return flow {
            val mappedResponse = when (val response = menuRepository.getTodayMenu()) {
                is Resource.Success -> {
                    val todayMenuUIModel = todayMenuMapper.mapToUIModel(response.value)
                    if (todayMenuUIModel.foods.isNotEmpty()) {
                        TodayMenuEvent.Success(todayMenuUIModel)
                    } else {
                        TodayMenuEvent.EmptyList
                    }
                }
                is Resource.Failure -> TodayMenuEvent.Failure(response.cuError)
            }
            emit(mappedResponse)
        }
    }
}