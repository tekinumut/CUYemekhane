package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.mappers.AnnouncementsMapper
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.model.announcements.AnnouncementsUIModel
import com.tekinumut.cuyemekhane.common.domain.repository.MenuRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.FlowUseCase
import com.tekinumut.cuyemekhane.common.domain.usecase.events.AnnouncementsEvent
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnnouncementsUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
    private val announcementsMapper: AnnouncementsMapper,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, AnnouncementsEvent>(dispatcher) {

    override suspend fun getExecutable(params: Unit): Flow<AnnouncementsEvent> {
        return flow {
            val mappedResponse = when (val response = menuRepository.getAnnouncements()) {
                is Resource.Success -> {
                    val announcements: List<AnnouncementsUIModel> = response.value.map {
                        announcementsMapper.mapToUIModel(it)
                    }
                    if (announcements.isNotEmpty()) {
                        AnnouncementsEvent.Success(announcements)
                    } else {
                        AnnouncementsEvent.NoAnnouncement
                    }
                }
                is Resource.Failure -> AnnouncementsEvent.Failure(response.cuError)
            }
            emit(mappedResponse)
        }
    }
}