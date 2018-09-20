package com.jesusmoreira.bir.utils

import android.text.Html
import android.text.Spanned

class TextUtils {
    companion object {
        fun parseToHtml(text: String): Spanned? {
            var text = text
            text = text.replace(kotlin.text.Regex("~(.*)~"), "<strong>$1</strong>")
            text = text.replace(kotlin.text.Regex("\\^([a-zA-Z0-9]*)"), "<sup><small>$1</small></sup>")
            text = text.replace(kotlin.text.Regex("_([0-9]*)"), "<sub><small>$1</small></sub>")
            return Html.fromHtml(text, 0)
        }
    }
}