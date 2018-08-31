package com.jesusmoreira.bir.model

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Collection(
        var version: String = "",
        var timestamp: String = "",
        var exams: Array<Exam> = arrayOf()
) {
    fun getAllQuestions(): Array<Question> {
        val arrayList = ArrayList<Question>()

        for (i in 0 until exams.size) {
            arrayList.addAll(exams[i].questions)
        }

        val questions: Array<Question> = arrayList.toTypedArray()
        questions.sortBy{ it.statement }

        return questions
    }

    fun groupByCategories(): Array<Category> {
        val categories = ArrayList<Category>()
        val questions = getAllQuestions()
        val tags = ArrayList<String>()
        for (question in questions) {
            if (question.tags != null && question.tags!!.isNotEmpty()) {
                for (tag in question.tags!!) {
                    if (!tags.contains(tag)) {
                        tags.add(tag)
                    }
                }
            }
        }
        tags.sortBy { it }
        println("Categories: " + Arrays.toString(tags.toTypedArray()))

        for (tag in tags) {
            val filteredQuestions = questions.filter { it.tags != null && it.tags!!.contains(tag) }
            categories.add(Category(tag, filteredQuestions.toTypedArray()))
        }
        return categories.toTypedArray()
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