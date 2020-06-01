package com.tekinumut.cuyemekhane.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = FoodDetail::class,
        parentColumns = arrayOf("detail_id"),
        childColumns = arrayOf("detailCreatorId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class FoodComponent(
    @PrimaryKey val comp_id: Int? = null,
    val name: String,
    @ColumnInfo(index = true)
    val detailCreatorId: Int
)