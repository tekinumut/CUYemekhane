package com.tekinumut.cuyemekhane.common.domain.repository

import com.tekinumut.cuyemekhane.common.data.model.mainpage.MainPageResponse
import com.tekinumut.cuyemekhane.common.data.model.response.Resource

/**
 * Created by Umut Tekin on 15.01.2023.
 */
interface MainPageRepository {

    suspend fun getMainPage(): Resource<MainPageResponse>
}