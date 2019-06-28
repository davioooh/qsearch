package com.davioooh.authentication

import io.javalin.core.security.AccessManager
import io.javalin.core.security.Role
import io.javalin.http.Context
import io.javalin.http.Handler
import org.slf4j.LoggerFactory

class SoOAuthAccessManager(
    private val accessTokenPersistence: AccessTokenPersistence,
    private val redirectHandler: OAuthRedirectHandler
) : AccessManager {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun manage(handler: Handler, ctx: Context, permittedRoles: MutableSet<Role>) {
        val tokenDetails = accessTokenPersistence.retrieve(ctx)

        when {
            tokenDetails == null -> {
                logger.debug("No persisted access token found")
                redirectHandler.handle(ctx)
            }
            tokenDetails.isExpired -> {
                logger.debug("Retrieved access token is expired: ${tokenDetails.expirationDate}")
                redirectHandler.handle(ctx)
            }
            else -> handler.handle(ctx)
        }
    }

}