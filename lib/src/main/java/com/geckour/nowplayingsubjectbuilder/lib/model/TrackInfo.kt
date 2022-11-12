package com.geckour.nowplayingsubjectbuilder.lib.model

import com.geckour.nowplayingsubjectbuilder.lib.util.containedPatterns
import com.geckour.nowplayingsubjectbuilder.lib.util.getReplacerWithModifier
import com.geckour.nowplayingsubjectbuilder.lib.util.splitConsideringEscape
import kotlinx.serialization.Serializable

@Serializable
data class TrackInfo(
    val title: String?,
    val artist: String?,
    val album: String?,
    val composer: String?,
    val artworkUriString: String?,
    val playerPackageName: String?,
    val spotifyUrl: String?
) {

    private fun isSatisfiedSpecifier(sharingFormatText: String): Boolean =
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
            return@joinToString Regex("^'(.+)'$").let { regex ->
                if (it.matches(regex)) it.replace(regex, "$1")
                else when (it) {
                    FormatPattern.S_QUOTE.value -> ""
                    FormatPattern.S_QUOTE_DOUBLE.value -> "'"
                    FormatPattern.TITLE.value -> title?.getReplacerWithModifier(
                        modifiers, it
                    ) ?: ""
                    FormatPattern.ARTIST.value -> artist?.getReplacerWithModifier(
                        modifiers, it
                    ) ?: ""
                    FormatPattern.ALBUM.value -> album?.getReplacerWithModifier(
                        modifiers, it
                    ) ?: ""
                    FormatPattern.COMPOSER.value -> composer?.getReplacerWithModifier(
                        modifiers, it
                    ) ?: ""
                    FormatPattern.SPOTIFY_URL.value -> spotifyUrl?.getReplacerWithModifier(
                        modifiers, it
                    ) ?: ""
                    FormatPattern.NEW_LINE.value -> "\n"
                    else -> it
                }
            }
        }
    }
}