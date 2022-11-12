package com.geckour.nowplayingsubjectbuilder.lib.model

import kotlinx.serialization.Serializable

@Serializable
data class FormatPatternModifier(
    val key: FormatPattern,
    val prefix: String = "",
    val suffix: String = ""
)