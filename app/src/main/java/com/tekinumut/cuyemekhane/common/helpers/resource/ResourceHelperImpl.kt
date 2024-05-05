package com.tekinumut.cuyemekhane.common.helpers.resource

import android.app.Application
import javax.inject.Inject

class ResourceHelperImpl @Inject constructor(
    private val application: Application
) : ResourceHelper {

    override fun getString(resourceId: Int): String {
        return application.resources.getString(resourceId)
    }
}