package com.davioooh.qsearch.authentication

object AuthenticationInfoHolder {
    val authenticatedUser = ThreadLocal<AuthenticatedUser>()
}

data class AuthenticatedUser(val userId: Int, val username: String, val accessToken: String)