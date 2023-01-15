package com.tekinumut.cuyemekhane.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekinumut.cuyemekhane.common.data.model.response.Resource
import com.tekinumut.cuyemekhane.common.domain.usecase.MainPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Umut Tekin on 15.01.2023.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainPageUseCase: MainPageUseCase
) : ViewModel() {

    private val _mainPageEvent = MutableStateFlow<MainPageEvent>(MainPageEvent.Default)
    val mainPageEvent: StateFlow<MainPageEvent> = _mainPageEvent

    fun getMainPage() {
        viewModelScope.launch {
            mainPageUseCase(Unit).collect { resource ->
                when (resource) {
                    Resource.Loading -> _mainPageEvent.value = MainPageEvent.Loading
                    is Resource.Failure -> {
                        _mainPageEvent.value = MainPageEvent.Failure(resource.cuError)
                    }
                    is Resource.Success -> {
                        _mainPageEvent.value = MainPageEvent.Success(resource.value)
                    }
                }
            }
        }
    }
}