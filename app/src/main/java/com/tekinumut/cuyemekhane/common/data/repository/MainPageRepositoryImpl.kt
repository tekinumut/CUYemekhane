package com.tekinumut.cuyemekhane.common.data.repository

import com.tekinumut.cuyemekhane.common.data.api.ApiService
import com.tekinumut.cuyemekhane.common.data.api.handleApiCall
import com.tekinumut.cuyemekhane.common.data.model.mainpage.MainPageResponse
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import com.tekinumut.cuyemekhane.common.domain.repository.MainPageRepository
import com.tekinumut.cuyemekhane.common.util.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

/**
 * Created by Umut Tekin on 15.01.2023.
 */
class MainPageRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MainPageRepository {

    override suspend fun getMainPage(): Resource<MainPageResponse> {
        return when (val response = handleApiCall { apiService.getMainPage() }) {
            is Resource.Loading -> response
            is Resource.Failure -> response
            is Resource.Success -> {
                val todayDate = getTodayDate(Jsoup.parse(response.value))
                Resource.Success(MainPageResponse(todayDate))
            }
        }
    }

    private fun getTodayDate(document: Document): String {
        return document.select(Constants.QUERY.TODAY_DATE).text()
    }
}