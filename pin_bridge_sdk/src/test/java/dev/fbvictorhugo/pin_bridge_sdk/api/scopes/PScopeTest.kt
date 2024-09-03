package dev.fbvictorhugo.pin_bridge_sdk.api.scopes

import org.junit.Assert
import org.junit.Test

class PScopeTest {

    @Test
    fun build_BasicReadScopes_ReturnsConcatenatedScopes() {
        val scopes = PScope.build(PScope.Pins.Read, PScope.Boards.Read)
        Assert.assertEquals("pins:read,boards:read", scopes)
    }

    @Test
    fun build_EmptyScope_ReturnsEmptyString() {
        val scopes = PScope.build()
        Assert.assertEquals("", scopes)
    }

}