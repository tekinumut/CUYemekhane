package com.tekinumut.cuyemekhane.common.data.repository

import com.tekinumut.cuyemekhane.common.data.api.ApiService
import com.tekinumut.cuyemekhane.common.data.api.handleApiCall
import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.DailyFood
import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.DailyMenu
import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.MonthlyMenu
import com.tekinumut.cuyemekhane.common.domain.repository.MonthlyMenuRepository
import com.tekinumut.cuyemekhane.common.extensions.withBaseUrl
import com.tekinumut.cuyemekhane.common.util.Constants
import javax.inject.Inject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class MonthlyMenuRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MonthlyMenuRepository {

    override suspend fun getMonthlyMenu(): Resource<MonthlyMenu> {
        return when (val response = handleApiCall { apiService.getMainPage() }) {
            is Resource.Failure -> response
            is Resource.Success -> {
                val responseHtml = Jsoup.parse(response.value)
                Resource.Success(parseMonthlyMenu(responseHtml))
            }
        }
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