package com.tekinumut.cuyemekhane.feature.pricing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.data.api.response.Resource
import com.tekinumut.cuyemekhane.common.domain.usecase.PricingUseCase
import com.tekinumut.cuyemekhane.common.ui.CommonErrorModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PricingViewModel @Inject constructor(
    private val pricingUseCase: PricingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

    init {
        getPricingInfo()
    }

    fun getPricingInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = pricingUseCase(Unit)) {
                is Resource.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            state = State.NoPricingDataFound
                        )
                    }
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            state = State.PricingDataFetched(response.value)
                        )
                    }
                }
            }
        }
    }

    val noPricingDataErrorModel = CommonErrorModel(
        drawableId = R.drawable.ic_error_info,
        description = R.string.pricing_html_data_no_found,
        positiveButtonText = R.string.update_list
    )

    data class UIState(
        val isLoading: Boolean = false,
        val state: State = State.Initial
    )

    sealed interface State {
        data object Initial : State
        data class PricingDataFetched(val htmlSource: String) : State
        data object NoPricingDataFound : State
    }
}