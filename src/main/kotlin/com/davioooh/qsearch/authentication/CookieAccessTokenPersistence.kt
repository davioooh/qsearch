package com.davioooh.qsearch.authentication

import com.davioooh.qsearch.stackexchange.api.model.AccessTokenDetails
import com.davioooh.qsearch.utils.TokenEncryption
import com.davioooh.qsearch.utils.fromFormEncoded
import com.davioooh.qsearch.utils.toFormEncoded
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.slf4j.LoggerFactory
import java.security.GeneralSecurityException

class CookieAccessTokenPersistence(
    private val objectMapper: ObjectMapper,
    private val tokenEncryption: TokenEncryption = NoopEncryption()
) : AccessTokenPersistence {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun persist(ctx: Context, accessTokenDetails: AccessTokenDetails) {
        ctx.cookie(
            ACCESS_TOKEN_COOKIE_NAME,
            tokenEncryption.encrypt(objectMapper.writeValueAsString(accessTokenDetails)).toFormEncoded()
        )
    }

    override fun retrieve(ctx: Context): AccessTokenDetails? =
        try {
            ctx.cookie(ACCESS_TOKEN_COOKIE_NAME)?.let { encodedToken ->
                tokenEncryption.decrypt(encodedToken.fromFormEncoded()).let { decodedToken ->
                    objectMapper.readValue<AccessTokenDetails>(decodedToken)
                }
            }
        } catch (ex: GeneralSecurityException) {
            logger.error("Cannot retrieve access token", ex)
            null
        }

    companion object {
        const val ACCESS_TOKEN_COOKIE_NAME = "so_at"
    }

}

class NoopEncryption : TokenEncryption {
    override fun decrypt(txt: String) = txt

    override fun encrypt(txt: String) = txt
}