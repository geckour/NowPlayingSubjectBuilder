package com.geckour.nowplayingsubjectbuilder.lib.model

import kotlinx.serialization.Serializable

@Serializable
data class FormatPattern(
    val key: String,
    val value: String?,
)