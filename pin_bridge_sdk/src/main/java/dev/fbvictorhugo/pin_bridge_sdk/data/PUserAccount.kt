package dev.fbvictorhugo.pin_bridge_sdk.data

import com.google.gson.annotations.SerializedName

data class PUserAccount(

    @SerializedName("account_type")
    val accountType: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("profile_image")
    val profileImage: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("about")
    val about: String,

    @SerializedName("business_name")
    val businessName: String?,

    @SerializedName("board_count")
    val boardCount: Int?,

    @SerializedName("pin_count")
    val pinCount: Int?,

    @SerializedName("follower_count")
    val followerCount: Int?,

    @SerializedName("following_count")
    val followingCount: Int?,

    @SerializedName("monthly_views")
    val monthlyViews: Int?
)
