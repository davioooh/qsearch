package com.davioooh.stackoverflow.api


class SoAuthApi(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String
) {
    fun fetchAccessToken(code: String): AccessTokenDetails? =
        TODO("Da implementare")

    companion object {
        const val AUTH_BASE = "https://stackoverflow.com/oauth"
    }
}


