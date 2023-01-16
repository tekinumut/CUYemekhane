package com.tekinumut.cuyemekhane.common.data.repository

import com.tekinumut.cuyemekhane.common.data.api.ApiService
import com.tekinumut.cuyemekhane.common.data.api.handleApiCall
import com.tekinumut.cuyemekhane.common.data.model.mainpage.DailyMenu
import com.tekinumut.cuyemekhane.common.data.model.mainpage.Food
import com.tekinumut.cuyemekhane.common.data.model.mainpage.MainPageResponse
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import com.tekinumut.cuyemekhane.common.domain.repository.MainPageRepository
import com.tekinumut.cuyemekhane.common.util.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
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
                val todayDate = parseDailyMenu(Jsoup.parse(response.value))
                Resource.Success(MainPageResponse(todayDate))
            }
        }
    }

    private fun parseDailyMenu(document: Document): DailyMenu {
        val date = document.select(Constants.QUERY.TODAY_DATE).text()
        val foodElements: Elements = document.select(Constants.QUERY.TODAY_FOODS_BASE)
        val categoryElements: Elements = document.select(Constants.QUERY.TODAY_FOOD_CATEGORY)
        val calorieElements: Elements = document.select(Constants.QUERY.TODAY_FOOD_CALORIE)

        val nameList: List<String> = foodElements.map {
            it.attr(Constants.ATTRIBUTE.FOOD_NAME_ATTR)
        }
        val calorieList: List<String> = calorieElements.map { it.ownText().orEmpty() }
        val categoryList: List<String> = categoryElements.map { it.text().orEmpty() }
        val imageList: List<String> = foodElements.map {
            Constants.NETWORK.BASE_ENDPOINT.plus(it.attr(Constants.ATTRIBUTE.FOOD_IMAGE_ATTR))
        }
        val foodList: List<Food> = nameList.mapIndexed { index, name ->
            Food(
                name = name,
                category = categoryList.getOrNull(index),
                calorie = calorieList.getOrNull(index),
                imageUrl = imageList.getOrNull(index)
            )
        }
        return DailyMenu(
            date = date,
            foodList = foodList
        )
    }
}