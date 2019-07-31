package com.davioooh.qsearch

import com.davioooh.qsearch.authentication.*
import com.davioooh.qsearch.caching.QuestionsCache
import com.davioooh.qsearch.caching.UsersCache
import com.davioooh.qsearch.config.*
import com.davioooh.qsearch.handlers.ajax.SearchFavoritesHandler
import com.davioooh.qsearch.services.QuestionsSearchIndex
import com.davioooh.qsearch.services.QuestionsService
import com.davioooh.qsearch.services.UsersService
import com.davioooh.qsearch.stackexchange.api.ApiClientConfig
import com.davioooh.qsearch.stackexchange.api.AuthApi
import com.davioooh.qsearch.stackexchange.api.QuestionsApi
import com.davioooh.qsearch.stackexchange.api.UsersApi
import com.davioooh.qsearch.utils.AesEncryption
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.plugin.json.JavalinJackson

fun main(args: Array<String>) {
    val objMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    val csrfPersistence = CookieCsrfPersistence()

    val accessTokenPersistence =
        CookieAccessTokenPersistence(
            objMapper,
            AesEncryption(ENCRYPTION_KEY.toByteArray())
        )

    val apiConfig = ApiClientConfig(
        "stackoverflow",
        SO_API_KEY,
        "!9Z(-x-Ptf"
    )

    val usersApi = UsersApi(apiConfig)
    val questionsApi = QuestionsApi(apiConfig)
    val authApi = AuthApi(
        SO_API_CLIENT_ID,
        SO_API_CLIENT_SECRET,
        SO_API_REDIRECT_URI
    )

    val oAuthRedirectHandler = OAuthRedirectHandler(
        SO_API_CLIENT_ID,
        SO_API_SCOPES,
        SO_API_REDIRECT_URI,
        csrfPersistence
    )

    val usersService = UsersService(usersApi, UsersCache)
    val questionsService = QuestionsService(questionsApi, QuestionsCache, QuestionsSearchIndex)

    Javalin
        .create { config ->
            JavalinJackson.configure(objMapper)

            config
                .addStaticFiles("/public")
                .accessManager(
                    OAuthAccessManager(
                        accessTokenPersistence,
                        usersService,
                        oAuthRedirectHandler,
                        excludedPaths = listOf("/back")
                    )
                )
        }
        .start(serverPort())
        .routes {
            // > auth
            get("/back", OAuthCallbackHandler(authApi, csrfPersistence, accessTokenPersistence))

            // >
            get("/") { it.render("/templates/index-vue.html") }

            // > ajax
            path("/ajax") {
                get("/favorites", SearchFavoritesHandler(questionsService))
            }
        }
}
