package com.davioooh

import com.davioooh.authentication.CookieAccessTokenPersistence
import com.davioooh.authentication.OAuthAccessManager
import com.davioooh.authentication.OAuthCallbackHandler
import com.davioooh.authentication.OAuthRedirectHandler
import com.davioooh.stackexchange.api.ApiClientConfig
import com.davioooh.stackexchange.api.AuthApi
import com.davioooh.stackexchange.api.UsersApi
import com.davioooh.utils.AesEncryption
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.plugin.json.JavalinJackson

fun main(args: Array<String>) {
    val objMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    val accessTokenPersistence =
        CookieAccessTokenPersistence(objMapper, AesEncryption(System.getProperty("encryptionKey").toByteArray()))

    Javalin
        .create {
            JavalinJackson.configure(objMapper)

            it.accessManager(
                OAuthAccessManager(
                    accessTokenPersistence,
                    UsersApi(ApiClientConfig("stackoverflow", System.getProperty("key"), "!9Z(-x-Ptf")),
                    OAuthRedirectHandler(
                        System.getProperty("clientId"),
                        System.getProperty("scopes").split(", "),
                        System.getProperty("redirectUri")
                    ),
                    excludedPaths = listOf("/back")
                )
            )
        }
        .start()
        .routes {
            // > auth
            get(
                "/back", OAuthCallbackHandler(
                    AuthApi(
                        System.getProperty("clientId"),
                        System.getProperty("clientSecret"),
                        System.getProperty("redirectUri")
                    ),
                    accessTokenPersistence
                )
            )

            // >
            get("/") {
                it.render("/templates/index.html")
            }
        }
}

