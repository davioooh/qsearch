package com.davioooh.stackoverflow.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

@JsonIgnoreProperties("expired", "expirationDate")
data class AccessTokenDetails(
    @JsonProperty("access_token") val token: String,
    @JsonProperty("expires") val expires: Long?,
    @JsonProperty("date") val date: Instant = Instant.now()
) {
    val isExpired: Boolean =
        if (expires == null) false else date.plusSeconds(expires) < Instant.now()

    val expirationDate: Instant? = expires?.let { date.plusSeconds(it) }
}