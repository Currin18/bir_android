package com.jesusmoreira.bir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Question(
        var ref: String = "",
        var statement: String = "",
        var answers: Array<String> = arrayOf(),
        var tags: Array<String> = arrayOf(),
        var correctAnswer: Int = 0,
        var impugned: Boolean = false,
        var id: Int? = null,
        var year: Int = 0,
        var number: Int = 0
): Parcelable {

    companion object {
        private const val JSON_ID = "id"
        private const val JSON_STATEMENT = "statement"
        private const val JSON_ANSWERS = "answers"
        private const val JSON_TAGS = "tags"
        private const val JSON_CORRECT_ANSWER = "correct-answer"
        private const val JSON_IMPUGNED = "impugned"
    }

    constructor(json: JSONObject) : this() {
        try {
            if (json.has(JSON_ID)) {
                ref = json.getString(JSON_ID)
                ref.split('-').let {
                    if (it.size > 1) {
                        year = it[0].toInt()
                        number = it[1].toInt()
                    }
                }
            }
            if (json.has(JSON_STATEMENT)) statement = json.getString(JSON_STATEMENT)
            if (json.has(JSON_ANSWERS)) {
                val jsonArray = when (json.get(JSON_ANSWERS)) {
                    is JSONArray -> json.getJSONArray(JSON_ANSWERS)
                    else -> JSONArray(json.getString(JSON_ANSWERS))
                }
                if (jsonArray.length() > 0) {
                    val arrayList = ArrayList<String>()
                    for (i in 0 until jsonArray.length()) {
                        arrayList.add(jsonArray.get(i) as String)
                    }
                    answers = arrayList.toTypedArray()
                }
            }
            if (json.has(JSON_TAGS)) {
                val jsonArray = when (json.get(JSON_TAGS)) {
                    is JSONArray -> json.getJSONArray(JSON_TAGS)
                    else -> JSONArray(json.getString(JSON_TAGS))
                }
                if (jsonArray.length() > 0) {
                    val arrayList = ArrayList<String>()
                    for (i in 0 until jsonArray.length()) {
                        arrayList.add(jsonArray.get(i) as String)
                    }
                    tags = arrayList.toTypedArray()
                }
            }
            if (json.has(JSON_CORRECT_ANSWER)) correctAnswer = (json.getInt(JSON_CORRECT_ANSWER) - 1)
            if (json.has(JSON_IMPUGNED)) impugned = json.getBoolean(JSON_IMPUGNED)
        } catch (e : Throwable) {
            e.printStackTrace()
        }
    }

    fun toJson(): JSONObject {
        val json = JSONObject()

        json.put(JSON_ID, id)
        json.put(JSON_STATEMENT, statement)
        json.put(JSON_CORRECT_ANSWER, correctAnswer)
        json.put(JSON_IMPUGNED, impugned)

        val jsonAnswers = JSONArray()
        answers.forEach { jsonAnswers.put(it) }
        json.put(JSON_ANSWERS, jsonAnswers)

        val jsonTags = JSONArray()
        tags.forEach { jsonTags.put(it) }
        json.put(JSON_TAGS, jsonTags)

        return json
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (ref != other.ref) return false
        if (statement != other.statement) return false
        if (!answers.contentEquals(other.answers)) return false
        if (!tags.contentEquals(other.tags)) return false
        if (correctAnswer != other.correctAnswer) return false
        if (impugned != other.impugned) return false
        if (id != other.id) return false
        if (year != other.year) return false
        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ref.hashCode()
        result = 31 * result + statement.hashCode()
        result = 31 * result + answers.contentHashCode()
        result = 31 * result + tags.contentHashCode()
        result = 31 * result + correctAnswer
        result = 31 * result + impugned.hashCode()
        result = 31 * result + (id ?: 0)
        result = 31 * result + year
        result = 31 * result + number
        return result
    }
}