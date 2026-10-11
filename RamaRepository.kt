package com.rama77.app.repository

import com.rama77.app.network.ApiService
import com.rama77.app.network.RetrofitClient
import com.rama77.app.network.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RamaRepository(private val api: ApiService = RetrofitClient.apiService) {

    // Flow for real-time market updates
    fun getMarketsFlow(): Flow<Result<List<MarketDto>>> = flow {
        try {
            val response = api.getMarkets()
            if (response.isSuccessful && response.body()?.success == true) {
                emit(Result.success(response.body()?.data ?: emptyList()))
            } else {
                emit(Result.failure(Exception(response.body()?.message ?: "Failed to fetch markets")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    // Flow for user wallet
    fun getWalletFlow(): Flow<Result<UserProfileDto>> = flow {
        try {
            val response = api.getWalletBalance()
            if (response.isSuccessful && response.body()?.success == true && response.body()?.data != null) {
                emit(Result.success(response.body()!!.data!!))
            } else {
                emit(Result.failure(Exception(response.body()?.message ?: "Failed to fetch wallet")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    // Place bet API call
    suspend fun placeBid(request: PlaceBidRequest): Result<PlaceBidResponse> {
        return try {
            val response = api.placeBid(request)
            if (response.isSuccessful && response.body()?.success == true && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to place bid"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Remote admin config flow
    fun getAdminConfigFlow(): Flow<Result<AdminConfigDto>> = flow {
        try {
            val response = api.getAdminConfig()
            if (response.isSuccessful && response.body()?.data != null) {
                emit(Result.success(response.body()!!.data!!))
            } else {
                emit(Result.failure(Exception("Failed to fetch admin config")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}