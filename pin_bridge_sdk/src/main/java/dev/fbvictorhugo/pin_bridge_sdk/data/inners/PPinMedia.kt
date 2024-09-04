package dev.fbvictorhugo.pin_bridge_sdk.data.inners

import com.google.gson.annotations.SerializedName

data class PPinMedia(

    @SerializedName("media_type")
    val mediaType: String,

    @SerializedName("images")
    val images: PPinImages

)