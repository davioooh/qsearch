package com.davioooh.authentication

import com.davioooh.stackoverflow.api.AccessTokenDetails
import com.davioooh.stackoverflow.api.SoAuthApi
import com.davioooh.utils.Parameter
import com.davioooh.utils.toBase64Url
import com.davioooh.utils.toUrl
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class OAuthCallbackHandlerTest {

    @Test
    fun `when CRSF in State is null sets HTTP status to 403`() {
        val ctx = mockk<Context>(relaxed = true)
        every { ctx.queryParam("state") } returns null

        val soAuthApi = mockk<SoAuthApi>(relaxed = true)
        val accessTokenPersistence = mockk<AccessTokenPersistence>()

        OAuthCallbackHandler(soAuthApi, accessTokenPersistence)
            .handle(ctx)

        verify { ctx.queryParam("state") }
        verify { ctx.status(403) }
    }

    @Test
    fun `when CRSF in State does not match persisted CRSF sets HTTP status to 403`() {
        val ctx = mockk<Context>(relaxed = true)
        every { ctx.queryParam("state") } returns listOf(Parameter("csrf", "csrf-code")).toUrl().toBase64Url()
        every { ctx.cookie(CSRF_NAME) } returns "different-code"

        val soAuthApi = mockk<SoAuthApi>(relaxed = true)
        val accessTokenPersistence = mockk<AccessTokenPersistence>()

        OAuthCallbackHandler(soAuthApi, accessTokenPersistence)
            .handle(ctx)

        verify { ctx.queryParam("state") }
        verify { ctx.cookie(CSRF_NAME) }
        verify { ctx.status(403) }
    }

    @Test
    fun `when Code param is null sets HTTP status to 403`() {
        val ctx = mockk<Context>(relaxed = true)
        every { ctx.queryParam("state") } returns listOf(Parameter("csrf", "csrf-code")).toUrl().toBase64Url()
        every { ctx.cookie(CSRF_NAME) } returns "csrf-code"
        every { ctx.queryParam("code") } returns null

        val soAuthApi = mockk<SoAuthApi>(relaxed = true)
        val accessTokenPersistence = mockk<AccessTokenPersistence>()

        OAuthCallbackHandler(soAuthApi, accessTokenPersistence)
            .handle(ctx)

        verify { ctx.queryParam("code") }
        verify { ctx.status(403) }
    }

    @Test
    fun `access token is fetched correctly`() {
        val ctx = mockk<Context>(relaxed = true)
        val csrf = "csrf-code"
        val code = "api-code"

        every { ctx.queryParam("state") } returns listOf(
            Parameter("csrf", csrf),
            Parameter("uri", "/original-url")
        ).toUrl().toBase64Url()
        every { ctx.cookie(CSRF_NAME) } returns csrf
        every { ctx.queryParam("code") } returns code

        val soAuthApi = mockk<SoAuthApi>(relaxed = true)
        val accessTokenPersistence = mockk<AccessTokenPersistence>(relaxed = true)
        val accessTokenDetails = AccessTokenDetails("test-token", 1000)
        every { soAuthApi.fetchAccessToken(code) } returns accessTokenDetails

        OAuthCallbackHandler(soAuthApi, accessTokenPersistence).handle(ctx)

        verify { ctx.queryParam("code") }
        verify { soAuthApi.fetchAccessToken(code) }
        verify { accessTokenPersistence.persist(ctx, accessTokenDetails) }
        verify { ctx.header("Location", "/original-url").status(307) }
    }

}