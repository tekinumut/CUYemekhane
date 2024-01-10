package com.tekinumut.cuyemekhane.common.data.repository

import android.util.Base64
import com.tekinumut.cuyemekhane.common.data.api.ApiService
import com.tekinumut.cuyemekhane.common.data.api.handleApiCall
import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.domain.repository.PricingRepository
import com.tekinumut.cuyemekhane.common.util.Constants
import javax.inject.Inject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class PricingRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PricingRepository {

    override suspend fun getPricingHtmlSource(): Resource<String> {
        return when (val response = handleApiCall { apiService.getPricing() }) {
            is Resource.Failure -> response
            is Resource.Success -> {
                val responseHtml = Jsoup.parse(response.value)
                Resource.Success(parsePricingHtmlSource(responseHtml))
            }
        }
    }

    private fun parsePricingHtmlSource(document: Document): String {
        val pricingSection = document.select(Constants.QUERY.PRICING_BASE_SECTION)
        val htmlSource = pricingSection.outerHtml()
        return Base64.encodeToString(htmlSource.toByteArray(), Base64.NO_PADDING)
    }
}