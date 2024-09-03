package dev.fbvictorhugo.pin_bridge_sdk.api

import com.google.gson.annotations.SerializedName
import dev.fbvictorhugo.pin_bridge_sdk.data.PBoard

data class GetBoardsResponse(

    @SerializedName("items")
    val boards: List<PBoard>,

    @SerializedName("bookmark")
    val bookmark: String

)