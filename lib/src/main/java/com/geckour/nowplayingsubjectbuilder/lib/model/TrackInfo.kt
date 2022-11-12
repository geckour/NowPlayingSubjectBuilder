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
    val artworkUriString: String?,
    val spotifyUrl: String?
) {

    internal fun isSatisfiedSpecifier(sharingFormatText: String): Boolean =
        sharingFormatText.containedPatterns.all {
            when (it) {
                FormatPattern.TITLE -> this.title != null
                FormatPattern.ARTIST -> this.artist != null
                FormatPattern.ALBUM -> this.album != null
                FormatPattern.COMPOSER -> this.composer != null
                FormatPattern.SPOTIFY_URL -> this.spotifyUrl != null
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
                FormatPattern.TITLE.value -> title?.withModifiers(modifiers, FormatPattern.TITLE) ?: ""
                FormatPattern.ARTIST.value -> artist?.withModifiers(modifiers, FormatPattern.ARTIST) ?: ""
                FormatPattern.ALBUM.value -> album?.withModifiers(modifiers, FormatPattern.ALBUM) ?: ""
                FormatPattern.COMPOSER.value -> composer?.withModifiers(modifiers, FormatPattern.COMPOSER) ?: ""
                FormatPattern.SPOTIFY_URL.value -> spotifyUrl?.withModifiers(modifiers, FormatPattern.SPOTIFY_URL) ?: ""
                FormatPattern.NEW_LINE.value -> "\n"
                else -> it
            }
        }
    }
}