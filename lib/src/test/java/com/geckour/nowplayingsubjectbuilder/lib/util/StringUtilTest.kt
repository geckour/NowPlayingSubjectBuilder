package com.geckour.nowplayingsubjectbuilder.lib.util

import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPattern
import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPatternModifier
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringUtilTest {

    @Test
    fun `containedPattern test 1`() {
        val formatString = "TIARALCOSUYU\\n'''TI'''"
        val actual = formatString.containedPatterns

        assertThat(actual).isEqualTo(
            listOf(
                FormatPattern.TITLE,
                FormatPattern.ARTIST,
                FormatPattern.ALBUM,
                FormatPattern.COMPOSER,
                FormatPattern.SPOTIFY_URL,
                FormatPattern.YOUTUBE_MUSIC_URL,
                FormatPattern.NEW_LINE,
                FormatPattern.S_QUOTE_DOUBLE,
                FormatPattern.S_QUOTE_DOUBLE,
            )
        )
    }

    @Test
    fun `containedPattern test 2`() {
        val formatString = "TIItiAL\n'''TI"
        val actual = formatString.containedPatterns

        assertThat(actual).isEqualTo(
            listOf(
                FormatPattern.TITLE,
                FormatPattern.ALBUM,
                FormatPattern.S_QUOTE_DOUBLE,
                FormatPattern.TITLE,
            )
        )
    }

    @Test
    fun `containedPattern test 3`() {
        val formatString = "'TIItiAL\n'''TI"
        val actual = formatString.containedPatterns

        assertThat(actual).isEqualTo(
            listOf(
                FormatPattern.S_QUOTE_DOUBLE,
                FormatPattern.TITLE,
            )
        )
    }

    @Test
    fun `withModifiers test 1`() {
        val actual = "Hoge".withModifiers(listOf(FormatPatternModifier(FormatPattern.TITLE, "Fuga", "Piyo")), FormatPattern.TITLE)

        assertThat(actual).isEqualTo("FugaHogePiyo")
    }

    @Test
    fun `withModifiers test 2`() {
        val actual = "Hoge".withModifiers(listOf(FormatPatternModifier(FormatPattern.TITLE, "Fuga", "Piyo")), FormatPattern.ARTIST)

        assertThat(actual).isEqualTo("Hoge")
    }

    @Test
    fun `withModifiers test 3`() {
        val actual = "Hoge".withModifiers(
            listOf(
                FormatPatternModifier(FormatPattern.TITLE, "Fuga", "Piyo"),
                FormatPatternModifier(FormatPattern.ARTIST, "Nyan", "Wan"),
            ),
            FormatPattern.ARTIST
        )

        assertThat(actual).isEqualTo("NyanHogeWan")
    }

    @Test
    fun `withModifiers test 4`() {
        val actual = "Hoge".withModifiers(
            listOf(
                FormatPatternModifier(FormatPattern.TITLE, "Fuga", "Piyo"),
                FormatPatternModifier(FormatPattern.ARTIST, "Nyan", "Wan"),
                FormatPatternModifier(FormatPattern.TITLE, "Pao", "Gao"),
            ),
            FormatPattern.TITLE
        )

        assertThat(actual).isEqualTo("FugaHogePiyo")
    }

    @Test
    fun `withModifiers test 5`() {
        val actual = "Hoge".withModifiers(listOf(FormatPatternModifier(FormatPattern.TITLE, suffix = "Piyo")), FormatPattern.TITLE)

        assertThat(actual).isEqualTo("HogePiyo")
    }

    @Test
    fun `withModifiers test 6`() {
        val actual = "Hoge".withModifiers(listOf(FormatPatternModifier(FormatPattern.TITLE, prefix = "Fuga")), FormatPattern.TITLE)

        assertThat(actual).isEqualTo("FugaHoge")
    }

    @Test
    fun `withModifiers test 7`() {
        val actual = "".withModifiers(listOf(FormatPatternModifier(FormatPattern.TITLE, "Fuga", "Piyo")), FormatPattern.TITLE)

        assertThat(actual).isEqualTo("FugaPiyo")
    }

    @Test
    fun `splitConsideringEscape test 1`() {
        val formatString = "TIARALCOSU\\n'''TI'''"
        val actual = formatString.splitConsideringEscape()

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
        val actual = formatString.splitConsideringEscape()

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
        val actual = formatString.splitConsideringEscape()

        assertThat(actual).isEqualTo(
            listOf(
                "'TIItiAL\n'",
                "''",
                "TI",
            )
        )
    }
}