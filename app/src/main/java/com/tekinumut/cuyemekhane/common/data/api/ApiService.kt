package com.tekinumut.cuyemekhane.common.data.api

import com.tekinumut.cuyemekhane.common.util.Constants
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(Constants.NETWORK.MAIN_PAGE)
    suspend fun getMainPage(): Response<String>
}