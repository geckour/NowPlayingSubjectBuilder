package com.geckour.nowplayingsubjectbuilder.lib.model

enum class FormatPattern(val value: String) {
    S_QUOTE("'"),
    S_QUOTE_DOUBLE("''"),
    TITLE("TI"),
    ARTIST("AR"),
    ALBUM("AL"),
    COMPOSER("CO"),
    SPOTIFY_URL("SU"),
    NEW_LINE("\\n");

    companion object {
        val replaceablePatterns: List<FormatPattern> = values().filter {
            it !in listOf(S_QUOTE, S_QUOTE_DOUBLE, NEW_LINE)
        }
    }
}