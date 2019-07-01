package com.davioooh.authentication

import com.davioooh.stackexchange.api.AccessTokenDetails
import io.javalin.http.Context

interface AccessTokenPersistence {
    fun persist(ctx: Context, accessTokenDetails: AccessTokenDetails) // TODO ctx necessario?
    fun retrieve(ctx: Context): AccessTokenDetails?
}