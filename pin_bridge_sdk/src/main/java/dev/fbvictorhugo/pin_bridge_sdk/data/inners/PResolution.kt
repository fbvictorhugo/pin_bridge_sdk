package dev.fbvictorhugo.pin_bridge_sdk.data.inners

import com.google.gson.annotations.SerializedName

data class PResolution(

    @SerializedName("width")
    val width: Int,

    @SerializedName("height")
    val height: Int?,

    @SerializedName("url")
    val url: String

)