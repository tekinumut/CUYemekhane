package com.tekinumut.cuyemekhane.common.data.repository

import com.tekinumut.cuyemekhane.common.data.api.ApiService
import com.tekinumut.cuyemekhane.common.data.api.handleApiCall
import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.model.detail.MenuDetail
import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.DailyFood
import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.DailyMenu
import com.tekinumut.cuyemekhane.common.data.model.monthlyfood.MonthlyMenu
import com.tekinumut.cuyemekhane.common.data.model.todayfood.TodayFood
import com.tekinumut.cuyemekhane.common.data.model.todayfood.TodayMenu
import com.tekinumut.cuyemekhane.common.domain.repository.MenuRepository
import com.tekinumut.cuyemekhane.common.extensions.toZeroOrNull
import com.tekinumut.cuyemekhane.common.extensions.withBaseUrl
import com.tekinumut.cuyemekhane.common.util.Constants
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class MenuRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MenuRepository {

    override suspend fun getTodayMenu(): Resource<TodayMenu> {
        return when (val response = handleApiCall { apiService.getMainPage() }) {
            is Resource.Failure -> response
            is Resource.Success -> {
                val responseHtml = Jsoup.parse(response.value)
                Resource.Success(parseTodayMenu(responseHtml))
            }
        }
    }

    override suspend fun getMonthlyMenu(): Resource<MonthlyMenu> {
        return when (val response = handleApiCall { apiService.getMainPage() }) {
            is Resource.Failure -> response
            is Resource.Success -> {
                val responseHtml = Jsoup.parse(response.value)
                Resource.Success(parseMonthlyMenu(responseHtml))
            }
        }
    }

    override suspend fun getMenuDetail(href: String): Resource<MenuDetail> {
        return when (val response = handleApiCall { apiService.getImageOfMenu(href) }) {
            is Resource.Failure -> response
            is Resource.Success -> {
                val responseHtml = Jsoup.parse(response.value)
                Resource.Success(parseMenuDetail(responseHtml))
            }
        }
    }

    private fun parseMenuDetail(document: Document): MenuDetail {
        val imageElement: Element? = document.selectFirst("img")
        val imageUrl = imageElement?.attr("src")?.withBaseUrl()
        val ingredients: List<String> = document.select("td").map {
            it.text().toString()
        }
        return MenuDetail(
            imageUrl = imageUrl,
            ingredients = ingredients
        )
    }

    private fun parseTodayMenu(document: Document): TodayMenu {
        val date: String = document.select(Constants.QUERY.TODAY_DATE).text()
        val todayFoodInfoElements: Elements = document.select(Constants.QUERY.TODAY_FOODS_INFO)
        val todayFoodElements: Elements = document.select(Constants.QUERY.TODAY_FOODS)
        val categoryElements: Elements = document.select(Constants.QUERY.TODAY_FOOD_CATEGORY)
        val calorieElements: Elements = document.select(Constants.QUERY.TODAY_FOOD_CALORIE)

        val nameList: List<String> = todayFoodElements.map {
            it.attr(Constants.ATTRIBUTE.FOOD_NAME_ATTR)
        }.ifEmpty {
            todayFoodInfoElements.mapNotNull { it.text().ifEmpty { null } }
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

    private suspend fun parseMonthlyMenu(document: Document): MonthlyMenu {
        val dateElements: Elements = document.select(Constants.QUERY.MONTHLY_DATE)
        val monthlyFoodsElements: Elements = document.select(Constants.QUERY.MONTHLY_FOODS)

        val dateList = dateElements.map { it.text().orEmpty() }
        val imageUrlList = getImageUrlList(monthlyFoodsElements)
        val dailyMenuList: List<DailyMenu> = dateList.mapIndexed { index, date ->
            val dailyFoodElements = monthlyFoodsElements.getOrNull(index)?.select(
                Constants.QUERY.DAILY_FOODS_SELECTOR
            )
            val foodList = dailyFoodElements?.map { dailyFoodElement ->
                val (name, calorie) = separateNameAndCalorie(dailyFoodElement)
                val detailUrl = dailyFoodElement.attr("href")
                DailyFood(
                    name = name,
                    calorie = calorie,
                    detailUrl = detailUrl,
                    imageUrl = imageUrlList.getOrNull(index)
                )
            }
            DailyMenu(
                date = date,
                totalCalorie = foodList?.sumOf { it.calorie.toZeroOrNull() },
                foodList = foodList
            )
        }.filter { !it.foodList.isNullOrEmpty() }
        return MonthlyMenu(dailyMenuList = dailyMenuList)
    }

    private suspend fun getImageUrlList(
        monthlyFoodsElements: Elements
    ): List<String?> = coroutineScope {
        val dailyFoodElements = monthlyFoodsElements.map {
            it.select(Constants.QUERY.DAILY_FOODS_SELECTOR)
        }
        dailyFoodElements.map {
            async {
                val menuDetail = getMenuDetail(href = it.attr("href"))
                (menuDetail as? Resource.Success)?.value?.imageUrl
            }
        }.awaitAll()
    }

    /**
     * Separates the food name and calorie from fetched html
     * The expected text: Kıymalı Sandviç 600 Kalori
     * Separate as name = Kıymalı Sandviç and calorie = 600
     * @param food DailyFood
     */
    private fun separateNameAndCalorie(food: Element): Pair<String, String> {
        val html = food.html()
        val name = html.substringBefore("<br>")
        val calorie = html.substringAfter("<br>").filter { it.isDigit() }
        return Pair(name, calorie)
    }
}