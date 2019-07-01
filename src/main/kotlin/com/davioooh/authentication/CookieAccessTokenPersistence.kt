package com.davioooh.authentication

import com.davioooh.stackexchange.api.AccessTokenDetails
import com.davioooh.utils.TokenEncryption
import com.davioooh.utils.fromFormEncoded
import com.davioooh.utils.toFormEncoded
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context

class CookieAccessTokenPersistence(
    private val objectMapper: ObjectMapper,
    private val tokenEncryption: TokenEncryption = NoopEncryption()
) : AccessTokenPersistence {
    override fun persist(ctx: Context, accessTokenDetails: AccessTokenDetails) {
        ctx.cookie(
            COOKIE_NAME,
            tokenEncryption.encrypt(objectMapper.writeValueAsString(accessTokenDetails)).toFormEncoded()
        )
    }

    override fun retrieve(ctx: Context): AccessTokenDetails? =
        ctx.cookie(COOKIE_NAME)?.let { encodedToken ->
            tokenEncryption.decrypt(encodedToken.fromFormEncoded()).let { decodedToken ->
                objectMapper.readValue<AccessTokenDetails>(decodedToken)
            }
        }

    companion object {
        const val COOKIE_NAME = "so_at"
    }

}

class NoopEncryption : TokenEncryption {
    override fun decrypt(txt: String) = txt

    override fun encrypt(txt: String) = txt
}