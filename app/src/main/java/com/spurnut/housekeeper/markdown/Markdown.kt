package com.spurnut.housekeeper.markdown

import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned
import org.markdown4j.Markdown4jProcessor

fun markdownHtmlFromText(input: String) : Spanned {
    val string = Markdown4jProcessor().process(input).dropLast(2)
    val spanned = HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_COMPACT)
    return spanned.toSpanned()
}