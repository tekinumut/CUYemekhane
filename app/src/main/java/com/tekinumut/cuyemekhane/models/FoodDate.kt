package com.tekinumut.cuyemekhane.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Aylık yemek listesine ait tarihleri tutar.
 */
@Entity
data class FoodDate(
    // Relationship için gerekli id
    @PrimaryKey val date_id: Int,
    // Tarih içeriği  Örnek: 20.05.2020 Çarşamba
    val name: String,
    // Aylık liste ekranında mevcut gösterilecek olan gün
    // Günlük listede kullanım alanı yok
    val selectedDay: Int? = 0
)