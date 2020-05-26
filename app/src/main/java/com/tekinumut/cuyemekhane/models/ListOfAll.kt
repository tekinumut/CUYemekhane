package com.tekinumut.cuyemekhane.models

data class ListOfAll(
    val foodDate:List<FoodDate>,
    val food:List<Food>,
    val foodDetail:List<FoodDetail>,
    val foodComponent:List<FoodComponent>
)