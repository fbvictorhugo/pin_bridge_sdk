package dev.fbvictorhugo.pin_bridge_sdk.api

import com.google.gson.annotations.SerializedName
import dev.fbvictorhugo.pin_bridge_sdk.data.PPin

data class GetPinsResponse(

    @SerializedName("items")
    val pins: List<PPin>,

    @SerializedName("bookmark")
    val bookmark: String

)