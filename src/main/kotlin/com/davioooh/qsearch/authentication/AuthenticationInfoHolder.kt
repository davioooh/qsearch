package com.davioooh.qsearch.authentication

import kotlin.concurrent.getOrSet

object AuthenticationInfoHolder {
    private val userInfo = ThreadLocal<AuthenticatedUser>()

    val currentUser: AuthenticatedUser
        get() = userInfo.getOrSet { throw IllegalStateException("User info not available") }

    internal fun setCurrentUser(authenticatedUser: AuthenticatedUser) {
        userInfo.set(authenticatedUser)
    }

}

data class AuthenticatedUser(val userId: Int, val username: String, val accessToken: String)