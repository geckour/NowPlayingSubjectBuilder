package com.geckour.nowplayingsubjectbuilder.lib

import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPattern
import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPatternModifier
import com.geckour.nowplayingsubjectbuilder.lib.model.TrackInfo
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TrackInfoTest {

    private val emptyInfo = TrackInfo(
        null,
        null,
        null,
        null,
        null,
        null,
    )
    private val fullInfo = TrackInfo(
        "hoge",
        "fuga",
        "piyo",
        "nyan",
        "wan",
        "pao"
    )
    private val containingEscapeInfo = TrackInfo(
        "Don't mind baby",
        "Rei, 長岡亮介",
        "QUILT",
        null,
        "https://open.spotify.com/track/4NXFsWNZRd227EJL76rb16",
        "https://music.youtube.com/watch?v=RbH_B-YPtbk",
    )

    @Test
    fun `isSatisfiedSpecifier returns false when given not empty pattern with empty TrackInfo`() {
        val actual = emptyInfo.isSatisfiedSpecifier(FormatPattern.TITLE.value)
        assertThat(actual).isFalse()
    }

    @Test
    fun `isSatisfiedSpecifier returns true when given empty pattern with empty TrackInfo`() {
        val actual = emptyInfo.isSatisfiedSpecifier("")
        assertThat(actual).isTrue()
    }

    @Test
    fun `isSatisfiedSpecifier returns true when given not empty pattern with full TrackInfo`() {
        val actual = fullInfo.isSatisfiedSpecifier(FormatPattern.TITLE.value)
        assertThat(actual).isTrue()
    }

    @Test
    fun `isSatisfiedSpecifier returns true when given empty pattern with full TrackInfo`() {
        val actual = fullInfo.isSatisfiedSpecifier("")
        assertThat(actual).isTrue()
    }

    @Test
    fun `isSatisfiedSpecifier returns false when given not empty pattern with not empty but contains no match parameter TrackInfo`() {
        val info = emptyInfo.copy(artist = "hoge")
        val actual = info.isSatisfiedSpecifier(FormatPattern.TITLE.value)
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
            FormatPatternModifier(FormatPattern.TITLE, prefix = " ", suffix = ""),
            FormatPatternModifier(FormatPattern.ARTIST, prefix = " - ", suffix = ""),
            FormatPatternModifier(FormatPattern.ALBUM, prefix = " (", suffix = ")"),
            FormatPatternModifier(FormatPattern.COMPOSER, prefix = " written by ", suffix = ""),
            FormatPatternModifier(FormatPattern.SPOTIFY_URL, prefix = "\n", suffix = ""),
            FormatPatternModifier(FormatPattern.YOUTUBE_MUSIC_URL, prefix = "\nYouTube Music: ", suffix = ""),
        )
        val actual = containingEscapeInfo.getSharingSubject(formatString, modifiers)

        assertThat(actual).isEqualTo("\nNowPlaying Don't mind baby - Rei, 長岡亮介 (QUILT)\nhttps://open.spotify.com/track/4NXFsWNZRd227EJL76rb16\nYouTube Music: https://music.youtube.com/watch?v=RbH_B-YPtbk")
    }
}