package com.geckour.nowplayingsubjectbuilder.lib.util

import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPattern
import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPatternModifier
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringUtilTest {

    val formatPatterns = listOf(
        FormatPattern(key = "TI", value = ""),
        FormatPattern(key = "AR", value = ""),
        FormatPattern(key = "AL", value = ""),
        FormatPattern(key = "CO", value = ""),
        FormatPattern(key = "SU", value = ""),
        FormatPattern(key = "YU", value = ""),
        FormatPattern(key = "AU", value = ""),
        FormatPattern(key = "PN", value = ""),
    )

    @Test
    fun `containedPattern test 1`() {
        val formatString = "TIARALCOSUYUAUPN\\n'''TI'''"
        val actual = formatString.getContainedPatterns(formatPatterns)

        assertThat(actual).isEqualTo(
            listOf(
                formatPatterns[0],
                formatPatterns[1],
                formatPatterns[2],
                formatPatterns[3],
                formatPatterns[4],
                formatPatterns[5],
                formatPatterns[6],
                formatPatterns[7],
            )
        )
    }

    @Test
    fun `containedPattern test 2`() {
        val formatString = "TIItiAL\n'''TI"
        val actual = formatString.getContainedPatterns(formatPatterns)

        assertThat(actual).isEqualTo(
            listOf(
                formatPatterns[0],
                formatPatterns[2],
                formatPatterns[0],
            )
        )
    }

    @Test
    fun `containedPattern test 3`() {
        val formatString = "'TIItiAL\n'''TI"
        val actual = formatString.getContainedPatterns(formatPatterns)

        assertThat(actual).isEqualTo(listOf(formatPatterns[0]))
    }

    @Test
    fun `withModifiers test 1`() {
        val actual = "Hoge".withModifiers(
            listOf(FormatPatternModifier(formatPatterns[0], "Fuga", "Piyo")),
            formatPatterns[0]
        )

        assertThat(actual).isEqualTo("FugaHogePiyo")
    }

    @Test
    fun `withModifiers test 2`() {
        val actual = "Hoge".withModifiers(
            listOf(FormatPatternModifier(formatPatterns[0], "Fuga", "Piyo")),
            formatPatterns[1]
        )

        assertThat(actual).isEqualTo("Hoge")
    }

    @Test
    fun `withModifiers test 3`() {
        val actual = "Hoge".withModifiers(
            listOf(
                FormatPatternModifier(formatPatterns[0], "Fuga", "Piyo"),
                FormatPatternModifier(formatPatterns[1], "Nyan", "Wan"),
            ),
            formatPatterns[1]
        )

        assertThat(actual).isEqualTo("NyanHogeWan")
    }

    @Test
    fun `withModifiers test 4`() {
        val actual = "Hoge".withModifiers(
            listOf(
                FormatPatternModifier(formatPatterns[0], "Fuga", "Piyo"),
                FormatPatternModifier(formatPatterns[1], "Nyan", "Wan"),
                FormatPatternModifier(formatPatterns[0], "Pao", "Gao"),
            ),
            formatPatterns[0]
        )

        assertThat(actual).isEqualTo("FugaHogePiyo")
    }

    @Test
    fun `withModifiers test 5`() {
        val actual = "Hoge".withModifiers(
            listOf(FormatPatternModifier(formatPatterns[0], suffix = "Piyo")),
            formatPatterns[0]
        )

        assertThat(actual).isEqualTo("HogePiyo")
    }

    @Test
    fun `withModifiers test 6`() {
        val actual = "Hoge".withModifiers(
            listOf(FormatPatternModifier(formatPatterns[0], prefix = "Fuga")),
            formatPatterns[0]
        )

        assertThat(actual).isEqualTo("FugaHoge")
    }

    @Test
    fun `withModifiers test 7`() {
        val actual = "".withModifiers(
            listOf(FormatPatternModifier(formatPatterns[0], "Fuga", "Piyo")),
            formatPatterns[0]
        )

        assertThat(actual).isEqualTo("FugaPiyo")
    }

    @Test
    fun `splitConsideringEscape test 1`() {
        val formatString = "TIARALCOSU\\n'''TI'''"
        val actual = formatString.splitConsideringEscape(formatPatterns)

        assertThat(actual).isEqualTo(
            listOf(
                "TI",
                "AR",
                "AL",
                "CO",
                "SU",
                "\\n",
                "''",
                "'TI'",
                "''",
            )
        )
    }

    @Test
    fun `splitConsideringEscape test 2`() {
        val formatString = "TIItiAL\n'''TI"
        val actual = formatString.splitConsideringEscape(formatPatterns)

        assertThat(actual).isEqualTo(
            listOf(
                "TI",
                "Iti",
                "AL",
                "\n",
                "''",
                "TI",
            )
        )
    }

    @Test
    fun `splitConsideringEscape test 3`() {
        val formatString = "'TIItiAL\n'''TI"
        val actual = formatString.splitConsideringEscape(formatPatterns)

        assertThat(actual).isEqualTo(
            listOf(
                "'TIItiAL\n'",
                "''",
                "TI",
            )
        )
    }
}