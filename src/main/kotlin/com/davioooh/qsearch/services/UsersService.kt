package com.davioooh.qsearch.services

import com.davioooh.qsearch.stackexchange.api.UsersApi
import com.davioooh.qsearch.stackexchange.api.model.User

class UsersService(
    private val usersApi: UsersApi
) {
    fun getMe(accessToken: String): User? {
        return usersApi.fetchUserProfile(accessToken).items.firstOrNull()
    }
}