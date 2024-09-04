package dev.fbvictorhugo.pin_bridge_sdk.data

import com.google.gson.annotations.SerializedName
import dev.fbvictorhugo.pin_bridge_sdk.data.inners.PBoardMedia
import dev.fbvictorhugo.pin_bridge_sdk.data.inners.Privacy
import java.util.Date

data class PBoard(

    @SerializedName("id")
    val id: String,

    @SerializedName("created_at")
    val createdAt: Date,

    @SerializedName("board_pins_modified_at")
    val boardPinsModifiedAt: Date,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("collaborator_count")
    val collaboratorCount: Int,

    @SerializedName("pin_count")
    val pinCount: Int,

    @SerializedName("follower_count")
    val followerCount: Int,

    @SerializedName("media")
    val media: PBoardMedia,

    @SerializedName("owner")
    val owner: POwner,

    @SerializedName("privacy")
    val privacy: Privacy,

    )