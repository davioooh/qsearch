package com.davioooh.qsearch.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class PaginationBarTest {

    @Test
    fun `builds url correctly`() {
        assertThat(PaginationBar.buildUrl("/", listOf())).isEqualTo("/")
        assertThat(PaginationBar.buildUrl("/", listOf("page" to "10"))).isEqualTo("/?page=10")
        assertThat(PaginationBar.buildUrl("/base?", listOf("arg" to "x"))).isEqualTo("/base?arg=x")
        assertThat(
            PaginationBar.buildUrl(
                "/baseUrl?aaa=12",
                listOf("page" to "10")
            )
        ).isEqualTo("/baseUrl?aaa=12&page=10")
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { PaginationBar.buildUrl("/baseUrl?aaa?ee", listOf("page" to "10")) }
            .withMessage("Invalid base url")
    }

    @Nested
    inner class WhenPagesAreLessThanOrEqualTo10 {

        @ParameterizedTest
        @MethodSource("configsMax10Pages")
        fun `shows only buttons bar`(barConfig: PaginationBarConfig) {
            val expected = paginationBar(
                barConfig.currentPage,
                barConfig.lastPage
            )

            val paginationBar = PaginationBar.from(barConfig.currentPage, barConfig.lastPage)

            assertThat(paginationBar).isEqualTo(expected)
        }

        private fun configsMax10Pages() =
            IntRange(1, 10).flatMap { lastPage ->
                IntRange(1, lastPage).map { currentPage ->
                    PaginationBarConfig(currentPage, lastPage)
                }
            }

    }

    @Nested
    inner class WhenPagesAreMoreThan10 {

        @ParameterizedTest
        @MethodSource("configMoreThan10PagesStartingInterval")
        fun `when current page is lower than 5 shows buttons bar and last button`(barConfig: PaginationBarConfig) {
            val expected = paginationBar(
                barConfig.currentPage,
                barConfig.lastPage,
                1, 5,
                lastButtonVisible = true
            )

            val paginationBar = PaginationBar.from(barConfig.currentPage, barConfig.lastPage)

            assertThat(paginationBar).isEqualTo(expected)
        }

        @ParameterizedTest
        @MethodSource("configMoreThan10PagesMiddleInterval")
        fun `when current page is greater than 4 and lower than (lastPage - 3) shows controls correctly`(barConfig: PaginationBarConfig) {
            val expected = paginationBar(
                barConfig.currentPage,
                barConfig.lastPage,
                barConfig.currentPage - 2, barConfig.currentPage + 2,
                firstButtonVisible = true, lastButtonVisible = true
            )

            val paginationBar = PaginationBar.from(barConfig.currentPage, barConfig.lastPage)

            assertThat(paginationBar).isEqualTo(expected)
        }

        @ParameterizedTest
        @MethodSource("configMoreThan10PagesEndingInterval")
        fun `when current page is greater than (lastPage - 3) shows controls correctly`(barConfig: PaginationBarConfig) {
            val expected = paginationBar(
                barConfig.currentPage,
                barConfig.lastPage,
                barConfig.lastPage - 4, barConfig.lastPage,
                firstButtonVisible = true
            )

            val paginationBar = PaginationBar.from(barConfig.currentPage, barConfig.lastPage)

            assertThat(paginationBar).isEqualTo(expected)
        }

        private val lastPageTo = 20

        private fun configMoreThan10PagesStartingInterval() =
            IntRange(11, lastPageTo).flatMap { lastPage ->
                IntRange(1, 4).map { currentPage ->
                    PaginationBarConfig(currentPage, lastPage)
                }
            }

        private fun configMoreThan10PagesMiddleInterval() =
            IntRange(11, lastPageTo).flatMap { lastPage ->
                IntRange(5, lastPage - 4).map { currentPage ->
                    PaginationBarConfig(currentPage, lastPage)
                }
            }

        private fun configMoreThan10PagesEndingInterval() =
            IntRange(11, lastPageTo).flatMap { lastPage ->
                IntRange(lastPage - 3, lastPage).map { currentPage ->
                    PaginationBarConfig(currentPage, lastPage)
                }
            }

    }

    data class PaginationBarConfig(
        val currentPage: Int,
        val lastPage: Int
    )

    private fun paginationBtn(pageNum: Int, btnText: String = pageNum.toString(), isCurrent: Boolean = false) =
        PaginationButton("/?page=$pageNum", btnText, isCurrent = isCurrent)

    private fun prevBtn(currentPageNum: Int) = paginationBtn(currentPageNum - 1, "Prev")
    private fun nextBtn(currentPageNum: Int) = paginationBtn(currentPageNum + 1, "Next")

    private fun paginationBar(
        currentPageNum: Int,
        lastPageNum: Int,
        btnBarStart: Int = 1,
        btnBarEnd: Int = lastPageNum,
        firstButtonVisible: Boolean = false,
        lastButtonVisible: Boolean = false
    ) = PaginationBar(
        IntRange(btnBarStart, btnBarEnd).map { paginationBtn(it, isCurrent = currentPageNum == it) }.toTypedArray(),
        currentPageNum,
        firstButton = if (firstButtonVisible) paginationBtn(1) else null,
        lastButton = if (lastButtonVisible) paginationBtn(lastPageNum) else null,
        previousButton = if (currentPageNum > 1) prevBtn(currentPageNum) else null,
        nextButton = if (currentPageNum < lastPageNum) nextBtn(currentPageNum) else null
    )

}
