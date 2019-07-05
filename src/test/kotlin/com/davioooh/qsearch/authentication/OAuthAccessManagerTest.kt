package com.davioooh.qsearch.authentication

import com.davioooh.qsearch.stackexchange.api.UsersApi
import com.davioooh.qsearch.stackexchange.api.model.AccessTokenDetails
import com.davioooh.qsearch.stackexchange.api.model.ResultWrapper
import com.davioooh.qsearch.stackexchange.api.model.User
import io.javalin.http.Context
import io.javalin.http.Handler
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

internal class OAuthAccessManagerTest {

    private val ctx = mockk<Context>(relaxed = true)
    private val reqHandler = mockk<Handler>(relaxed = true)
    private val accessTokenPersistence = mockk<AccessTokenPersistence>(relaxed = true)
    private val soUsersApi = mockk<UsersApi>(relaxed = true)
    private val oAuthRedirectHandler = mockk<OAuthRedirectHandler>(relaxed = true)
    private val oAuthAccessManager =
        OAuthAccessManager(
            accessTokenPersistence,
            soUsersApi,
            oAuthRedirectHandler,
            excludedPaths = listOf("/excluded")
        )


    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @Test
    fun `when access token not found invokes redirect handler`() {
        every { accessTokenPersistence.retrieve(ctx) } returns null

        oAuthAccessManager.manage(reqHandler, ctx, mutableSetOf())

        verify { oAuthRedirectHandler.handle(ctx) }
    }

    @Test
    fun `when access token is expired invokes redirect handler`() {
        val expiredAccessTokenDetails = AccessTokenDetails(
            "test-token",
            1,
            Instant.now().minusSeconds(3600)
        )

        every { accessTokenPersistence.retrieve(ctx) } returns expiredAccessTokenDetails

        oAuthAccessManager.manage(reqHandler, ctx, mutableSetOf())

        assertThat(expiredAccessTokenDetails.isExpired).isTrue()
        verify { oAuthRedirectHandler.handle(ctx) }
    }

    @Test
    fun `when access token found invokes request handler`() {
        val validAccessTokenDetails = AccessTokenDetails(
            "test-token",
            1000
        )

        every { accessTokenPersistence.retrieve(ctx) } returns validAccessTokenDetails

        val username = "User001"
        val userId = 100
        every { soUsersApi.fetchUserProfile(validAccessTokenDetails.token) } returns ResultWrapper(
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

        oAuthAccessManager.manage(reqHandler, ctx, mutableSetOf())

        verify { soUsersApi.fetchUserProfile(validAccessTokenDetails.token) }
        verify {
            AuthenticationInfoHolder.authenticatedUser.set(
                AuthenticatedUser(userId, username, validAccessTokenDetails.token)
            )
        }
        verify { reqHandler.handle(ctx) }
    }

    @Test
    fun `when path is contained in excluded list invokes request handler`() {
        every { ctx.path() } returns "/excluded"

        oAuthAccessManager.manage(reqHandler, ctx, mutableSetOf())

        verify(exactly = 0) { accessTokenPersistence.retrieve(ctx) }
        verify { reqHandler.handle(ctx) }
    }

}