package com.davioooh.qsearch.authentication

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

internal class AuthenticationInfoHolderTest {

    @Test
    fun `when current user not set throws exception`() {
        Thread(Runnable {
            assertThatExceptionOfType(IllegalStateException::class.java)
                .isThrownBy { AuthenticationInfoHolder.currentUser }
        }).start()
    }

    @Test
    fun `when current user is set returns it`() {
        Thread(Runnable {
            val authenticatedUser = AuthenticatedUser(10, "test-user", "test-token")
            AuthenticationInfoHolder.setCurrentUser(authenticatedUser)

            val currentUser = AuthenticationInfoHolder.currentUser

            assertThat(currentUser).isEqualTo(authenticatedUser)
        }).start()
    }

}