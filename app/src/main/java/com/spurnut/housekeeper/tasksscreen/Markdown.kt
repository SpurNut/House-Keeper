package com.spurnut.housekeeper.tasksscreen

import android.text.Spanned
import androidx.core.text.HtmlCompat
import org.markdown4j.Markdown4jProcessor

fun markdownHtmlFromText(input: String) : Spanned {
    val string = Markdown4jProcessor().process(input).dropLast(2)
    return HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY).dropLast(2) as Spanned
}