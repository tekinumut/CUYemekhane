package com.tekinumut.cuyemekhane.common.data.repository

import com.tekinumut.cuyemekhane.common.data.api.ApiService
import com.tekinumut.cuyemekhane.common.data.api.handleApiCall
import com.tekinumut.cuyemekhane.common.data.model.mainpage.*
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import com.tekinumut.cuyemekhane.common.domain.repository.MainPageRepository
import com.tekinumut.cuyemekhane.common.extensions.withBaseUrl
import com.tekinumut.cuyemekhane.common.util.Constants
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
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
                val responseHtml = Jsoup.parse(response.value)
                val todayMenu = parseTodayMenu(responseHtml)
                val monthlyMenu = parseMonthlyMenu(responseHtml)
                Resource.Success(MainPageResponse(todayMenu, monthlyMenu))
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

    private fun parseMonthlyMenu(document: Document): MonthlyMenu {
        val dateElements: Elements = document.select(Constants.QUERY.MONTHLY_DATE)
        val monthlyFoodsElements: Elements = document.select(Constants.QUERY.MONTHLY_FOODS)

        val dateList = dateElements.map { it.text().orEmpty() }

        val dailyMenuList: List<DailyMenu> = dateList.mapIndexed { index, date ->
            val dailyFoodElements = monthlyFoodsElements.getOrNull(index)?.select(
                Constants.QUERY.DAILY_FOODS_SELECTOR
            )
            val foodList = dailyFoodElements?.map { dailyFoodElement ->
                val (name, calorie) = separateNameAndCalorie(dailyFoodElement)
                val imageUrl = dailyFoodElement.attr(Constants.ATTRIBUTE.HREF).withBaseUrl()
                DailyFood(
                    name = name,
                    calorie = calorie,
                    imageUrl = imageUrl
                )
            }
            DailyMenu(
                date = date,
                foodList = foodList
            )
        }
        return MonthlyMenu(dailyMenuList = dailyMenuList)
    }

    /**
     * Separates the food name and calorie from fetched html
     * The expected text: Kıymalı Sandviç 600 Kalori
     * Separate as name = Kıymalı Sandviç and calorie = 600
     * @param food DailyFood
     */
    private fun separateNameAndCalorie(food: Element): Pair<String, String> {
        // Turn text to -> Kıymalı Sandviç$$$600 Kalori
        val replacedHtml = food.html().replace("<br>", "$$$")
        // Turn text to -> Kıymalı Sandviç<br>600 Kalori
        val newHtml = Jsoup.parse(replacedHtml).text().replace("$$$", "<br>")
        val name = newHtml.substringBefore("<br>")
        val calorie = newHtml.substringAfter("<br>").filter { it.isDigit() }
        return Pair(name, calorie)
    }
}















