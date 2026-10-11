package com.rama77.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rama77.app.network.model.PlaceBidRequest
import com.rama77.app.network.model.PlaceBidResponse
import com.rama77.app.repository.RamaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WalletViewModel(private val repository: RamaRepository = RamaRepository()) : ViewModel() {

    private val _balance = MutableStateFlow<Double>(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _bidResult = MutableStateFlow<Result<PlaceBidResponse>?>(null)
    val bidResult: StateFlow<Result<PlaceBidResponse>?> = _bidResult.asStateFlow()

    init {
        refreshWallet()
    }

    fun refreshWallet() {
        viewModelScope.launch {
            repository.getWalletFlow().collect { result ->
                result.onSuccess { profile ->
                    _balance.value = profile.balance
                }
            }
        }
    }

    fun submitBids(request: PlaceBidRequest) {
        viewModelScope.launch {
            val result = repository.placeBid(request)
            _bidResult.value = result
            result.onSuccess { response ->
                _balance.value = response.remainingBalance
            }
        }
    }
}