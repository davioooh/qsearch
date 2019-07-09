package com.davioooh.qsearch.model

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PaginationTest {
    private val itemsList = listOf("ab", "bc", "cd", "de", "ef", "fg", "gh", "hi")

    @Nested
    inner class PaginateFun {

        @Test
        fun `when page is less than 0 throws an exception`() {
            Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(listOf<String>(), -1, 25) }
        }

        @Test
        fun `when page is 0 throws an exception`() {
            Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(listOf<String>(), 0, 25) }
        }

        @Test
        fun `when pageSize is less than 0 throws an exception`() {
            Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(listOf<String>(), 1, -1) }
        }

        @Test
        fun `when pageSize is 0 throws an exception`() {
            Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(listOf<String>(), 0, 0) }
        }

        @Test
        fun `when required page is greater than last page throws an exception`() {
            val expectedLastPage = 1

            Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { paginate(itemsList, 2, 10) }
                .withMessage("page can't be greater than $expectedLastPage")
        }

        @Test
        fun `returns page correctly`() {
            val expectedResult = PageableResult(
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
            assertThat(calculateLastPage(itemsList, 3)).isEqualTo(3)
        }
    }
}
