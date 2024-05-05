package com.tekinumut.cuyemekhane.common.domain.repository

import com.tekinumut.cuyemekhane.common.data.api.response.Resource

interface PricingRepository {

    suspend fun getPricingHtmlSource(): Resource<String>
}