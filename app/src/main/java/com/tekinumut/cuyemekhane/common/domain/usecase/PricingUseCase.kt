package com.tekinumut.cuyemekhane.common.domain.usecase

import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.di.IODispatcher
import com.tekinumut.cuyemekhane.common.domain.repository.PricingRepository
import com.tekinumut.cuyemekhane.common.domain.usecase.base.SingleUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class PricingUseCase @Inject constructor(
    private val pricingRepository: PricingRepository,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : SingleUseCase<Unit, String>(dispatcher) {

    override suspend fun getExecutable(params: Unit): Resource<String> {
        return when (val response = pricingRepository.getPricingHtmlSource()) {
            is Resource.Failure -> response
            is Resource.Success -> {
                if (response.value.isNotBlank()) {
                    response
                } else {
                    Resource.Failure()
                }
            }
        }
    }
}