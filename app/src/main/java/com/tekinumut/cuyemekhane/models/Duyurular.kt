package com.tekinumut.cuyemekhane.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Duyurular(
    @PrimaryKey
    val duyuru_id: Int? = null,
    val title: String,
    val content: String,
    val picBase64: String? = null
)