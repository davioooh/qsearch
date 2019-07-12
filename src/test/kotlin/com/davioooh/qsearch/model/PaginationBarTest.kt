package com.davioooh.qsearch.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class PaginationBarTest {

    // TODO test next + previous buttons
    // TODO refactor tests:
    // - split by controls: first button, last button, buttons bar, etc.
    // - code reuse...

    @Nested
    inner class WhenPagesAreLessThanOrEqualTo10 {

        @ParameterizedTest
        @MethodSource("configsMax10Pages")
        fun `shows only buttons bar`(config: PaginationConfig) {
            val paginationBar = PaginationBar.from(config.currentPage, config.lastPage)

            assertThat(paginationBar.currentPage)
                .withFailMessage(
                    "[Current page] expected: %s; actual: %s",
                    config.currentPage,
                    paginationBar.currentPage
                )
                .isEqualTo(config.currentPage)

            assertThat(paginationBar.firstButton)
                .withFailMessage("[First button]: expected: null; actual: ${paginationBar.firstButton}")
                .isNull()

            assertThat(paginationBar.lastButton)
                .withFailMessage("[Last button]: expected: null; actual: ${paginationBar.lastButton}")
                .isNull()

            assertThat(paginationBar.buttonsBar).isEqualTo((1..config.lastPage).map { paginationButton(it) }.toTypedArray())
        }

        private fun configsMax10Pages() = IntRange(1, 10).flatMap { lastPage ->
            IntRange(1, lastPage).map { currentPage ->
                PaginationConfig(currentPage, lastPage)
            }
        }.stream()

    }

    @Nested
    inner class WhenPagesAreMoreThan10 {

        @ParameterizedTest
        @MethodSource("moreThan10PagesCurrentPageMax4")
        fun `when current page is lower than 5 shows buttons bar and last button`(config: PaginationConfig) {
            val paginationBar = PaginationBar.from(config.currentPage, config.lastPage)

            assertThat(paginationBar.currentPage).isEqualTo(config.currentPage)

            assertThat(paginationBar.firstButton).isNull()

            assertThat(paginationBar.lastButton).isEqualTo(paginationButton(config.lastPage))

            assertThat(paginationBar.buttonsBar).isEqualTo((1..5).map { paginationButton(it) }.toTypedArray())
        }

        @ParameterizedTest
        @MethodSource("moreThan10PagesCurrentPageBetween5AndLastPageMinus4")
        fun `when current page is greater than 4 and lower than (lastPage - 3) shows controls correctly`(config: PaginationConfig) {
            val paginationBar = PaginationBar.from(config.currentPage, config.lastPage)

            assertThat(paginationBar.currentPage).isEqualTo(config.currentPage)

            assertThat(paginationBar.firstButton).isEqualTo(paginationButton(1))

            assertThat(paginationBar.lastButton).isEqualTo(paginationButton(config.lastPage))

            assertThat(paginationBar.buttonsBar).isEqualTo(
                IntRange(
                    config.currentPage - 2,
                    config.currentPage + 2
                ).map { paginationButton(it) }.toTypedArray()
            )
        }

        @ParameterizedTest
        @MethodSource("moreThan10PagesCurrentPageGreaterThanLastPageMinus3")
        fun `when current page is greater than (lastPage - 3) shows controls correctly`(config: PaginationConfig) {
            val paginationBar = PaginationBar.from(config.currentPage, config.lastPage)

            assertThat(paginationBar.currentPage).isEqualTo(config.currentPage)

            assertThat(paginationBar.firstButton).isEqualTo(paginationButton(1))

            assertThat(paginationBar.lastButton).isNull()

            assertThat(paginationBar.buttonsBar).isEqualTo(
                IntRange(
                    config.lastPage - 4,
                    config.lastPage
                ).map { paginationButton(it) }.toTypedArray()
            )
        }

        private val lastPageTo = 20

        private fun moreThan10PagesCurrentPageMax4() =
            IntRange(11, lastPageTo).flatMap { lastPage ->
                IntRange(1, 4).map { currentPage ->
                    PaginationConfig(currentPage, lastPage)
                }
            }

        private fun moreThan10PagesCurrentPageBetween5AndLastPageMinus4() =
            IntRange(11, lastPageTo).flatMap { lastPage ->
                IntRange(5, lastPage - 4).map { currentPage ->
                    PaginationConfig(currentPage, lastPage)
                }
            }

        private fun moreThan10PagesCurrentPageGreaterThanLastPageMinus3() =
            IntRange(11, lastPageTo).flatMap { lastPage ->
                IntRange(lastPage - 3, lastPage).map { currentPage ->
                    PaginationConfig(currentPage, lastPage)
                }
            }

    }

    private fun configsProducer(
        lastPageFrom: Int,
        lastPageTo: Int,
        currentPageFrom: Int = 1,
        currentPageTo: Int? = null
    ) =
        IntRange(lastPageFrom, lastPageTo).flatMap { lastPage ->
            IntRange(currentPageFrom, currentPageTo ?: lastPage).map { currentPage ->
                PaginationConfig(currentPage, lastPage)
            }
        }

    data class PaginationConfig(
        val currentPage: Int,
        val lastPage: Int
    )

    private fun paginationButton(pageNum: Int) = PaginationButton("/?page=$pageNum", pageNum.toString())

}
