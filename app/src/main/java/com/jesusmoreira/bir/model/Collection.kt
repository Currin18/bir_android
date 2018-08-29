package com.jesusmoreira.bir.model

import java.util.*
import kotlin.collections.ArrayList

data class Collection(
        var version: String = "",
        var timestamp: String = "",
        var exams: Array<Exam> = arrayOf()
) {
    fun getQuestionsArray(): Array<Question> {
        val arrayList = ArrayList<Question>()

        for (i in 0 until exams.size) {
            arrayList.addAll(exams[i].questions)
        }

        val questions: Array<Question> = arrayList.toTypedArray()
        questions.sortByDescending { it.statement }

        return questions
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Collection

        if (version != other.version) return false
        if (timestamp != other.timestamp) return false
        if (!Arrays.equals(exams, other.exams)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + Arrays.hashCode(exams)
        return result
    }
}