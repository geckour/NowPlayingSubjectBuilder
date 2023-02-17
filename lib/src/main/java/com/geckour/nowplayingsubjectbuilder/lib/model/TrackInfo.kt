package com.geckour.nowplayingsubjectbuilder.lib.model

import com.geckour.nowplayingsubjectbuilder.lib.util.containedPatterns
import com.geckour.nowplayingsubjectbuilder.lib.util.splitConsideringEscape
import com.geckour.nowplayingsubjectbuilder.lib.util.withModifiers
import kotlinx.serialization.Serializable

@Serializable
data class TrackInfo(
    val title: String?,
    val artist: String?,
    val album: String?,
    val composer: String?,
    val spotifyUrl: String?,
    val youTubeMusicUrl: String?
) {

    fun isSatisfiedSpecifier(sharingFormatText: String): Boolean =
        sharingFormatText.containedPatterns.all {
            when (it) {
                FormatPattern.TITLE -> this.title != null
                FormatPattern.ARTIST -> this.artist != null
                FormatPattern.ALBUM -> this.album != null
                FormatPattern.COMPOSER -> this.composer != null
                FormatPattern.SPOTIFY_URL -> this.spotifyUrl != null
                FormatPattern.YOUTUBE_MUSIC_URL -> this.youTubeMusicUrl != null
                else -> true
            }
        }

    fun getSharingSubject(
        sharingFormatText: String,
        modifiers: List<FormatPatternModifier> = emptyList(),
        requireMatchAllPattern: Boolean = false
    ): String? {
        if (requireMatchAllPattern && isSatisfiedSpecifier(sharingFormatText).not()) return null

        return sharingFormatText.splitConsideringEscape().joinToString("") {
            val regex = Regex("^'([\\s\\S]+)'$")
            return@joinToString if (it.matches(regex)) it.replace(regex, "$1") else when (it) {
                FormatPattern.S_QUOTE.value -> ""
                FormatPattern.S_QUOTE_DOUBLE.value -> "'"
                FormatPattern.TITLE.value -> title?.withModifiers(modifiers, FormatPattern.TITLE).orEmpty()
                FormatPattern.ARTIST.value -> artist?.withModifiers(modifiers, FormatPattern.ARTIST).orEmpty()
                FormatPattern.ALBUM.value -> album?.withModifiers(modifiers, FormatPattern.ALBUM).orEmpty()
                FormatPattern.COMPOSER.value -> composer?.withModifiers(modifiers, FormatPattern.COMPOSER).orEmpty()
                FormatPattern.SPOTIFY_URL.value -> spotifyUrl?.withModifiers(modifiers, FormatPattern.SPOTIFY_URL).orEmpty()
                FormatPattern.YOUTUBE_MUSIC_URL.value -> youTubeMusicUrl?.withModifiers(modifiers, FormatPattern.YOUTUBE_MUSIC_URL).orEmpty()
                FormatPattern.NEW_LINE.value -> "\n"
                else -> it
            }
        }
    }
}