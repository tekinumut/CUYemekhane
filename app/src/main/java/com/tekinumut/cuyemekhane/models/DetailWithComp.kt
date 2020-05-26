package com.tekinumut.cuyemekhane.models

import androidx.room.Embedded
import androidx.room.Relation

data class DetailWithComp(
    @Embedded val foodDetail: FoodDetail,
    @Relation(
        parentColumn = "detail_id",
        entityColumn = "detailCreatorId"
    )
    val foodComponent: List<FoodComponent>
)