package com.davioooh.stackexchange.api.model

import com.fasterxml.jackson.annotation.JsonProperty

data class User(
    @JsonProperty("badge_counts") var badgeCounts: BadgeCounts? = null,
    @JsonProperty("view_count") var viewCount: Int? = null,
    @JsonProperty("down_vote_count") var downVoteCount: Int? = null,
    @JsonProperty("up_vote_count") var upVoteCount: Int? = null,
    @JsonProperty("answer_count") var answerCount: Int? = null,
    @JsonProperty("question_count") var questionCount: Int? = null,
    @JsonProperty("account_id") var accountId: Int? = null,
    @JsonProperty("is_employee") var isEmployee: Boolean? = null,
    @JsonProperty("last_modified_date") var lastModifiedDate: Int? = null,
    @JsonProperty("last_access_date") var lastAccessDate: Int? = null,
    @JsonProperty("reputation_change_year") var reputationChangeYear: Int? = null,
    @JsonProperty("reputation_change_quarter") var reputationChangeQuarter: Int? = null,
    @JsonProperty("reputation_change_month") var reputationChangeMonth: Int? = null,
    @JsonProperty("reputation_change_week") var reputationChangeWeek: Int? = null,
    @JsonProperty("reputation_change_day") var reputationChangeDay: Int? = null,
    @JsonProperty("reputation") var reputation: Int? = null,
    @JsonProperty("creation_date") var creationDate: Int? = null,
    @JsonProperty("user_type") var userType: String? = null,
    @JsonProperty("user_id") var userId: Int,
    @JsonProperty("accept_rate") var acceptRate: Int? = null,
    @JsonProperty("about_me") var aboutMe: String? = null,
    @JsonProperty("location") var location: String? = null,
    @JsonProperty("website_url") var websiteUrl: String? = null,
    @JsonProperty("link") var link: String? = null,
    @JsonProperty("profile_image") var profileImage: String? = null,
    @JsonProperty("display_name") var displayName: String? = null
)