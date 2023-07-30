package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.data.mappers.MenuDetailMapper
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.detail.MenuDetailUIModel
import com.tekinumut.cuyemekhane.common.domain.repository.MenuRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.SingleUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class MenuDetailUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
    private val menuDetailMapper: MenuDetailMapper,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : SingleUseCase<MenuDetailUseCase.Param, MenuDetailUIModel>(dispatcher) {

    override suspend fun getExecutable(params: Param): Resource<MenuDetailUIModel> {
        return when (val response = menuRepository.getMenuDetail(params.href)) {
            is Resource.Success -> {
                val menuDetailUIModel = menuDetailMapper.mapToUIModel(response.value)
                Resource.Success(menuDetailUIModel)
            }
            is Resource.Failure -> Resource.Failure(response.cuError)
        }
    }

    data class Param(
        val href: String
    )
}