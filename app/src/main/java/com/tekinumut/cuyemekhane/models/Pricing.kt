package com.tekinumut.cuyemekhane.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pricing(
    @PrimaryKey val pricing_id: Int = 1,
    val html: String? = ""
)
