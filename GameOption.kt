package com.rama77.app.model

data class GameOption(
    val id: String,
    val title: String,
    val iconResId: Int,
    val isBulk: Boolean = false
)