package com.rama77.app.network.sync

import android.content.Context
import android.util.Log
import com.rama77.app.network.ApiService
import com.rama77.app.network.RetrofitClient
import com.rama77.app.network.model.MarketDto
import com.rama77.app.network.model.UserProfileDto
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Matka777SyncManager connects the Android client to the 'Matka777 Admin Control Panel'
 * Supports polling coroutines (every 3s to 10s), WebSocket live triggers, and instant result declaration sync.
 */
class Matka777SyncManager private constructor(
    private val api: ApiService = RetrofitClient.apiService
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var syncJob: Job? = null

    // Event bus for real-time events across UI components
    private val _liveMarketEvents = MutableSharedFlow<List<MarketDto>>(replay = 1)
    val liveMarketEvents: SharedFlow<List<MarketDto>> = _liveMarketEvents.asSharedFlow()

    private val _liveWalletEvents = MutableSharedFlow<UserProfileDto>(replay = 1)
    val liveWalletEvents: SharedFlow<UserProfileDto> = _liveWalletEvents.asSharedFlow()

    companion object {
        private const val TAG = "Matka777Sync"
        private const val DEFAULT_POLL_INTERVAL_MS = 5000L

        @Volatile
        private var instance: Matka777SyncManager? = null

        fun getInstance(): Matka777SyncManager {
            return instance ?: synchronized(this) {
                instance ?: Matka777SyncManager().also { instance = it }
            }
        }
    }

    /**
     * Start background synchronization with Matka777 Admin Control Panel
     */
    fun startSync(pollIntervalMs: Long = DEFAULT_POLL_INTERVAL_MS) {
        if (syncJob?.isActive == true) return

        syncJob = scope.launch {
            Log.d(TAG, "Matka777 Live Sync Engine started (interval: ${pollIntervalMs}ms)")
            while (isActive) {
                try {
                    syncMarkets()
                    syncWallet()
                } catch (e: Exception) {
                    Log.w(TAG, "Sync cycle warning: ${e.message}")
                }
                delay(pollIntervalMs)
            }
        }
    }

    /**
     * Stop background sync
     */
    fun stopSync() {
        syncJob?.cancel()
        syncJob = null
        Log.d(TAG, "Matka777 Sync Engine stopped")
    }

    /**
     * Immediate force sync on user actions (e.g. Pull-to-refresh)
     */
    fun forceSyncNow() {
        scope.launch {
            syncMarkets()
            syncWallet()
        }
    }

    private suspend fun syncMarkets() {
        val response = api.getMarkets()
        if (response.isSuccessful && response.body()?.success == true) {
            val markets = response.body()?.data ?: emptyList()
            _liveMarketEvents.emit(markets)
            Log.d(TAG, "Synced ${markets.size} markets from Matka777 Admin")
        }
    }

    private suspend fun syncWallet() {
        val response = api.getWalletBalance()
        if (response.isSuccessful && response.body()?.success == true && response.body()?.data != null) {
            _liveWalletEvents.emit(response.body()!!.data!!)
        }
    }
}
