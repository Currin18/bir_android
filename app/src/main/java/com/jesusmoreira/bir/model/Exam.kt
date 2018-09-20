package com.jesusmoreira.bir.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Exam (var year: String = "", var questions: Array<Question> = arrayOf()): Parcelable {

    var questionPosition = -1
    var questionCount = 0
    var questionsSuccess = 0
    var questionsError = 0
    var questionsPassed = 0

    var rand = false

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

    fun nextQuestion(): Question? {
        if (rand) {
            val questionsRemaining = questions.filter { it.selectedAnswer == null }
            questionPosition = if (questionsRemaining.isNotEmpty()) {
                val randPosition = Random().nextInt(questionsRemaining.size)
                questions.indexOf(questionsRemaining[randPosition])
            } else { -1 }
        } else {
            questionPosition++
        }

        if (questionPosition >= 0 && questionPosition < questions.size) {
            questionCount++
            return questions[questionPosition]
        }
        return null
    }

    fun setOnClickAnswer(position: Int): Boolean {
        if (questionPosition >= 0 && questionPosition < questions.size) {
            questions[questionPosition].selectedAnswer = position
            return true
        }
        return false
    }

    fun setLetPassInteraction() {
        if (questionPosition >= 0 && questionPosition < questions.size)
            questions[questionPosition].selectedAnswer = -1
    }

    fun setContinueInteraction() {
        if (questionPosition >= 0 && questionPosition < questions.size && questions[questionPosition].impugned)
            questions[questionPosition].selectedAnswer = -2
    }
}