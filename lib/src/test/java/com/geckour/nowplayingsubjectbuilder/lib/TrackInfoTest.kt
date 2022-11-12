package com.geckour.nowplayingsubjectbuilder.lib

import com.geckour.nowplayingsubjectbuilder.lib.model.FormatPattern
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
        null
    )
    private val fullInfo = TrackInfo(
        "hoge",
        "fuga",
        "piyo",
        "nyan",
        "wan",
        "pao",
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

        assertThat(actual).isEqualTo("hogefugapiyonyanpao\n'TI'")
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
}