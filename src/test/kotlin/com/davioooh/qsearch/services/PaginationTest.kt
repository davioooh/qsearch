package com.davioooh.qsearch.services

import com.davioooh.qsearch.model.toQuestionDetailsList
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PaginationTest {
    private val questions = questionsList(8)

    @Nested
    inner class BuildPageFun {

        @Test
        fun `when page is less than 0 throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { buildPage(questions, PaginationCriteria(-1, 25)) }
                .withMessage("paginationCriteria.page must be greater than 0")
        }

        @Test
        fun `when page is 0 throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { buildPage(questions, PaginationCriteria(0, 25)) }
                .withMessage("paginationCriteria.page must be greater than 0")
        }

        @Test
        fun `when pageSize is less than 0 throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { buildPage(questions, PaginationCriteria(1, -1)) }
                .withMessage("paginationCriteria.pageSize must be greater than 0")
        }

        @Test
        fun `when pageSize is 0 throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { buildPage(questions, PaginationCriteria(1, 0)) }
                .withMessage("paginationCriteria.pageSize must be greater than 0")
        }

        @Test
        fun `when required page is greater than last page throws an exception`() {
            val expectedLastPage = 1

            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { buildPage(questions, PaginationCriteria(2, 10)) }
                .withMessage("paginationCriteria.page can't be greater than $expectedLastPage")
        }

        @Test
        fun `when items is empty throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { buildPage(listOf(), PaginationCriteria(2, 10)) }
                .withMessage("questions can't be an empty list")
        }

        @Test
        fun `returns page correctly`() {
            val expectedResult = PageResult(
                questions.subList(3, 6).toQuestionDetailsList(),
                questions.size,
                PaginationCriteria(2, 3)
            )

            val result = buildPage(questions, PaginationCriteria(2, 3))

            assertThat(result).isNotNull()
            assertThat(result).isEqualTo(expectedResult)
        }
    }

    @Nested
    inner class CalculateLastPageFun {
        @Test
        fun `calculate last page correctly`() {
            assertThat(calculateLastPage(questions.size, 3)).isEqualTo(3)
        }
    }
}
