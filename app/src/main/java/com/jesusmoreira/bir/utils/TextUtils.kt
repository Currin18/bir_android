package com.jesusmoreira.bir.utils

import android.text.Html
import android.text.Spanned

class TextUtils {
    companion object {

        private const val strongPatter = "~(.*)~"
        private const val strongReplacement = "<i>$1</i>"
        private const val supPattern = "\\^([a-zA-Z0-9]*)"
        private const val supReplacement = "<sup><small>$1</small></sup>"
        private const val subPattern = "_([0-9]*)"
        private const val subReplacement = "<sub><small>$1</small></sub>"

        fun parseToHtml(str: String): Spanned? {
            var text = str
            text = text.replace(kotlin.text.Regex(strongPatter), strongReplacement)
            text = text.replace(kotlin.text.Regex(supPattern), supReplacement)
            text = text.replace(kotlin.text.Regex(subPattern), subReplacement)
            return Html.fromHtml(text, 0)
        }

        private const val strSeparator: String = "__,__"

        fun convertArrayToString(array: Array<String>?): String? {
            var str = ""
            if (array != null) {
                for (i in 0 until array.size) {
                    str += array[i]
                    // Do not append comma at the end of last element
                    if (i < array.size - 1) {
                        str += strSeparator
                    }
                }
            }
            return str
        }

        fun convertStringToArray(str: String): Array<String> {
            val arr = str.split(strSeparator)
            return arr.toTypedArray()
        }

        fun convertArrayOfIntToString(array: Array<Int>?): String? {
            var str = ""
            if (array != null) {
                for (i in 0 until array.size) {
                    str += array[i].toString()
                    // Do not append comma at the end of last element
                    if (i < array.size - 1) {
                        str += strSeparator
                    }
                }
            }
            return str
        }

        fun convertStringToArrayOfInt(str: String): Array<Int> {
            val arr = str.split(strSeparator).map { it.toInt() }
            return arr.toTypedArray()
        }
    }
}