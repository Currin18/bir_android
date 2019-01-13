package com.jesusmoreira.bir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject

@Parcelize
data class Filters(
        var random: Boolean = false,
        var years: Array<Int> = arrayOf(),
        var categories: Array<String> = arrayOf(),
        var words: Array<String> = arrayOf(),
        var includeAnswers: Boolean = false
): Parcelable {
    companion object {
        private const val JSON_RANDOM = "random"
        private const val JSON_YEARS = "years"
        private const val JSON_CATEGORIES = "categories"
        private const val JSON_WORDS = "words"
        private const val JSON_INCLUDE_ANSWERS = "includeAnswers"
    }

    constructor(json: JSONObject): this() {
        if (json.has(JSON_RANDOM)) random = json.getBoolean(JSON_RANDOM)
        if (json.has(JSON_YEARS)) {
            json.getJSONArray(JSON_YEARS).let {
                val years = mutableListOf<Int>()
                for (i in 0 until it.length()) {
                    years.add(it.getInt(i))
                }
                this.years = years.toTypedArray()
            }
        }
        if (json.has(JSON_CATEGORIES)) {
            json.getJSONArray(JSON_CATEGORIES).let {
                val categories = mutableListOf<String>()
                for (i in 0 until it.length()) {
                    categories.add(it.getString(i))
                }
                this.categories = categories.toTypedArray()
            }
        }
        if (json.has(JSON_WORDS)) {
            json.getJSONArray(JSON_WORDS).let {
                val words = mutableListOf<String>()
                for (i in 0 until it.length()) {
                    words.add(it.getString(i))
                }
                this.words = words.toTypedArray()
            }
        }
        if (json.has(JSON_INCLUDE_ANSWERS)) includeAnswers = json.getBoolean(JSON_INCLUDE_ANSWERS)
    }

    override fun toString(): String {
        val json = JSONObject()

        json.put(JSON_RANDOM, random)

        JSONArray().let { jsonArray ->
            years.forEach { jsonArray.put(it) }
            json.put(JSON_YEARS, jsonArray)
        }

        JSONArray().let { jsonArray ->
            categories.forEach { jsonArray.put(it) }
            json.put(JSON_CATEGORIES, jsonArray)
        }

        JSONArray().let { jsonArray ->
            words.forEach { jsonArray.put(it) }
            json.put(JSON_WORDS, jsonArray)
        }

        json.put(JSON_INCLUDE_ANSWERS, includeAnswers)

        return json.toString()
    }

    fun countFilters(): Int {
        var count = 0
        if (random) count++
        if (years.isNotEmpty()) count++
        if (categories.isNotEmpty()) count++
        if (words.isNotEmpty()) count++
        return count
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Filters

        if (random != other.random) return false
        if (!years.contentEquals(other.years)) return false
        if (!categories.contentEquals(other.categories)) return false
        if (!words.contentEquals(other.words)) return false
        if (includeAnswers != other.includeAnswers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = random.hashCode()
        result = 31 * result + years.contentHashCode()
        result = 31 * result + categories.contentHashCode()
        result = 31 * result + words.contentHashCode()
        result = 31 * result + includeAnswers.hashCode()
        return result
    }
}