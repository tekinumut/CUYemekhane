package com.tekinumut.cuyemekhane.common.domain.model.announcements

import com.tekinumut.cuyemekhane.common.data.mappers.UIModel

data class AnnouncementsUIModel(
    val title: String,
    val description: String,
    val descriptionUrl: String,
    val logoImageUrl: String
) : UIModel