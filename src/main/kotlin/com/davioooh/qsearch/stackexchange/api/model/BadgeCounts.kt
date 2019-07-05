package com.davioooh.qsearch.stackexchange.api.model

import com.fasterxml.jackson.annotation.JsonProperty

data class BadgeCounts(
    @JsonProperty("bronze") var bronze: Int? = null,
    @JsonProperty("silver") var silver: Int? = null,
    @JsonProperty("gold") var gold: Int? = null
)