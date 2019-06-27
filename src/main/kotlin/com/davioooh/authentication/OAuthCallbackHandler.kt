package com.davioooh.authentication

import com.davioooh.stackoverflow.api.SoAuthApi
import com.davioooh.utils.fromBase64Url
import com.davioooh.utils.toParameters
import io.javalin.http.Context
import io.javalin.http.Handler
import org.eclipse.jetty.http.HttpStatus

class OAuthCallbackHandler(
    private val soAuthApi: SoAuthApi,
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
            soAuthApi.fetchAccessToken(code)?.let { tokenDetails ->
                val originalUri = state.find { it.first == "uri" }?.second ?: "/"
                accessTokenPersistence.persist(ctx, tokenDetails)
                ctx.header("Location", originalUri).status(HttpStatus.TEMPORARY_REDIRECT_307)
                // TODO accessToken = tokenDetails
            }
        } ?: ctx.status(HttpStatus.FORBIDDEN_403)
    }

}