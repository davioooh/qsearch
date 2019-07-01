package com.davioooh.stackexchange.api

import com.davioooh.stackexchange.api.model.ResultWrapper
import com.davioooh.stackexchange.api.model.User
import com.github.kittinunf.fuel.jackson.responseObject

class UsersApi(conf: ApiClientConfig) {
    private val httpClient = buildHttpClient(conf)

    fun fetchUserProfile(accessToken: String): ResultWrapper<User> =
        httpClient.get(
            "/me", listOf(
                "access_token" to accessToken
            )
        ).responseObject<ResultWrapper<User>>().third.get()
}