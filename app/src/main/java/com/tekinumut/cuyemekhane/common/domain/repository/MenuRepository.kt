package com.tekinumut.cuyemekhane.common.domain.repository

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.model.announcements.Announcements
import com.tekinumut.cuyemekhane.common.data.model.detail.MenuDetail
import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.MonthlyMenu
import com.tekinumut.cuyemekhane.common.data.model.todayfood.TodayMenu

interface MenuRepository {

    suspend fun getTodayMenu(): Resource<TodayMenu>

    suspend fun getMonthlyMenu(): Resource<MonthlyMenu>

    suspend fun getMenuDetail(href: String): Resource<MenuDetail>

    suspend fun getAnnouncements(): Resource<List<Announcements>>
}