package com.tekinumut.cuyemekhane.common.util

/**
 * Created by Umut Tekin on 15.01.2023.
 */
object Constants {

    const val DEFAULT_INT = -1
    const val INT_ZERO = 0
    const val DOUBLE_ZERO = 0.0

    const val STRING_EMPTY = ""

    object NETWORK {
        const val BASE_ENDPOINT = "https://yemekhane.cu.edu.tr/"
        const val MAIN_PAGE = BASE_ENDPOINT + "default.asp"
    }

    object QUERY {
        const val TODAY_DATE = "#gunluk_yemek h1"
        const val TODAY_FOODS = "#gunluk_yemek ul li"
        const val TODAY_FOOD_CATEGORY = "$TODAY_FOODS *>span.label.label-danger"
        const val TODAY_FOOD_CALORIE = "$TODAY_FOODS font"

        const val MONTHLY_DATE = ".call-to-action font:eq(1)"
        const val MONTHLY_FOODS = ".call-to-action ul"

        const val DAILY_FOODS_SELECTOR = "li a"
    }

    object ATTRIBUTE {
        const val FOOD_NAME_ATTR = "data-title"
        const val TODAY_FOOD_IMAGE_ATTR = "data-thumb"
        const val HREF = "href"
    }
}