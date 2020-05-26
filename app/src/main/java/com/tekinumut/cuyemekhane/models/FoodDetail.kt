package com.tekinumut.cuyemekhane.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Food::class,
        parentColumns = arrayOf("food_id"),
        childColumns = arrayOf("detail_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class FoodDetail(
    // Değerini yemek modelinden alır. Aralarında 1-1 ilişki vardır
    @PrimaryKey val detail_id: Int,
    // Yemeğin fotoğrafı
    val picBase64: String
)