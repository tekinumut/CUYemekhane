package com.tekinumut.cuyemekhane.common.util

/**
 * Created by Umut Tekin on 15.01.2023.
 */
object Constants {

    const val DEFAULT_INT = -1
    const val INT_ZERO = 0
    const val DOUBLE_ZERO = 0.0

    object NETWORK {
        const val BASE_ENDPOINT = "https://yemekhane.cu.edu.tr/"
    }

    object QUERY {
        const val TODAY_DATE = "#gunluk_yemek h1"
        const val TODAY_FOODS_BASE = "#gunluk_yemek ul li"
        const val TODAY_FOOD_CATEGORY = "$TODAY_FOODS_BASE *>span.label.label-danger"
        const val TODAY_FOOD_CALORIE = "$TODAY_FOODS_BASE font"
    }

    object ATTRIBUTE {
        const val FOOD_NAME_ATTR = "data-title"
        const val FOOD_IMAGE_ATTR = "data-thumb"
    }
}