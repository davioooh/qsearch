package com.davioooh.qsearch.authentication

import com.davioooh.qsearch.stackexchange.api.model.AccessTokenDetails
import io.javalin.http.Context

interface AccessTokenPersistence {
    fun persist(ctx: Context, accessTokenDetails: AccessTokenDetails)
    fun retrieve(ctx: Context): AccessTokenDetails?
}