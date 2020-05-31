package com.tekinumut.cuyemekhane.models

import androidx.room.Embedded
import androidx.room.Relation

data class DateWithFoodDetailComp(
    @Embedded val foodDate: FoodDate,
    @Relation(
        entity = Food::class,
        parentColumn = "date_id",
        entityColumn = "dateCreatorId"
    )
    val yemekWithComponentComp: List<FoodWithDetailComp>?
)