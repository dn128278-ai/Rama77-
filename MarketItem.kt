package com.rama77.app.model

data class MarketItem(
    val id: String,
    val name: String,
    val openPana: String,
    val jodi: String,
    val closePana: String,
    val openTime: String,
    val closeTime: String,
    val status: String,
    val isOpen: Boolean
)