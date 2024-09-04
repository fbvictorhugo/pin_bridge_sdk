package dev.fbvictorhugo.pin_bridge_sdk.data.inners

import com.google.gson.annotations.SerializedName

data class PPinImages(

    @SerializedName("150x150")
    val resolution150x150: PResolution,

    @SerializedName("400x300")
    val resolution400x300: PResolution,

    @SerializedName("600x")
    val resolution600x: PResolution,

    @SerializedName("1200x")
    val resolution1200x: PResolution,

    )
