package com.tekinumut.cuyemekhane.common.domain.model.announcements

import com.tekinumut.cuyemekhane.common.data.mappers.UIModel
import com.tekinumut.cuyemekhane.common.util.Constants

data class AnnouncementsUIModel(
    val title: String,
    val description: String,
    val descriptionUrl: String = Constants.STRING_EMPTY,
    val logoImageUrl: String = Constants.STRING_EMPTY
) : UIModel