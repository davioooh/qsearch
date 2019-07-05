package com.davioooh.qsearch

import com.davioooh.qsearch.authentication.CookieAccessTokenPersistence
import com.davioooh.qsearch.authentication.OAuthAccessManager
import com.davioooh.qsearch.authentication.OAuthCallbackHandler
import com.davioooh.qsearch.authentication.OAuthRedirectHandler
import com.davioooh.qsearch.stackexchange.api.ApiClientConfig
import com.davioooh.qsearch.stackexchange.api.AuthApi
import com.davioooh.qsearch.stackexchange.api.UsersApi
import com.davioooh.qsearch.utils.AesEncryption
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
        CookieAccessTokenPersistence(
            objMapper,
            AesEncryption(System.getProperty("encryptionKey").toByteArray())
        )

    Javalin
        .create {
            JavalinJackson.configure(objMapper)

            it.accessManager(
                OAuthAccessManager(
                    accessTokenPersistence,
                    UsersApi(
                        ApiClientConfig(
                            "stackoverflow",
                            System.getProperty("key"),
                            "!9Z(-x-Ptf"
                        )
                    ),
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

