package com.davioooh

import com.davioooh.authentication.CookieAccessTokenPersistence
import com.davioooh.authentication.OAuthAccessManager
import com.davioooh.authentication.OAuthCallbackHandler
import com.davioooh.authentication.OAuthRedirectHandler
import com.davioooh.stackoverflow.api.SoAuthApi
import com.davioooh.utils.AesEncryption
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.plugin.openapi.jackson.JacksonToJsonMapper.objectMapper

fun main(args: Array<String>) {
    val accessTokenPersistence =
        CookieAccessTokenPersistence(objectMapper, AesEncryption(System.getProperty("encryptionKey").toByteArray()))

    Javalin
        .create {
            it.accessManager(
                OAuthAccessManager(
                    accessTokenPersistence,
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
                    SoAuthApi(
                        System.getProperty("clientId"),
                        System.getProperty("clientSecret"),
                        System.getProperty("redirectUri")
                    ),
                    accessTokenPersistence
                )
            )

            // >
            get("/") { it.render("/templates/index.html") }
        }
}

