package com.tekinumut.cuyemekhane.common.util

object Constants {

    const val DEFAULT_INT = -1
    const val INT_ZERO = 0
    const val DOUBLE_ZERO = 0.0
    const val INT_ONE = 1

    const val STRING_EMPTY = ""

    object NETWORK {
        const val BASE_ENDPOINT = "https://yemekhane.cu.edu.tr/"
        const val MAIN_PAGE = BASE_ENDPOINT + "default.asp"
    }

    object QUERY {
        const val TODAY_DATE = "#gunluk_yemek h1"
        const val TODAY_FOODS_INFO = "#gunluk_yemek ul"
        const val TODAY_FOODS = "#gunluk_yemek ul li"
        const val TODAY_FOOD_CATEGORY = "$TODAY_FOODS *>span.label.label-danger"
        const val TODAY_FOOD_CALORIE = "$TODAY_FOODS font"

        const val MONTHLY_DATE = ".call-to-action font:eq(1)"
        const val MONTHLY_FOODS = ".call-to-action ul"

        const val DAILY_FOODS_SELECTOR = "li a"
        const val SELECTOR_HREF = "a[href]"
        const val SELECTOR_IMG = "img"

        const val ANNOUNCEMENTS = "section#duyurular .testimonial-item"
        const val ANNOUNCEMENT_TITLE = "p"
        const val ANNOUNCEMENT_DESCRIPTION = ">span"
    }

    object ATTRIBUTE {
        const val HREF = "href"
        const val SRC = "src"
        const val FOOD_NAME_ATTR = "data-title"
        const val TODAY_FOOD_IMAGE_ATTR = "data-thumb"
    }

    object URL {
        const val DEFAULT_ANNOUNCEMENT_LOGO = "${NETWORK.BASE_ENDPOINT}images/simge2.png"
    }
}