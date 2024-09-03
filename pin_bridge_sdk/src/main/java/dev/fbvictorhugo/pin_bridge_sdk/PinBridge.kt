package dev.fbvictorhugo.pin_bridge_sdk

class PinBridge {

    fun buildAuthorizationUrl(
        clientId: String,
        redirectUri: String,
        scope: String
    ): String {
        return "https://www.pinterest.com/oauth/?" +
                "client_id=$clientId" +
                "&redirect_uri=$redirectUri" +
                "&response_type=code" +
                "&scope=$scope"
    }

}