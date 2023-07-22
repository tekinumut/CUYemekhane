package com.tekinumut.cuyemekhane.common.domain.repository

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.MonthlyMenu

interface MonthlyMenuRepository {

    suspend fun getMonthlyMenu(): Resource<MonthlyMenu>
}