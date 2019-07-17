package com.davioooh.qsearch.authentication

import com.davioooh.qsearch.services.UsersService
import io.javalin.core.security.AccessManager
import io.javalin.core.security.Role
import io.javalin.http.Context
import io.javalin.http.Handler
import org.slf4j.LoggerFactory

class OAuthAccessManager(
    private val accessTokenPersistence: AccessTokenPersistence,
    private val usersService: UsersService,
    private val redirectHandler: OAuthRedirectHandler,
    private val excludedPaths: List<String> = listOf()
) : AccessManager {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun manage(handler: Handler, ctx: Context, permittedRoles: MutableSet<Role>) {
        if (excludedPaths.contains(ctx.path())) {
            handler.handle(ctx)
            return
        }

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
            else -> {
                usersService.getMe(tokenDetails.token)?.let { user ->
                    AuthenticationInfoHolder.setCurrentUser(
                        AuthenticatedUser(
                            user.userId,
                            user.displayName,
                            tokenDetails.token
                        )
                    )
                } ?: throw Exception("Cannot fetch user data")

                handler.handle(ctx)
            }
        }
    }

}