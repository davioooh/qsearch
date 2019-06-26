package com.davioooh.authentication

import com.davioooh.utils.join
import com.davioooh.utils.toBase64Url
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jetty.http.HttpStatus
import org.junit.jupiter.api.Test

class OAuthRedirectHandlerTest {

    @Test
    fun `request is redirected correctly to SO auth url`() {
        val clientId = "client-000"
        val uri = "/test"
        val redirectUri = "/redirect-uri"
        val csrf = "test=csrf"
        val state = listOf(
            "csrf" to csrf,
            "uri" to uri
        ).join().toBase64Url()

        val ctx = mockk<Context>(relaxed = true)
        every { ctx.path() } returns uri

        val slot = slot<String>()
        every { ctx.redirect(capture(slot), HttpStatus.TEMPORARY_REDIRECT_307) } answers { nothing }

        OAuthRedirectHandler(clientId, listOf(), redirectUri) { csrf }
            .handle(ctx)

        verify { ctx.redirect(any(), HttpStatus.TEMPORARY_REDIRECT_307) }

        assertThat(slot.captured).isEqualTo(
            "https://stackoverflow.com/oauth?client_id=$clientId&scope=&redirect_uri=$redirectUri&state=$state"
        )

    }

}