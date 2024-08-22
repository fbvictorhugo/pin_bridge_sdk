package dev.fbvictorhugo.pin_bridge_sdk.data

data class PBoard(
    override var id: Int,
    var name: String,
    var description: String
) : PModel {

}