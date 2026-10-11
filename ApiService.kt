package com.rama77.app.network

import com.rama77.app.network.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // 1. Fetch dynamic markets list & live results (Matka777 endpoint)
    @GET("api/v1/matka/markets")
    suspend fun getMarkets(): Response<ApiResponse<List<MarketDto>>>

    // Legacy / Alternate endpoint
    @GET("api/v1/markets")
    suspend fun getMarketsAlternate(): Response<ApiResponse<List<MarketDto>>>

    // 2. Fetch single market details by ID
    @GET("api/v1/matka/markets/{id}")
    suspend fun getMarketById(@Path("id") marketId: String): Response<ApiResponse<MarketDto>>

    // 3. Admin Declare Market Result (Auto-calculates winner payouts)
    @PUT("api/v1/matka/markets/{id}/result")
    suspend fun declareMarketResult(
        @Path("id") marketId: String,
        @Body request: DeclareResultRequest
    ): Response<ApiResponse<MarketDto>>

    // 4. Fetch user wallet balance & profile
    @GET("api/v1/user/wallet")
    suspend fun getWalletBalance(): Response<ApiResponse<UserProfileDto>>

    // 5. Admin adjust user wallet balance
    @FormUrlEncoded
    @POST("api/v1/admin/wallet/adjust")
    suspend fun adjustWallet(
        @Field("amount") amount: Double,
        @Field("reason") reason: String
    ): Response<ApiResponse<UserProfileDto>>

    // 6. Place bet / bids from sub-game betting screen
    @POST("api/v1/matka/bids/place")
    suspend fun placeBid(@Body request: PlaceBidRequest): Response<ApiResponse<PlaceBidResponse>>

    // 7. Fetch bid history for specific user
    @GET("api/v1/matka/bids/user/{phone}")
    suspend fun getUserBids(@Path("phone") phone: String): Response<ApiResponse<List<BidItemDto>>>

    // 8. Fetch game rates configured in Matka777 Admin Panel
    @GET("api/v1/matka/rates")
    suspend fun getGameRates(): Response<ApiResponse<List<GameRateDto>>>

    // 9. Fetch remote app config (announcement ticker, support numbers, min deposit)
    @GET("api/v1/admin/config")
    suspend fun getAdminConfig(): Response<ApiResponse<AdminConfigDto>>

    // 10. Authentication: MPIN Login
    @FormUrlEncoded
    @POST("api/v1/auth/login")
    suspend fun login(
        @Field("phone") phone: String,
        @Field("mpin") mpin: String
    ): Response<ApiResponse<UserProfileDto>>
}