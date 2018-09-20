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
        var id: String? = "",
        var statement: String? = "",
        var answers: Array<String>? = arrayOf(),
        var tags: Array<String>? = arrayOf(),
        var correctAnswer: Int = 0,
        var impugned: Boolean = false
): Parcelable {

    companion object {
        private const val JSON_ID = "id"
        private const val JSON_STATEMENT = "statement"
        private const val JSON_ANSWERS = "answers"
        private const val JSON_TAGS = "tags"
        private const val JSON_CORRECT_ANSWER = "correct-answer"
        private const val JSON_IMPUGNED = "impugned"
    }

    var selectedAnswer: Int? = null

    constructor(json: JSONObject = JSONObject()) : this("") {
        try {
            if (json.has(JSON_ID)) id = json.getString(JSON_ID)
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
            // FIXME: fix json files with correct values in correct-answer
            if (json.has(JSON_CORRECT_ANSWER)) correctAnswer = (json.getInt(JSON_CORRECT_ANSWER) - 1)
            if (json.has(JSON_IMPUGNED)) impugned = json.getBoolean(JSON_IMPUGNED)
        } catch (e : Throwable) {
            e.printStackTrace()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (id != other.id) return false
        if (statement != other.statement) return false
        if (!Arrays.equals(answers, other.answers)) return false
        if (!Arrays.equals(tags, other.tags)) return false
        if (correctAnswer != other.correctAnswer) return false
        if (impugned != other.impugned) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (statement?.hashCode() ?: 0)
        result = 31 * result + (answers?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + (tags?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + correctAnswer
        result = 31 * result + impugned.hashCode()
        result = 31 * result + JSON_ID.hashCode()
        result = 31 * result + JSON_STATEMENT.hashCode()
        result = 31 * result + JSON_ANSWERS.hashCode()
        result = 31 * result + JSON_TAGS.hashCode()
        result = 31 * result + JSON_CORRECT_ANSWER.hashCode()
        result = 31 * result + JSON_IMPUGNED.hashCode()
        return result
    }
}