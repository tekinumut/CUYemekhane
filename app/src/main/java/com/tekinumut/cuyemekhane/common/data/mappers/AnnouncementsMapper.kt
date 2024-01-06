package com.tekinumut.cuyemekhane.common.data.mappers

import com.tekinumut.cuyemekhane.common.data.model.announcements.Announcements
import com.tekinumut.cuyemekhane.common.domain.model.announcements.AnnouncementsUIModel
import com.tekinumut.cuyemekhane.common.extensions.ifEmptyTo
import com.tekinumut.cuyemekhane.common.util.Constants
import javax.inject.Inject

class AnnouncementsMapper @Inject constructor(
) : ApiMapper<Announcements, AnnouncementsUIModel> {
    override fun mapToUIModel(responseModel: Announcements?): AnnouncementsUIModel {
        return AnnouncementsUIModel(
            title = responseModel?.title.orEmpty(),
            description = responseModel?.description.orEmpty(),
            descriptionUrl = responseModel?.descriptionUrl.orEmpty(),
            logoImageUrl = responseModel?.logoImageUrl.ifEmptyTo(
                Constants.URL.DEFAULT_ANNOUNCEMENT_LOGO
            )
        )
    }
}