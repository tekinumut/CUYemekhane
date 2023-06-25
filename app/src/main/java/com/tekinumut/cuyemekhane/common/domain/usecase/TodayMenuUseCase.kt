package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.common.data.model.mappers.MainPageMapper
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.repository.MainPageRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.FlowUseCase
import com.tekinumut.cuyemekhane.todaymenu.events.TodayMenuEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Umut Tekin on 15.01.2023.
 */
class TodayMenuUseCase @Inject constructor(
    private val mainPageRepository: MainPageRepository,
    private val mainPageMapper: MainPageMapper,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, TodayMenuEvent>(dispatcher) {

    override suspend fun getExecutable(params: Unit): Flow<TodayMenuEvent> {
        return flow {
            val mappedResponse = when (val response = mainPageRepository.getMainPage()) {
                is Resource.Success -> {
                    val mainPageUIModel = mainPageMapper.mapToUIModel(response.value)
                    if (mainPageUIModel.todayMenu.foods.isNotEmpty()) {
                        TodayMenuEvent.Success(mainPageUIModel.todayMenu)
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