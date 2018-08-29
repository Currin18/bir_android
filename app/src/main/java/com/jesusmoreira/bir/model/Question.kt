package com.jesusmoreira.bir.model

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

data class Question(
        var id: String = "",
        var statement: String = "",
        var answers: ArrayList<String> = ArrayList(),
        var tags: ArrayList<String> = ArrayList(),
        var correctAnswer: Int = 0,
        var impugned: Boolean = false
) {
    private val JSON_ID = "id"
    private val JSON_STATEMENT = "statement"
    private val JSON_ANSWERS = "answers"
    private val JSON_TAGS = "tags"
    private val JSON_CORRECT_ANSWER = "correct_answer"
    private val JSON_IMPUGNED = "impugned"

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
                    for (i in 0 until jsonArray.length()) {
                        answers.add(jsonArray.get(i) as String)
                    }
                }
            }
            if (json.has(JSON_TAGS)) {
                val jsonArray = when (json.get(JSON_TAGS)) {
                    is JSONArray -> json.getJSONArray(JSON_TAGS)
                    else -> JSONArray(json.getString(JSON_TAGS))
                }
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        tags.add(jsonArray.get(i) as String)
                    }
                }
            }
            if (json.has(JSON_CORRECT_ANSWER)) correctAnswer = json.getInt(JSON_CORRECT_ANSWER)
            if (json.has(JSON_IMPUGNED)) impugned = json.getBoolean(JSON_IMPUGNED)
        } catch (e : JSONException) {
            e.printStackTrace()
        }
    }
}