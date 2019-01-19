package com.jesusmoreira.bir.dao.question

import com.jesusmoreira.bir.model.Filters
import com.jesusmoreira.bir.model.Question
import com.jesusmoreira.bir.utils.Constants

interface IQuestionDao {
    // Get Questions
    fun fetchQuestionById(questionId: Int): Question
    fun fetchQuestionByRef(questionRef: String): Question
    fun fetchAllQuestions(): List<Question>
    fun fetchAllQuestionsByExam(exams: List<Int>): List<Question>
    fun fetchAllQuestionsByCategories(categories: List<String>): List<Question>
    fun fetchAllQuestionsByWords(words: List<String>, includeAnswers: Boolean): List<Question>
    fun fetchRandomQuestions(limit: Int = Constants.numberOfQuestions): List<Question>
    fun fetchFilteredQuestions(filters: Filters, limit: Int = Constants.numberOfQuestions): List<Question>
    // Add Questions
    fun addQuestion(question: Question): Boolean
    fun addQuestions(questions: List<Question>)
    // Update Questions
    fun updateQuestion(question: Question): Boolean
    fun updateQuestions(questions: List<Question>)
    // Delete Questions
    fun deleteQuestionById(questionId: Int): Boolean
    fun deleteQuestionByRef(questionRef: String): Boolean
    fun deleteAllQuestions(): Boolean
    // Utils
    fun getAllYears(): List<Int>
    fun getAllCategories(): List<String>
}