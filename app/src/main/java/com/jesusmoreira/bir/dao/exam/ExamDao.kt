package com.jesusmoreira.bir.dao.exam

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.jesusmoreira.bir.dao.DbContentProvider
import com.jesusmoreira.bir.dao.question.QuestionDao
import com.jesusmoreira.bir.model.Exam
import com.jesusmoreira.bir.model.Filters
import com.jesusmoreira.bir.utils.TextUtils
import org.json.JSONObject

class ExamDao(db: SQLiteDatabase): DbContentProvider<Exam>(db), IExamDao {
    var questionDao: QuestionDao? = null

    init {
        questionDao = QuestionDao(db)
    }

    override fun cursorToEntity(cursor: Cursor): Exam {

        val exam = Exam()

        cursor.getColumnIndex(IExamScheme.COLUMN_ID).let { if(it != -1) exam.id = cursor.getInt(it) }
        cursor.getColumnIndex(IExamScheme.COLUMN_CREATE_DATE).let { if(it != -1) exam.created = cursor.getLong(it) }
        cursor.getColumnIndex(IExamScheme.COLUMN_FINISH_DATE).let { if(it != -1) exam.finished = cursor.getLong(it) }
        cursor.getColumnIndex(IExamScheme.COLUMN_FILTERS).let {
            if(it != -1) exam.filters = Filters(JSONObject(cursor.getString(it)))
        }
        cursor.getColumnIndex(IExamScheme.COLUMN_QUESTIONS).let {
            if(it != -1) {
                val ids = TextUtils.convertStringToArrayOfInt(cursor.getString(it))
                exam.questions = questionDao?.fetchAllQuestionsByIds(ids.toList()) ?: listOf()
            }
        }
        cursor.getColumnIndex(IExamScheme.COLUMN_SELECTED_ANSWERS).let {
            if (it != -1) exam.selectedAnswers = TextUtils.convertStringToArrayOfInt(cursor.getString(it))
        }



        return exam
    }

    override fun entityToContentValues(entity: Exam): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(IExamScheme.COLUMN_ID, entity.id)
        contentValues.put(IExamScheme.COLUMN_CREATE_DATE, entity.created)
        contentValues.put(IExamScheme.COLUMN_FINISH_DATE, entity.finished)
        contentValues.put(IExamScheme.COLUMN_FILTERS, entity.filters.toString())
        contentValues.put(IExamScheme.COLUMN_QUESTIONS, TextUtils.convertArrayOfIntToString(entity.questions.map { it.id ?: 0 }.toTypedArray()))
        contentValues.put(IExamScheme.COLUMN_SELECTED_ANSWERS, TextUtils.convertArrayOfIntToString(entity.selectedAnswers))

        return contentValues
    }

    override fun fetchExamById(examId: Int): Exam {
        var exam = Exam()
        val selection = "${IExamScheme.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(examId.toString())

        val cursor = super.query(IExamScheme.TABLE_NAME, IExamScheme.COLUMNS, selection, selectionArgs, IExamScheme.COLUMN_ID)

        cursor.moveToFirst()
        if (cursor.count > 0) exam = cursorToEntity(cursor)
        cursor.close()

        return exam
    }

    override fun fetchExamUnfinished(): Exam? {
        var exam: Exam? = null

        val cursor = super.query(IExamScheme.TABLE_NAME, IExamScheme.COLUMNS, null, null, IExamScheme.COLUMN_ID)

        if (cursor.count > 0) {
            cursor.moveToLast()
            cursorToEntity(cursor).let {
                if (it.finished != null && it.finished == 0L) exam = it
            }
        }
        cursor.close()

        return exam
    }

    override fun fetchAllExams(): List<Exam> {
        val examList = mutableListOf<Exam>()
        val cursor = super.query(IExamScheme.TABLE_NAME, IExamScheme.COLUMNS, null, null, IExamScheme.COLUMN_ID)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val exam = cursorToEntity(cursor)
            examList.add(exam)
            cursor.moveToNext()
        }
        cursor.close()

        return examList
    }

    override fun addExam(exam: Exam): Boolean = super.insert(IExamScheme.TABLE_NAME, entityToContentValues(exam)) > 0

    override fun updateExam(exam: Exam) =
        super.update(IExamScheme.TABLE_NAME, entityToContentValues(exam), "${IExamScheme.COLUMN_ID} = ?", arrayOf(exam.id.toString())) > 0

    override fun deleteExamById(examId: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllExams(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}