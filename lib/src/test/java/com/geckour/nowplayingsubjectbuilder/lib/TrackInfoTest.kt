package com.geckour.nowplayingsubjectbuilder.lib

import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPattern
import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPatternModifier
import com.geckour.nowplayingsubjectbuilder.lib.model.TrackInfo
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TrackInfoTest {

    private val emptyInfo = TrackInfo(
        formatPatterns = emptyList()
    )
    private val fullInfo = TrackInfo(
        formatPatterns = listOf(
            FormatPattern(key = "TI", value = "hoge"),
            FormatPattern(key = "AR", value = "fuga"),
            FormatPattern(key = "AL", value = "piyo"),
            FormatPattern(key = "CO", value = "nyan"),
            FormatPattern(key = "SU", value = "wan"),
            FormatPattern(key = "YU", value = "pao"),
            FormatPattern(key = "AU", value = "pao"),
            FormatPattern(key = "PN", value = "pao"),
        )
    )
    private val containingEscapeInfo = TrackInfo(
        formatPatterns = listOf(
            FormatPattern(key = "TI", value = "Don't mind baby"),
            FormatPattern(key = "AR", value = "Rei, 長岡亮介"),
            FormatPattern(key = "AL", value = "QUILT"),
            FormatPattern(key = "CO", value = null),
            FormatPattern(key = "SU", value = "https://open.spotify.com/track/4NXFsWNZRd227EJL76rb16"),
            FormatPattern(key = "YU", value = "https://music.youtube.com/watch?v=RbH_B-YPtbk"),
            FormatPattern(key = "AU", value = null),
            FormatPattern(key = "PN", value = null),
        )
    )

    @Test
    fun `isSatisfiedSpecifier returns false when given not empty pattern with empty TrackInfo`() {
        val actual = emptyInfo.isSatisfiedSpecifier("TI")
        assertThat(actual).isFalse()
    }

    @Test
    fun `isSatisfiedSpecifier returns true when given empty pattern with empty TrackInfo`() {
        val actual = emptyInfo.isSatisfiedSpecifier("")
        assertThat(actual).isTrue()
    }

    @Test
    fun `isSatisfiedSpecifier returns true when given not empty pattern with full TrackInfo`() {
        val actual = fullInfo.isSatisfiedSpecifier("TI")
        assertThat(actual).isTrue()
    }

    @Test
    fun `isSatisfiedSpecifier returns true when given empty pattern with full TrackInfo`() {
        val actual = fullInfo.isSatisfiedSpecifier("")
        assertThat(actual).isTrue()
    }

    @Test
    fun `isSatisfiedSpecifier returns false when given not empty pattern with not empty but contains no match parameter TrackInfo`() {
        val info = TrackInfo(formatPatterns = listOf(FormatPattern(key = "AR", value = "hoge")))
        val actual = info.isSatisfiedSpecifier("TI")
        assertThat(actual).isFalse()
    }

    @Test
    fun `getSharingSubject test 1`() {
        val formatString = "TIARALCOSU\\n'''TI'''"
        val actual = fullInfo.getSharingSubject(formatString)

        assertThat(actual).isEqualTo("hogefugapiyonyanwan\n'TI'")
    }

    @Test
    fun `getSharingSubject test 2`() {
        val formatString = "TIItiAL\n'''TI"
        val actual = fullInfo.getSharingSubject(formatString)

        assertThat(actual).isEqualTo("hogeItipiyo\n'hoge")
    }

    @Test
    fun `getSharingSubject test 3`() {
        val formatString = "'TIItiAL\n'''TI"
        val actual = fullInfo.getSharingSubject(formatString)

        assertThat(actual).isEqualTo("TIItiAL\n'hoge")
    }

    @Test
    fun `getSharingSubject test 4`() {
        val formatString = "TIARALCOSU\\n'''TI'''"
        val actual = containingEscapeInfo.getSharingSubject(formatString)

        assertThat(actual).isEqualTo("Don't mind babyRei, 長岡亮介QUILThttps://open.spotify.com/track/4NXFsWNZRd227EJL76rb16\n'TI'")
    }

    @Test
    fun `getSharingSubject test 5`() {
        val formatString = "TIItiAR\n'''TI"
        val actual = containingEscapeInfo.getSharingSubject(formatString)

        assertThat(actual).isEqualTo("Don't mind babyItiRei, 長岡亮介\n'Don't mind baby")
    }

    @Test
    fun `getSharingSubject test 6`() {
        val formatString = "'TIItiAL\n'''TI"
        val actual = containingEscapeInfo.getSharingSubject(formatString)

        assertThat(actual).isEqualTo("TIItiAL\n'Don't mind baby")
    }

    @Test
    fun `getSharingSubject test 7`() {
        val formatString = "\nNowPlayingTIARALCOSUYU"
        val modifiers = listOf(
            FormatPatternModifier(containingEscapeInfo.formatPatterns[0], prefix = " ", suffix = ""),
            FormatPatternModifier(containingEscapeInfo.formatPatterns[1], prefix = " - ", suffix = ""),
            FormatPatternModifier(containingEscapeInfo.formatPatterns[2], prefix = " (", suffix = ")"),
            FormatPatternModifier(containingEscapeInfo.formatPatterns[3], prefix = " written by ", suffix = ""),
            FormatPatternModifier(containingEscapeInfo.formatPatterns[4], prefix = "\n", suffix = ""),
            FormatPatternModifier(containingEscapeInfo.formatPatterns[5], prefix = "\nYouTube Music: ", suffix = ""),
        )
        val actual = containingEscapeInfo.getSharingSubject(formatString, modifiers)

        assertThat(actual).isEqualTo("\nNowPlaying Don't mind baby - Rei, 長岡亮介 (QUILT)\nhttps://open.spotify.com/track/4NXFsWNZRd227EJL76rb16\nYouTube Music: https://music.youtube.com/watch?v=RbH_B-YPtbk")
    }
}