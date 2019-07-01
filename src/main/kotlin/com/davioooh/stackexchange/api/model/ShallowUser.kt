package com.davioooh.stackexchange.api.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ShallowUser(
    @JsonProperty("reputation") val reputation: Int? = null,
    @JsonProperty("user_id") val userId: Int? = null,
    @JsonProperty("user_type") val userType: String? = null,
    @JsonProperty("accept_rate") val acceptRate: Int? = null,
    @JsonProperty("profile_image") val profileImage: String? = null,
    @JsonProperty("display_name") val displayName: String? = null,
    @JsonProperty("link") val link: String? = null
)