package com.tekinumut.cuyemekhane.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = FoodDate::class,
        parentColumns = arrayOf("date_id"),
        childColumns = arrayOf("dateCreatorId"),
        onDelete = ForeignKey.CASCADE
    )]
)
/**
 * Yemek modeli
 */
data class Food(
    @PrimaryKey val food_id: Int,
    // Yemeğin kategorisi
    val category: String? = null,
    // Yemeğin adı
    // Eğer yemek bilgisi yok ise, tüm metini alır
    val name: String,
    // Yemeğin kalorisi
    val calorie: String? = null,
    // Yemeğin detaylarının olduğu link
    val detailURL: String? = null,
    // Yemeğin ait olduğu tarih
    val dateCreatorId: Int
)