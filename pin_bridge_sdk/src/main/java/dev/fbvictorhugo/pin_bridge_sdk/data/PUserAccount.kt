package dev.fbvictorhugo.pin_bridge_sdk.data

data class PUserAccount(
    val account_type: String,
    val id: String,
    val profile_image: String,
    val username: String,
    val about: String,
    val business_name: String,
    val board_count: Int,
    val pin_count: Int,
    val follower_count: Int,
    val following_count: Int,
    val monthly_views: Int
)
