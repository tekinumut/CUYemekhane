package com.tekinumut.cuyemekhane.common.domain.usecase.events

import com.tekinumut.cuyemekhane.common.data.api.response.CuError
import com.tekinumut.cuyemekhane.common.domain.model.announcements.AnnouncementsUIModel

sealed interface AnnouncementsEvent {
    data class Success(
        val announcements: List<AnnouncementsUIModel>
    ) : AnnouncementsEvent

    data class Failure(val exception: CuError) : AnnouncementsEvent
    data object NoAnnouncement : AnnouncementsEvent
}