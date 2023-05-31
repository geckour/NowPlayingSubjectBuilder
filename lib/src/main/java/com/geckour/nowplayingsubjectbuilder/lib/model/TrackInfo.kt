package com.geckour.nowplayingsubjectbuilder.lib.model

import com.geckour.nowplayingsubjectbuilder.lib.util.getContainedPatterns
import com.geckour.nowplayingsubjectbuilder.lib.util.splitConsideringEscape
import com.geckour.nowplayingsubjectbuilder.lib.util.withModifiers
import kotlinx.serialization.Serializable

@Serializable
data class TrackInfo(
    val formatPatterns: List<FormatPattern>,
) {

    fun isSatisfiedSpecifier(sharingFormatText: String): Boolean =
        sharingFormatText.isEmpty() || sharingFormatText.getContainedPatterns(formatPatterns).let {
            it.isNotEmpty() && it.all { containedPattern ->
                formatPatterns.map { it.key }.contains(containedPattern.key)
            }
        }

    fun getSharingSubject(
        sharingFormatText: String,
        modifiers: List<FormatPatternModifier> = emptyList(),
        requireMatchAllPattern: Boolean = false
    ): String? {
        if (requireMatchAllPattern && isSatisfiedSpecifier(sharingFormatText).not()) return null

        return sharingFormatText.splitConsideringEscape(formatPatterns).joinToString("") {
            val regex = Regex("^'([\\s\\S]+)'$")
            return@joinToString when {
                it.matches(regex) -> {
                    it.replace(regex, "$1")
                }

                it == "'" -> {
                    ""
                }

                it == "''" -> {
                    "'"
                }

                formatPatterns.map { it.key }.contains(it) -> {
                    formatPatterns
                        .first { pattern -> pattern.key == it }
                        .let { pattern -> pattern.value?.withModifiers(modifiers, pattern).orEmpty() }
                }

                it == "\\n" -> {
                    "\n"
                }

                else -> {
                    it
                }
            }
        }
    }
}