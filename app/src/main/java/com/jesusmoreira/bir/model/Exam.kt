package com.jesusmoreira.bir.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Exam (
        var filters: Filters = Filters(),
        var questions: List<Question> = listOf(),
        var selectedAnswers: Array<Int> = Array(questions.size) { i -> 0 },
        var id: Int? = null,
        var created: Long? = null,
        var finished: Long? = null,
        var countCorrect: Int = 0,
        var countErrors: Int = 0,
        var countImpugned: Int = 0
): Parcelable {

    companion object {
        enum class QuestionStatus {
            Default,
            Correct,
            Error,
            Passed
        }
    }

    var selectedAnswerStatus: Array<QuestionStatus> = arrayOf(QuestionStatus.Default)

    fun getPositionById(questionId: Int): Int {
        return questions.indexOfFirst { it.id == questionId }
    }

    fun getSelectedAnswer(questionId: Int): Int {
        return selectedAnswers[getPositionById(questionId)]
    }

    fun getQuestionStatus(position: Int) : QuestionStatus {
        if (position >= 0 && position < questions.size) {
            return when (selectedAnswers[position]) {
                0 -> QuestionStatus.Default
                -1 -> QuestionStatus.Passed
                questions[position].correctAnswer -> QuestionStatus.Correct
                else -> QuestionStatus.Error
            }
        }
        return QuestionStatus.Default
    }

    fun nextQuestion(questionId: Int?): Question? {
        if (questionId != null) {
            val position = questions.indexOfFirst { it.id == questionId } + 1
            return if (position >= 0 && position < questions.size) {
                questions[position]
            } else {
                null
            }
        }

        return questions[0]
    }

    fun setOnClickAnswer(questionId: Int, answerSelected: Int): Boolean {
        val position = questions.indexOfFirst { it.id == questionId }
        if (position >= 0 && position < questions.size) {
            selectedAnswers[position] = answerSelected
            getQuestionStatus(position).let {
                selectedAnswerStatus[position] = it
                when(it) {
                    QuestionStatus.Correct -> countCorrect++
                    QuestionStatus.Error -> countErrors++
                    else -> {}
                }
            }
            return true
        }
        return false
    }

    fun setLetPassInteraction(questionId: Int) {
        val position = questions.indexOfFirst { it.id == questionId }
        if (position >= 0 && position < questions.size) {
            selectedAnswers[position] = -1
//            getQuestionStatus(position).let {
//                selectedAnswerStatus[position] = it
//                when(it) {
//                    QuestionStatus.Correct -> countCorrect++
//                    QuestionStatus.Error -> countErrors++
//                    else -> {}
//                }
//            }
        }
    }

    fun setContinueInteraction(questionId: Int) {
        val position = questions.indexOfFirst { it.id == questionId }
        if (position >= 0 && position < questions.size && questions[position].impugned)
            countImpugned++
    }

}