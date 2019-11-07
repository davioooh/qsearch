package com.davioooh.qsearch.config

val SERVER_PORT: Int? = System.getenv("PORT")?.toInt()
val ENCRYPTION_KEY: String = System.getenv("ENCRYPTION_KEY")
val GA_CODE: String = System.getenv("GA_CODE")

val SO_API_KEY: String = System.getenv("SO_API_KEY")
val SO_API_CLIENT_ID: String = System.getenv("SO_API_CLIENT_ID")
val SO_API_CLIENT_SECRET: String = System.getenv("SO_API_CLIENT_SECRET")
val SO_API_REDIRECT_URI: String = System.getenv("SO_API_REDIRECT_URI")
val SO_API_SCOPES = System.getenv("SO_API_SCOPES").split(", ")

fun serverPort() = SERVER_PORT ?: 7000
