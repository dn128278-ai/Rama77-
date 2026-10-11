package com.rama77.app.network.model

import com.google.gson.annotations.SerializedName

/**
 * Standard API Envelope matching Matka777 Backend Responses
 */
data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null
)

/**
 * Market status & results DTO matching Matka777 Admin Control Panel
 * Uses Gson 'alternate' parameter to seamlessly accept both Matka777 and legacy field names
 */
data class MarketDto(
    @SerializedName(value = "market_id", alternate = ["id"])
    val id: String,

    @SerializedName(value = "market_name", alternate = ["name"])
    val name: String,

    @SerializedName(value = "open_pana", alternate = ["open_panna", "openPana"])
    val openPana: String = "***",

    @SerializedName(value = "jodi", alternate = ["result_jodi", "jodi_digit"])
    val jodi: String = "**",

    @SerializedName(value = "close_pana", alternate = ["close_panna", "closePana"])
    val closePana: String = "***",

    @SerializedName(value = "open_time", alternate = ["openTime"])
    val openTime: String,

    @SerializedName(value = "close_time", alternate = ["closeTime"])
    val closeTime: String,

    @SerializedName(value = "status", alternate = ["market_status"])
    val status: String, // "Running for Open" | "Running for Close" | "Closed for Today"

    @SerializedName(value = "is_open", alternate = ["is_active", "isOpen"])
    val isOpen: Boolean = true,

    @SerializedName("market_type")
    val marketType: String? = "regular"
)

/**
 * User account & wallet balance DTO matching Matka777 user model
 */
data class UserProfileDto(
    @SerializedName(value = "user_id", alternate = ["id"])
    val id: String,

    @SerializedName(value = "phone", alternate = ["mobile", "phone_number"])
    val phone: String,

    @SerializedName(value = "name", alternate = ["user_name"])
    val name: String,

    @SerializedName(value = "wallet_balance", alternate = ["balance"])
    val balance: Double,

    @SerializedName(value = "account_status", alternate = ["status"])
    val status: String = "Active",

    @SerializedName("bonus_balance")
    val bonusBalance: Double = 0.0
)

/**
 * Game rate payout multipliers DTO matching Matka777 rates matrix
 */
data class GameRateDto(
    @SerializedName(value = "title", alternate = ["game_type", "name"])
    val title: String,

    @SerializedName(value = "rate", alternate = ["payout_ratio"])
    val rate: String,

    @SerializedName("category")
    val category: String? = "Matka"
)

/**
 * Request payload when user places bets in Matka777 SubGameBetActivity
 */
data class PlaceBidRequest(
    @SerializedName(value = "market_name", alternate = ["market_id"])
    val marketName: String,

    @SerializedName("session")
    val session: String, // "Open" | "Close"

    @SerializedName(value = "game_type", alternate = ["game_id", "game_title"])
    val gameType: String, // "single_digit", "jodi_digit", "single_pana", etc.

    @SerializedName(value = "items", alternate = ["bets", "bid_items"])
    val items: List<BidItemDto>
)

data class BidItemDto(
    @SerializedName(value = "number", alternate = ["bid_number", "digit"])
    val number: String,

    @SerializedName(value = "points", alternate = ["bid_points", "amount"])
    val points: Int
)

data class PlaceBidResponse(
    @SerializedName(value = "transaction_id", alternate = ["bid_id"])
    val transactionId: String,

    @SerializedName(value = "deducted_points", alternate = ["points_deducted"])
    val deductedPoints: Int,

    @SerializedName(value = "remaining_balance", alternate = ["new_balance"])
    val remainingBalance: Double,

    @SerializedName("placed_count")
    val placedCount: Int
)

/**
 * DTO for declaring market result in Matka777 Admin Control Panel
 */
data class DeclareResultRequest(
    @SerializedName("market_id") val marketId: String,
    @SerializedName("open_pana") val openPana: String,
    @SerializedName("close_pana") val closePana: String,
    @SerializedName("jodi") val jodi: String
)

/**
 * Remote system configuration & announcement ticker DTO matching Matka777 admin
 */
data class AdminConfigDto(
    @SerializedName(value = "running_ticker", alternate = ["app_notice", "notice_text"])
    val runningTicker: String,

    @SerializedName(value = "min_deposit", alternate = ["minimum_deposit"])
    val minDeposit: Int,

    @SerializedName(value = "support_phone", alternate = ["contact_phone"])
    val supportPhone: String,

    @SerializedName(value = "support_whatsapp", alternate = ["whatsapp_number"])
    val supportWhatsapp: String,

    @SerializedName(value = "upi_id", alternate = ["merchant_upi"])
    val upiId: String,

    @SerializedName(value = "maintenance_mode", alternate = ["is_maintenance"])
    val maintenanceMode: Boolean,

    @SerializedName(value = "app_version", alternate = ["version"])
    val appVersion: String = "2.5.0"
)