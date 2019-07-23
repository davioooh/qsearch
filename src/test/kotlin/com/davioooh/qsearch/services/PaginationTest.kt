package com.davioooh.qsearch.services

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PaginationTest {
    private val itemsList = listOf("ab", "bc", "cd", "de", "ef", "fg", "gh", "hi")

    @Nested
    inner class PaginateFun {

        @Test
        fun `when page is less than 0 throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(itemsList, -1, 25) }
                .withMessage("page must be greater than 0")
        }

        @Test
        fun `when page is 0 throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(itemsList, 0, 25) }
                .withMessage("page must be greater than 0")
        }

        @Test
        fun `when pageSize is less than 0 throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(itemsList, 1, -1) }
                .withMessage("pageSize must be greater than 0")
        }

        @Test
        fun `when pageSize is 0 throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(itemsList, 1, 0) }
                .withMessage("pageSize must be greater than 0")
        }

        @Test
        fun `when required page is greater than last page throws an exception`() {
            val expectedLastPage = 1

            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(itemsList, 2, 10) }
                .withMessage("page can't be greater than $expectedLastPage")
        }

        @Test
        fun `when items is empty throws an exception`() {
            assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(listOf<Any>(), 2, 10) }
                .withMessage("items can't be an empty list")
        }

        @Test
        fun `returns page correctly`() {
            val expectedResult = PageResult(
                listOf("de", "ef", "fg"),
                2,
                3,
                8
            )

            val result = paginate(itemsList, 2, 3)

            assertThat(result).isNotNull()
            assertThat(result).isEqualTo(expectedResult)
        }
    }

    @Nested
    inner class CalculateLastPageFun {
        @Test
        fun `calculate last page correctly`() {
            assertThat(calculateLastPage(itemsList.size, 3)).isEqualTo(3)
        }
    }
}
