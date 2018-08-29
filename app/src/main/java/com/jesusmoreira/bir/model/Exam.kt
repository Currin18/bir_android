package com.jesusmoreira.bir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Exam (var year: String = "", var questions: Array<Question> = arrayOf()): Parcelable {

    constructor(jsonArray: JSONArray = JSONArray(), year: String = "") : this(year) {
        val arrayList: ArrayList<Question> = ArrayList()
        if (jsonArray.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                arrayList.add(Question(jsonArray.get(i) as JSONObject))
            }
            questions = arrayList.toTypedArray()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exam

        if (year != other.year) return false
        if (!Arrays.equals(questions, other.questions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = year.hashCode()
        result = 31 * result + Arrays.hashCode(questions)
        return result
    }
}