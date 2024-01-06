package com.tekinumut.cuyemekhane.common.domain.usecase.events

import com.tekinumut.cuyemekhane.common.domain.model.announcements.AnnouncementsUIModel

sealed interface AnnouncementsEvent {
    data class Success(
        val announcements: List<AnnouncementsUIModel>
    ) : AnnouncementsEvent

    data class NoAnnouncement(
        val announcements: List<AnnouncementsUIModel>
    ) : AnnouncementsEvent
}