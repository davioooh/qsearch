package com.davioooh.qsearch.services

import com.davioooh.qsearch.authentication.AuthenticationInfoHolder
import com.davioooh.qsearch.authentication.authenticatedUser
import com.davioooh.qsearch.model.PageableResult
import com.davioooh.qsearch.stackexchange.api.QuestionsApi
import com.davioooh.qsearch.stackexchange.api.model.ResultWrapper
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class QuestionsServiceTest {

    private val questionsApi = mockk<QuestionsApi>(relaxed = true)
    private val userFavoritesService = QuestionsService(questionsApi)

    private val questions = listOf(
        question(1, "Q1"),
        question(2, "Q2"),
        question(3, "Q3"),
        question(4, "Q4"),
        question(5, "Q5")
    )

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    @Test
    fun `when all items fits in a page returns page correctly`() {
        val page = 1
        val pageSize = 25
        testUserFavoritesPage(
            page,
            pageSize,
            PageableResult(
                questions,
                page,
                pageSize,
                questions.size
            )
        )
    }

    @Test
    fun `when items span on multiple pages returns page correctly`() {
        val page = 2
        val pageSize = 3
        testUserFavoritesPage(
            page,
            pageSize,
            PageableResult(
                questions.chunked(3)[1],
                page,
                pageSize,
                questions.size
            )
        )
    }

    private fun <T> testUserFavoritesPage(page: Int, pageSize: Int, expectedResult: PageableResult<T>) {
        val authenticatedUser = authenticatedUser()

        mockkObject(AuthenticationInfoHolder)
        every { AuthenticationInfoHolder.currentUser } returns authenticatedUser

        every {
            questionsApi.fetchUserFavoriteQuestions(
                authenticatedUser.accessToken,
                authenticatedUser.userId
            )
        } returns resultWrapper(questions)

        val result = userFavoritesService.getUserFavorites(authenticatedUser.userId, page, pageSize)

        assertThat(result).isEqualTo(expectedResult)
    }

    private fun <T> resultWrapper(items: List<T> = listOf<T>()) = ResultWrapper(
        hasMore = false,
        items = items,
        quotaMax = 999,
        quotaRemaining = 999
    )
}