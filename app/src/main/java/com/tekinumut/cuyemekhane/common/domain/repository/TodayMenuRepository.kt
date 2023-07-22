package com.tekinumut.cuyemekhane.common.domain.repository

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.model.todayfood.TodayMenu

interface TodayMenuRepository {

    suspend fun getTodayMenu(): Resource<TodayMenu>
}