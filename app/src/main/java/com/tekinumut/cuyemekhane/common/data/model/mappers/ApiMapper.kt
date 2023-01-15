package com.tekinumut.cuyemekhane.common.data.model.mappers

/**
 * Created by Umut Tekin on 15.01.2023.
 */
interface ApiMapper<R : ResponseModel, U : UIModel> {

    fun mapToUIModel(responseModel: R?): U
}

interface ResponseModel

interface UIModel