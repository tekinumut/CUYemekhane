package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.mappers.AnnouncementsMapper
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.model.announcements.AnnouncementsUIModel
import com.tekinumut.cuyemekhane.common.domain.repository.MenuRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.FlowUseCase
import com.tekinumut.cuyemekhane.common.domain.usecase.events.AnnouncementsEvent
import com.tekinumut.cuyemekhane.common.helpers.resource.ResourceHelper
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnnouncementsUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
    private val announcementsMapper: AnnouncementsMapper,
    resourceHelper: ResourceHelper,
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
                        AnnouncementsEvent.NoAnnouncement(emptyAnnouncementList)
                    }
                }
                is Resource.Failure -> AnnouncementsEvent.NoAnnouncement(emptyAnnouncementList)
            }
            emit(mappedResponse)
        }
    }

    private val emptyAnnouncementList: List<AnnouncementsUIModel> = listOf(
        AnnouncementsUIModel(
            title = resourceHelper.getString(R.string.no_announcements_found_title),
            description = resourceHelper.getString(R.string.no_announcements_found_description)
        )
    )
}