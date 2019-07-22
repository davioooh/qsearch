package com.davioooh.qsearch.services

import com.davioooh.qsearch.caching.CacheFacade
import com.davioooh.qsearch.caching.UsersCache
import com.davioooh.qsearch.stackexchange.api.UsersApi
import com.davioooh.qsearch.stackexchange.api.model.User
import org.slf4j.LoggerFactory

class UsersService(
    private val usersApi: UsersApi,
    private val cache: CacheFacade<User>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getMe(accessToken: String): User? {
        var user = cache.get(accessToken)
        if (user == null) {
            user = usersApi.fetchUserProfile(accessToken).items.firstOrNull()
                ?.also {
                    UsersCache.put(accessToken, it)

                    logger.debug("User ${it.displayName}(${it.userId}) retrieved correctly and cached in UsersCache")
                }
        } else {
            logger.debug("User ${user.displayName}(${user.userId}) found in UsersCache")
        }
        return user
    }

}