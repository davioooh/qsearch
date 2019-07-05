package com.davioooh.qsearch.authentication

import com.davioooh.qsearch.stackexchange.api.AuthApi
import com.davioooh.qsearch.stackexchange.api.model.AccessTokenDetails
import com.davioooh.qsearch.utils.Parameter
import com.davioooh.qsearch.utils.toBase64Url
import com.davioooh.qsearch.utils.toUrl
import io.javalin.http.Context
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class OAuthCallbackHandlerTest {

    private val ctx = mockk<Context>(relaxed = true)
    private val soAuthApi = mockk<AuthApi>(relaxed = true)
    private val csrfPersistence = mockk<CsrfPersistence>(relaxed = true)
    private val accessTokenPersistence = mockk<AccessTokenPersistence>(relaxed = true)
    private val oAuthCallbackHandler = OAuthCallbackHandler(soAuthApi, csrfPersistence, accessTokenPersistence)

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
        every { csrfPersistence.retrieve(ctx) } returns "different-code"

        oAuthCallbackHandler.handle(ctx)

        verify { ctx.queryParam("state") }
        verify { csrfPersistence.retrieve(ctx) }
        verify { ctx.status(403) }
    }

    @Test
    fun `when Code param is null sets HTTP status to 403`() {
        every { ctx.queryParam("state") } returns listOf(Parameter("csrf", "csrf-code")).toUrl().toBase64Url()
        every { csrfPersistence.retrieve(ctx) } returns "csrf-code"
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
        every { csrfPersistence.retrieve(ctx) } returns csrf
        every { ctx.queryParam("code") } returns code

        val accessTokenDetails = AccessTokenDetails("test-token", 1000)
        every { soAuthApi.fetchAccessToken(code) } returns accessTokenDetails

        oAuthCallbackHandler.handle(ctx)

        verify { ctx.queryParam("code") }
        verify { soAuthApi.fetchAccessToken(code) }
        verify { accessTokenPersistence.persist(ctx, accessTokenDetails) }
        verify { ctx.header("Location", "/original-url").status(307) }
    }

}