package dev.fbvictorhugo.pin_bridge_sdk

import dev.fbvictorhugo.pin_bridge_sdk.api.scopes.PScope
import org.junit.Assert.assertEquals
import org.junit.Test

class PinBridgeTest {

    @Test
    fun addition_isCorrect() {

        val pinBridge = PinBridge()

        val buildAuthorizationUrl = pinBridge.buildAuthorizationUrl(
            "YOUR_CLIENT_ID",
            "YOUR_REDIRECT_URI",
            PScope.build(PScope.Boards.Read, PScope.Pins.Read)
        )

        assertEquals(
            "https://www.pinterest.com/oauth/?client_id=YOUR_CLIENT_ID&redirect_uri=YOUR_REDIRECT_URI&response_type=code&scope=boards:read,pins:read",
            buildAuthorizationUrl
        )


    }


}