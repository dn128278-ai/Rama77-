package com.rama77.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rama77.app.network.model.MarketDto
import com.rama77.app.repository.RamaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MarketUiState {
    object Loading : MarketUiState()
    data class Success(val markets: List<MarketDto>) : MarketUiState()
    data class Error(val message: String) : MarketUiState()
}

class MarketViewModel(private val repository: RamaRepository = RamaRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow<MarketUiState>(MarketUiState.Loading)
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()

    init {
        fetchMarkets()
    }

    fun fetchMarkets() {
        viewModelScope.launch {
            _uiState.value = MarketUiState.Loading
            repository.getMarketsFlow().collect { result ->
                result.fold(
                    onSuccess = { list ->
                        _uiState.value = MarketUiState.Success(list)
                    },
                    onFailure = { error ->
                        _uiState.value = MarketUiState.Error(error.localizedMessage ?: "Unknown Error")
                    }
                )
            }
        }
    }
}