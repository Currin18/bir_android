package com.jesusmoreira.bir.dao.question

import com.jesusmoreira.bir.model.Category
import com.jesusmoreira.bir.model.Question

interface IQuestionDao {
    // Get Questions
    fun fetchQuestionById(questionId: Int): Question
    fun fetchQuestionByRef(questionRef: String): Question
    fun fetchAllQuestions(): List<Question>
    fun fetchAllQuestionsByExam(exams: List<String>): List<Question>
    fun fetchAllQuestionsByCategories(categories: List<String>): List<Question>
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
    fun getAllCategories(): List<Category>
}