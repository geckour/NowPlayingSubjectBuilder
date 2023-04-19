package com.geckour.nowplayingsubjectbuilder.lib.util

import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPattern
import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPatternModifier

val String.containedPatterns: List<FormatPattern>
    get() = this.splitConsideringEscape().mapNotNull { delimiter ->
        FormatPattern.values().firstOrNull { it.value == delimiter }
    }

fun String.splitConsideringEscape(): List<String> =
    this.splitIncludeDelimiter(
        FormatPattern.S_QUOTE_DOUBLE.value,
        FormatPattern.S_QUOTE.value,
        FormatPattern.TITLE.value,
        FormatPattern.ARTIST.value,
        FormatPattern.ALBUM.value,
        FormatPattern.COMPOSER.value,
        FormatPattern.SPOTIFY_URL.value,
        FormatPattern.YOUTUBE_MUSIC_URL.value,
        FormatPattern.APPLE_MUSIC_URL.value,
        FormatPattern.PIXEL_NOW_PLAYING.value,
        "\\\\n"
    ).let { splitList ->
        val escapes = splitList.mapIndexed { i, s -> Pair(i, s) }.filter { it.second == "'" }
            .apply { if (lastIndex < 0) return@let splitList }

        return@let mutableListOf<String>().apply {
            for (i in 0 until escapes.lastIndex step 2) {
                this.addAll(
                    splitList.subList(
                        if (i == 0) 0 else escapes[i - 1].first + 1, escapes[i].first
                    )
                )

                this.add(
                    splitList.subList(
                        escapes[i].first, escapes[i + 1].first + 1
                    ).joinToString("")
                )
            }

            this.addAll(
                splitList.subList(
                    if (escapes[escapes.lastIndex].first + 1 < splitList.lastIndex) escapes[escapes.lastIndex].first + 1
                    else splitList.lastIndex, splitList.size
                )
            )
        }.filter { it.isNotEmpty() }
    }

private fun String.splitIncludeDelimiter(vararg delimiters: String) =
    delimiters.joinToString("|").let { pattern ->
        this.split(Regex("(?<=$pattern)|(?=$pattern)"))
    }

fun String.withModifiers(
    modifiers: List<FormatPatternModifier>,
    identifier: FormatPattern
): String = "${modifiers.getPrefix(identifier.value)}$this${modifiers.getSuffix(identifier.value)}"

private fun List<FormatPatternModifier>.getPrefix(value: String): String =
    this.firstOrNull { m -> m.key.value == value }?.prefix ?: ""

private fun List<FormatPatternModifier>.getSuffix(value: String): String =
    this.firstOrNull { m -> m.key.value == value }?.suffix ?: ""