package com.geckour.nowplayingsubjectbuilder.lib.util

import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPattern
import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPatternModifier

fun String.getContainedPatterns(formatPatterns: List<FormatPattern>): List<FormatPattern> =
    this.splitConsideringEscape(formatPatterns).mapNotNull { delimiter ->
        formatPatterns.firstOrNull { it.key == delimiter }
    }

fun String.splitConsideringEscape(formatPatterns: List<FormatPattern>): List<String> =
    this.splitIncludeDelimiter("'", "''", *formatPatterns.map { it.key }.toTypedArray(), "\\\\n").let { splitList ->
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
): String = "${modifiers.getPrefix(identifier.key)}$this${modifiers.getSuffix(identifier.key)}"

private fun List<FormatPatternModifier>.getPrefix(value: String): String =
    this.firstOrNull { m -> m.key.key == value }?.prefix ?: ""

private fun List<FormatPatternModifier>.getSuffix(value: String): String =
    this.firstOrNull { m -> m.key.key == value }?.suffix ?: ""