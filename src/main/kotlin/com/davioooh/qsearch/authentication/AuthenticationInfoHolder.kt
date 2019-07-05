package com.davioooh.qsearch.authentication

object AuthenticationInfoHolder {
    val authenticatedUser = ThreadLocal<AuthenticatedUser>() // TODO gestire eccezione per utente non valorizzato
}

data class AuthenticatedUser(val userId: Int, val username: String, val accessToken: String)