package com.jesusmoreira.bir.model

import org.json.JSONArray
import org.json.JSONObject

data class Exam (
        var year: String = "",
        var version: String = "",
        var questions: ArrayList<Question> = ArrayList()
) {
    constructor(jsonArray: JSONArray = JSONArray(), year: String = "", version: String = "") : this(year, version) {
        if (jsonArray.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                questions.add(Question(jsonArray.get(i) as JSONObject))
            }
        }
    }
}