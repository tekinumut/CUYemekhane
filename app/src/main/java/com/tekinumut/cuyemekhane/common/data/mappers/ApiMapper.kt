package com.tekinumut.cuyemekhane.common.data.mappers

interface ApiMapper<R : ResponseModel, U : UIModel> {

    fun mapToUIModel(responseModel: R?): U
}

interface ResponseModel

interface UIModel