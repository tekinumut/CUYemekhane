package com.tekinumut.cuyemekhane.common.domain.model.monthlydetail

import com.tekinumut.cuyemekhane.common.data.mappers.UIModel

data class MenuDetailUIModel(
    val imageUrl: String,
    val ingredients: List<String>
) : UIModel