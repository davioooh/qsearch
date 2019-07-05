package com.davioooh.qsearch.authentication

import com.davioooh.qsearch.stackexchange.api.AuthApi
import com.davioooh.qsearch.utils.fromBase64Url
import com.davioooh.qsearch.utils.toParameters
import io.javalin.http.Context
import io.javalin.http.Handler
import org.eclipse.jetty.http.HttpStatus

class OAuthCallbackHandler(
    private val authApi: AuthApi,
    private val accessTokenPersistence: AccessTokenPersistence
) : Handler {

    override fun handle(ctx: Context) {
        val state = ctx.queryParam("state")?.fromBase64Url()?.toParameters() ?: emptyList()
        val csrfInState = state.find { it.first == "csrf" }?.second

        if (csrfInState == null || csrfInState != ctx.cookie(CSRF_NAME)) {
            ctx.status(HttpStatus.FORBIDDEN_403)
            return
        }

        ctx.queryParam("code")?.let { code ->
            authApi.fetchAccessToken(code).let { tokenDetails ->
                val originalUri = state.find { it.first == "uri" }?.second ?: "/"
                accessTokenPersistence.persist(ctx, tokenDetails)
                ctx.header("Location", originalUri).status(HttpStatus.TEMPORARY_REDIRECT_307)
            }
        } ?: ctx.status(HttpStatus.FORBIDDEN_403)
    }

}