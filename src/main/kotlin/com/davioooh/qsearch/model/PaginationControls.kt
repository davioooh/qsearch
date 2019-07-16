package com.davioooh.qsearch.model

import com.davioooh.qsearch.utils.Parameters
import com.davioooh.qsearch.utils.toParameters
import com.davioooh.qsearch.utils.toUrl

data class PaginationBar(
    val buttonsBar: Array<PaginationButton>,
    val currentPage: Int,
    val firstButton: PaginationButton? = null,
    val lastButton: PaginationButton? = null,
    val previousButton: PaginationButton? = null,
    val nextButton: PaginationButton? = null
) {
    companion object {
        fun from(currentPage: Int, lastPage: Int, baseUrl: String = "/"): PaginationBar {
            require(currentPage >= 1) { "currentPage can't be lower than 1" }
            require(currentPage <= lastPage) { "currentPage ($currentPage) can't be greater than lastPage ($lastPage)" }

            val btnUrlTemplate = buildUrl(baseUrl, listOf("page" to "%s"))

            val prevButton = when (currentPage) {
                1 -> null
                else -> PaginationButton(btnUrlTemplate.format(currentPage - 1), "Prev")
            }
            val nextButton = when (currentPage) {
                lastPage -> null
                else -> PaginationButton(btnUrlTemplate.format(currentPage + 1), "Next")
            }

            val btnBarRange =
                if (lastPage in 1..10) 1..lastPage
                else when (currentPage) {
                    in 1..4 -> 1..5
                    in (lastPage - 3)..lastPage -> (lastPage - 4)..lastPage
                    else -> IntRange(
                        if (currentPage - 2 >= 1) currentPage - 2 else 1,
                        if (currentPage + 2 <= lastPage) currentPage + 2 else lastPage
                    )
                }

            val firstButton =
                when (lastPage) {
                    in 1..10 -> null
                    else -> when (currentPage) {
                        in 1..4 -> null
                        else -> PaginationButton(btnUrlTemplate.format(1), 1.toString())
                    }
                }

            val lastButton =
                when (lastPage) {
                    in 1..10 -> null
                    else -> when (currentPage) {
                        in (lastPage - 3)..lastPage -> null
                        else -> PaginationButton(btnUrlTemplate.format(lastPage), lastPage.toString())
                    }
                }

            return PaginationBar(
                btnBarRange.map {
                    PaginationButton(
                        btnUrlTemplate.format(it),
                        it.toString(),
                        currentPage == it
                    )
                }.toTypedArray(),
                currentPage,
                firstButton = firstButton,
                lastButton = lastButton,
                previousButton = prevButton,
                nextButton = nextButton
            )
        }

        fun buildUrl(baseUrl: String, queryParams: Parameters): String {
            val urlPieces = baseUrl.split("?")

            return when (urlPieces.size) {
                1 -> if (queryParams.isNotEmpty()) "$baseUrl?" + queryParams.toUrl() else baseUrl
                2 -> "${urlPieces[0]}?" + (urlPieces[1].toParameters() + queryParams).toUrl()
                else -> throw IllegalArgumentException("Invalid base url")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaginationBar

        if (!buttonsBar.contentEquals(other.buttonsBar)) return false
        if (currentPage != other.currentPage) return false
        if (firstButton != other.firstButton) return false
        if (lastButton != other.lastButton) return false
        if (previousButton != other.previousButton) return false
        if (nextButton != other.nextButton) return false

        return true
    }

    override fun hashCode(): Int {
        var result = buttonsBar.contentHashCode()
        result = 31 * result + currentPage
        result = 31 * result + (firstButton?.hashCode() ?: 0)
        result = 31 * result + (lastButton?.hashCode() ?: 0)
        result = 31 * result + (previousButton?.hashCode() ?: 0)
        result = 31 * result + (nextButton?.hashCode() ?: 0)
        return result
    }
}

data class PaginationButton(
    val url: String,
    val text: String,
    val isCurrent: Boolean = false,
    val isEnabled: Boolean = true
    //, val visible: Boolean = true
)