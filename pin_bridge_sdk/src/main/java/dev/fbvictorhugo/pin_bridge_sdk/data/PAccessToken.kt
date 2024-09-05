package dev.fbvictorhugo.pin_bridge_sdk.data

import com.google.gson.annotations.SerializedName

data class PAccessToken(

    @SerializedName("response_type")
    val responseType: String,

    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_in")
    val expiresIn: Long,

    @SerializedName("scope")
    val scope: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("refresh_token_expires_in")
    val refreshTokenExpiresIn: Long

)