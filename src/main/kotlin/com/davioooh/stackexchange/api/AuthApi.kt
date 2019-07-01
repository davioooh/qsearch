package com.davioooh.stackexchange.api

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.responseObject

class AuthApi(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String
) {

    fun fetchAccessToken(code: String): AccessTokenDetails =
        Fuel.post(
            "$AUTH_BASE/access_token/json", listOf(
                "redirect_uri" to redirectUri,
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "code" to code
            )
        ).responseObject<AccessTokenDetails>().third.get()

}
