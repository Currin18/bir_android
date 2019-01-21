package com.jesusmoreira.bir.model

import android.os.Parcelable
import com.jesusmoreira.bir.utils.Constants
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
        }
    }

    fun setContinueInteraction(questionId: Int) {
        val position = questions.indexOfFirst { it.id == questionId }
        if (position >= 0 && position < questions.size && questions[position].impugned)
            countImpugned++
    }

    fun calculateAnsweredQuestions(): Int = selectedAnswers.filter { it > 0 }.size
    fun calculateImpugnedQuestions(): Int = questions.filter { it.impugned }.size

    fun calculateScore(): Double {

        var validQuestions = 0
        var successfulQuestions = 0
        var failedQuestions = 0

        loop@ for (i in 0 until questions.size) {
            if (validQuestions < Constants.numberOfCountableQuestions) {
                if (!questions[i].impugned && selectedAnswers[i] > 0) {
                    validQuestions++

                    if (questions[i].correctAnswer == selectedAnswers[i]) successfulQuestions++
                    else failedQuestions++
                }
            } else {
                break@loop
            }
        }

        return successfulQuestions.toDouble() - (failedQuestions.toDouble() / 3)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exam

        if (filters != other.filters) return false
        if (questions != other.questions) return false
        if (!selectedAnswers.contentEquals(other.selectedAnswers)) return false
        if (id != other.id) return false
        if (created != other.created) return false
        if (finished != other.finished) return false
        if (countCorrect != other.countCorrect) return false
        if (countErrors != other.countErrors) return false
        if (countImpugned != other.countImpugned) return false

        return true
    }

    override fun hashCode(): Int {
        var result = filters.hashCode()
        result = 31 * result + questions.hashCode()
        result = 31 * result + selectedAnswers.contentHashCode()
        result = 31 * result + (id ?: 0)
        result = 31 * result + (created?.hashCode() ?: 0)
        result = 31 * result + (finished?.hashCode() ?: 0)
        result = 31 * result + countCorrect
        result = 31 * result + countErrors
        result = 31 * result + countImpugned
        return result
    }

}