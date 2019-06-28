package com.davioooh.stackoverflow.api

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.responseObject

class SoAuthApi(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String
) {

    fun fetchAccessToken(code: String): AccessTokenDetails =
        Fuel.post(
            "$AUTH_BASE/oauth/access_token/json", listOf(
                "redirect_uri" to redirectUri,
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "code" to code
            )
        ).responseObject<AccessTokenDetails>().let {
            val (_, rep, res) = it
            res.get()
        }

    companion object {
        const val AUTH_BASE = "https://stackoverflow.com/oauth"
    }
}


