package com.tekinumut.cuyemekhane.common.data.mappers

import com.tekinumut.cuyemekhane.common.data.model.detail.MenuDetail
import com.tekinumut.cuyemekhane.common.domain.model.monthlydetail.MenuDetailUIModel
import javax.inject.Inject

class MenuDetailMapper @Inject constructor(
) : ApiMapper<MenuDetail, MenuDetailUIModel> {
    override fun mapToUIModel(responseModel: MenuDetail?) = MenuDetailUIModel(
        imageUrl = responseModel?.imageUrl.orEmpty(),
        ingredients = responseModel?.ingredients.orEmpty()
    )
}