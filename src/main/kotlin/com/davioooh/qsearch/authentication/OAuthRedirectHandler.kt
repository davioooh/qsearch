package com.davioooh.qsearch.authentication

import com.davioooh.qsearch.stackexchange.api.AUTH_BASE
import com.davioooh.qsearch.utils.toBase64Url
import com.davioooh.qsearch.utils.toUrl
import io.javalin.http.Context
import io.javalin.http.Handler
import org.eclipse.jetty.http.HttpStatus
import java.math.BigInteger
import java.security.SecureRandom

class OAuthRedirectHandler(
    private val clientId: String,
    private val scopes: List<String>,
    private val redirectUri: String,
    private val csrfPersistence: CsrfPersistence,
    private val csrfGenerator: () -> String = { BigInteger(130, SecureRandom()).toString(32) }
) : Handler {

    override fun handle(ctx: Context) {
        val csrf = csrfGenerator()
        csrfPersistence.persist(ctx, csrf)
        val state = listOf(
            "csrf" to csrf,
            "uri" to ctx.path()
        ).toUrl().toBase64Url()
        ctx.redirect(
            "$AUTH_BASE?" +
                    "client_id=$clientId&" +
                    "scope=${scopes.joinToString(",")}&" +
                    "redirect_uri=$redirectUri&" +
                    "state=$state",
            HttpStatus.TEMPORARY_REDIRECT_307
        )
    }

}