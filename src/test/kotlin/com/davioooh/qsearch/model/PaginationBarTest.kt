package com.davioooh.qsearch.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PaginationBarTest {

    @Test
    fun `buttons are populated correctly`() {
        val currentPage = 1
        val lastPage = 1

        val paginationBar = PaginationBar.from(currentPage, lastPage, "/base")

        assertThat(paginationBar.buttonsBar).hasSize(1)
        assertThat(paginationBar.buttonsBar[0]).isEqualTo(PaginationButton("/base?page=1", "1", enabled = true))
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4, 5])
    fun `when pages are less than or equal to 5 shows only buttons bar`(lastPage: Int) {
        val currentPage = 1

        val paginationBar = PaginationBar.from(currentPage, lastPage)

        assertThat(paginationBar.currentPage)
            .withFailMessage("[Current page] expected: $currentPage; actual: ${paginationBar.currentPage}")
            .isEqualTo(currentPage)

        assertThat(paginationBar.firstButton)
            .withFailMessage("[First button]: expected: null; actual: ${paginationBar.firstButton}")
            .isNull()

        assertThat(paginationBar.lastButton)
            .withFailMessage("[Last button]: expected: null; actual: ${paginationBar.lastButton}")
            .isNull()

        assertThat(paginationBar.buttonsBar).hasSize(lastPage)
        assertThat(paginationBar.buttonsBar[0].text).isEqualTo("1")
        assertThat(paginationBar.buttonsBar[lastPage - 1].text).isEqualTo(lastPage.toString())
    }
}
