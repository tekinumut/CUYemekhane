package com.tekinumut.cuyemekhane.common.data.api

import com.tekinumut.cuyemekhane.common.util.Constants
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Umut Tekin on 15.01.2023.
 */
interface ApiService {

    @GET(Constants.NETWORK.MAIN_PAGE)
    suspend fun getMainPage(): Response<String>
}