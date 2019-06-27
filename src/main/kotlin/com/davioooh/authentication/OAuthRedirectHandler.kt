package com.davioooh.authentication

import com.davioooh.stackoverflow.api.SoAuthApi
import com.davioooh.utils.toBase64Url
import com.davioooh.utils.toUrl
import io.javalin.http.Context
import io.javalin.http.Handler
import org.eclipse.jetty.http.HttpStatus
import java.math.BigInteger
import java.security.SecureRandom

class OAuthRedirectHandler(
    private val clientId: String,
    private val scopes: List<String>,
    private val redirectUri: String,
    private val csrfGenerator: () -> String = { BigInteger(130, SecureRandom()).toString(32) }
) : Handler {

    override fun handle(ctx: Context) {
        val csrf = csrfGenerator()
        ctx.cookie(CSRF_NAME, csrf) // TODO estrarre logica di persistenza csrf?
        val state = listOf(
            "csrf" to csrf,
            "uri" to ctx.path()
        ).toUrl().toBase64Url()
        ctx.redirect(
            "${SoAuthApi.AUTH_BASE}?" +
                    "client_id=$clientId&" +
                    "scope=${scopes.joinToString(",")}&" +
                    "redirect_uri=$redirectUri&" +
                    "state=$state",
            HttpStatus.TEMPORARY_REDIRECT_307
        )
    }

}