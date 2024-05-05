package com.tekinumut.cuyemekhane.common.data.api

import com.tekinumut.cuyemekhane.common.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET(Constants.NETWORK.MAIN_PAGE)
    suspend fun getMainPage(): Response<String>

    @GET
    suspend fun getImageOfMenu(@Url href: String): Response<String>

    @GET(Constants.NETWORK.PRICING)
    suspend fun getPricing(): Response<String>
}