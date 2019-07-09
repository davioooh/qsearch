package com.davioooh.qsearch.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class QuestionsFormattingTest {

    @Test
    fun `format M correctly with decimal digits`() {
        assertThat(15_334_000.toNiceString()).isEqualTo("15.3M")
    }

    @Test
    fun `format M correctly without decimal digits`() {
        assertThat(4_000_000.toNiceString()).isEqualTo("4M")
    }

    @Test
    fun `format K correctly with decimal digits`() {
        assertThat(534_600.toNiceString()).isEqualTo("534.6K")
    }

    @Test
    fun `format K correctly without decimal digits`() {
        assertThat(80_000.toNiceString()).isEqualTo("80K")
    }

    @Test
    fun `format correctly numbers lower than 1000`() {
        assertThat(980.toNiceString()).isEqualTo("980")
    }
}