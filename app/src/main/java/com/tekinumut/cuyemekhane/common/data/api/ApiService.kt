package com.tekinumut.cuyemekhane.common.data.api

import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Umut Tekin on 15.01.2023.
 */
interface ApiService {

    @GET("default.asp")
    suspend fun getMainPage(): Response<String>
}