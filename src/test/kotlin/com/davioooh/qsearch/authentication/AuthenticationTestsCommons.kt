package com.davioooh.qsearch.authentication

import com.davioooh.qsearch.stackexchange.api.model.AccessTokenDetails
import com.davioooh.qsearch.stackexchange.api.model.User
import java.time.Instant

fun authenticatedUser(userId: Int = 10, username: String = "test-user", accessToken: String = "test-token") =
    AuthenticatedUser(userId, username, accessToken)

fun accessToken(token: String = "test-token", expires: Long = 1000, date: Instant = Instant.now()) =
    AccessTokenDetails(token, expires, date)

fun soUser(userId: Int = 10, username: String = "test-user") = User(displayName = username, userId = userId)