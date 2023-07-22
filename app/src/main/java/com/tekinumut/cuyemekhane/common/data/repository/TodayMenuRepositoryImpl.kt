package com.tekinumut.cuyemekhane.common.data.repository

import com.tekinumut.cuyemekhane.common.data.api.ApiService
import com.tekinumut.cuyemekhane.common.data.api.handleApiCall
import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.model.todayfood.TodayFood
import com.tekinumut.cuyemekhane.common.data.model.todayfood.TodayMenu
import com.tekinumut.cuyemekhane.common.domain.repository.TodayMenuRepository
import com.tekinumut.cuyemekhane.common.extensions.withBaseUrl
import com.tekinumut.cuyemekhane.common.util.Constants
import javax.inject.Inject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class TodayMenuRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TodayMenuRepository {

    override suspend fun getTodayMenu(): Resource<TodayMenu> {
        return when (val response = handleApiCall { apiService.getMainPage() }) {
            is Resource.Failure -> response
            is Resource.Success -> {
                val responseHtml = Jsoup.parse(response.value)
                Resource.Success(parseTodayMenu(responseHtml))
            }
        }
    }

    private fun parseTodayMenu(document: Document): TodayMenu {
        val date: String = document.select(Constants.QUERY.TODAY_DATE).text()
        val todayFoodElements: Elements = document.select(Constants.QUERY.TODAY_FOODS)
        val categoryElements: Elements = document.select(Constants.QUERY.TODAY_FOOD_CATEGORY)
        val calorieElements: Elements = document.select(Constants.QUERY.TODAY_FOOD_CALORIE)

        val nameList: List<String> = todayFoodElements.map {
            it.attr(Constants.ATTRIBUTE.FOOD_NAME_ATTR)
        }
        val calorieList: List<String> = calorieElements.map { it.ownText().orEmpty() }
        val categoryList: List<String> = categoryElements.map { it.text().orEmpty() }
        val imageList: List<String> = todayFoodElements.map {
            it.attr(Constants.ATTRIBUTE.TODAY_FOOD_IMAGE_ATTR).withBaseUrl()
        }
        val foods: List<TodayFood> = nameList.mapIndexed { index, name ->
            TodayFood(
                name = name,
                category = categoryList.getOrNull(index),
                calorie = calorieList.getOrNull(index),
                imageUrl = imageList.getOrNull(index)
            )
        }
        return TodayMenu(
            date = date,
            foods = foods
        )
    }
}