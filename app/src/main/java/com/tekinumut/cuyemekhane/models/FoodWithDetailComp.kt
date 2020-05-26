package com.tekinumut.cuyemekhane.models

import androidx.room.Embedded
import androidx.room.Relation

data class FoodWithDetailComp (
    @Embedded val yemek: Food,
    @Relation(
        entity = FoodDetail::class,
        parentColumn = "food_id",
        entityColumn = "detail_id"
    )
    val compoenentIntegratiWith: List<DetailWithComp>
)