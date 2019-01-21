package com.jesusmoreira.bir.dao.exam

import com.jesusmoreira.bir.model.Exam

interface IExamDao {
    // Get Exams
    fun fetchExamById(examId: Int): Exam
    fun fetchExamUnfinished(): Exam?
    fun fetchAllExams(): List<Exam>
    // Add Exams
    fun addExam(exam: Exam): Boolean
    // Update Exams
    fun updateExam(exam: Exam): Boolean
    // Delete Exams
    fun deleteExamById(examId: Int): Boolean
    fun deleteAllExams(): Boolean
}