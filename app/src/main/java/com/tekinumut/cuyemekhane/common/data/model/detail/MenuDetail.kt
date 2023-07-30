package com.tekinumut.cuyemekhane.common.data.model.detail

import com.tekinumut.cuyemekhane.common.data.mappers.ResponseModel

data class MenuDetail(
    val imageUrl: String?,
    val ingredients: List<String>?
) : ResponseModel