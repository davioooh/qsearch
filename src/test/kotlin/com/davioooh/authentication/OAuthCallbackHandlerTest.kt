package com.davioooh.authentication

import com.davioooh.AuthenticatedUser
import com.davioooh.AuthenticationInfoHolder
import com.davioooh.stackexchange.api.AuthApi
import com.davioooh.stackexchange.api.UsersApi
import com.davioooh.stackexchange.api.model.AccessTokenDetails
import com.davioooh.stackexchange.api.model.ResultWrapper
import com.davioooh.stackexchange.api.model.User
import com.davioooh.utils.Parameter
import com.davioooh.utils.toBase64Url
import com.davioooh.utils.toUrl
import io.javalin.http.Context
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class OAuthCallbackHandlerTest {

    private val ctx = mockk<Context>(relaxed = true)
    private val soAuthApi = mockk<AuthApi>(relaxed = true)
    private val soUsersApi = mockk<UsersApi>(relaxed = true)
    private val accessTokenPersistence = mockk<AccessTokenPersistence>(relaxed = true)
    private val oAuthCallbackHandler = OAuthCallbackHandler(soAuthApi, soUsersApi, accessTokenPersistence)

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @Test
    fun `when CRSF in State is null sets HTTP status to 403`() {
        every { ctx.queryParam("state") } returns null

        oAuthCallbackHandler.handle(ctx)

        verify { ctx.queryParam("state") }
        verify { ctx.status(403) }
    }

    @Test
    fun `when CRSF in State does not match persisted CRSF sets HTTP status to 403`() {
        every { ctx.queryParam("state") } returns listOf(Parameter("csrf", "csrf-code")).toUrl().toBase64Url()
        every { ctx.cookie(CSRF_NAME) } returns "different-code"

        oAuthCallbackHandler.handle(ctx)

        verify { ctx.queryParam("state") }
        verify { ctx.cookie(CSRF_NAME) }
        verify { ctx.status(403) }
    }

    @Test
    fun `when Code param is null sets HTTP status to 403`() {
        every { ctx.queryParam("state") } returns listOf(Parameter("csrf", "csrf-code")).toUrl().toBase64Url()
        every { ctx.cookie(CSRF_NAME) } returns "csrf-code"
        every { ctx.queryParam("code") } returns null

        oAuthCallbackHandler.handle(ctx)

        verify { ctx.queryParam("code") }
        verify { ctx.status(403) }
    }

    @Test
    fun `access token is fetched correctly`() {
        val csrf = "csrf-code"
        val code = "api-code"

        every { ctx.queryParam("state") } returns listOf(
            Parameter("csrf", csrf),
            Parameter("uri", "/original-url")
        ).toUrl().toBase64Url()
        every { ctx.cookie(CSRF_NAME) } returns csrf
        every { ctx.queryParam("code") } returns code

        val accessTokenDetails = AccessTokenDetails("test-token", 1000)
        every { soAuthApi.fetchAccessToken(code) } returns accessTokenDetails

        val username = "User001"
        val userId = 100
        every { soUsersApi.fetchUserProfile(accessTokenDetails.token) } returns ResultWrapper(
            items = listOf(
                User(
                    displayName = username,
                    userId = userId
                )
            ),
            hasMore = false,
            quotaMax = 0,
            quotaRemaining = 0
        )

        mockkObject(AuthenticationInfoHolder)
        every { AuthenticationInfoHolder.authenticatedUser.set(any()) } answers { nothing }

        oAuthCallbackHandler.handle(ctx)

        verify { ctx.queryParam("code") }
        verify { soAuthApi.fetchAccessToken(code) }
        verify { accessTokenPersistence.persist(ctx, accessTokenDetails) }
        verify { ctx.header("Location", "/original-url").status(307) }
        verify { soUsersApi.fetchUserProfile(accessTokenDetails.token) }
        verify {
            AuthenticationInfoHolder.authenticatedUser.set(
                AuthenticatedUser(userId, username, accessTokenDetails.token)
            )
        }
    }

}