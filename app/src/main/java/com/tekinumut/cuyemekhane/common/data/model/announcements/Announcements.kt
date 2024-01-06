package com.tekinumut.cuyemekhane.common.data.model.announcements

import com.tekinumut.cuyemekhane.common.data.mappers.ResponseModel

data class Announcements(
    val title: String?,
    val description: String?,
    val descriptionUrl: String?,
    val logoImageUrl: String?
) : ResponseModel