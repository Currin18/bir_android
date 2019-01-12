package com.jesusmoreira.bir.dao.question

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.jesusmoreira.bir.dao.DbContentProvider
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMNS
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMN_ANSWERS
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMN_CORRECT_ANSWER
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMN_ID
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMN_IMPUGNED
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMN_REF
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMN_STATEMENT
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMN_TAGS
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMN_YEAR
import com.jesusmoreira.bir.dao.question.IQuestionScheme.COLUMN_NUMBER
import com.jesusmoreira.bir.dao.question.IQuestionScheme.TABLE_NAME
import com.jesusmoreira.bir.model.Category
import com.jesusmoreira.bir.model.Question
import com.jesusmoreira.bir.utils.TextUtils

class QuestionDao(db: SQLiteDatabase): DbContentProvider<Question>(db), IQuestionDao {
    override fun cursorToEntity(cursor: Cursor): Question {
        val question = Question()

        cursor.getColumnIndex(COLUMN_ID).let {
            if (it != -1) question.id = cursor.getInt(it)
        }
        cursor.getColumnIndex(COLUMN_REF).let {
            if (it != -1) question.ref = cursor.getString(it)
        }
        cursor.getColumnIndex(COLUMN_YEAR).let {
            if (it != -1) question.year = cursor.getInt(it)
        }
        cursor.getColumnIndex(COLUMN_NUMBER).let {
            if (it != -1) question.number = cursor.getInt(it)
        }
        cursor.getColumnIndex(COLUMN_STATEMENT).let {
            if (it != -1) question.statement = cursor.getString(it)
        }
        cursor.getColumnIndex(COLUMN_ANSWERS).let {
            if (it != -1) question.answers = TextUtils.convertStringToArray(cursor.getString(it))
        }
        cursor.getColumnIndex(COLUMN_TAGS).let {
            if (it != -1) question.tags = TextUtils.convertStringToArray(cursor.getString(it))
        }
        cursor.getColumnIndex(COLUMN_CORRECT_ANSWER).let {
            if (it != -1) question.correctAnswer = cursor.getInt(it)
        }
        cursor.getColumnIndex(COLUMN_IMPUGNED).let {
            if (it != -1) question.impugned = cursor.getInt(it) == 1
        }

        return question
    }

    override fun entityToContentValues(entity: Question): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(COLUMN_ID, entity.id)
        contentValues.put(COLUMN_REF, entity.ref)
        contentValues.put(COLUMN_YEAR, entity.year)
        contentValues.put(COLUMN_NUMBER, entity.number)
        contentValues.put(COLUMN_STATEMENT, entity.statement)
        contentValues.put(COLUMN_ANSWERS, TextUtils.convertArrayToString(entity.answers))
        contentValues.put(COLUMN_TAGS, TextUtils.convertArrayToString(entity.tags))
        contentValues.put(COLUMN_CORRECT_ANSWER, entity.correctAnswer)
        contentValues.put(COLUMN_IMPUGNED, entity.impugned)

        return contentValues
    }

    override fun fetchQuestionById(questionId: Int): Question {
        val selection = "$COLUMN_ID  = ?"
        val selectionArgs = arrayOf(questionId.toString())

        var question = Question()
        val cursor = super.query(TABLE_NAME, COLUMNS, selection, selectionArgs, COLUMN_ID)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            question = cursorToEntity(cursor)
            cursor.moveToNext()
        }
        cursor.close()

        return question
    }

    override fun fetchQuestionByRef(questionRef: String): Question {
        val selection = "$COLUMN_REF  = ?"
        val selectionArgs = arrayOf(questionRef)

        var question = Question()
        val cursor = super.query(TABLE_NAME, COLUMNS, selection, selectionArgs, COLUMN_ID)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            question = cursorToEntity(cursor)
            cursor.moveToNext()
        }
        cursor.close()

        return question
    }

    override fun fetchAllQuestions(): List<Question> {
        val questionList = mutableListOf<Question>()
        val cursor = super.query(TABLE_NAME, COLUMNS, null, null, COLUMN_ID)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val question = cursorToEntity(cursor)
            questionList.add(question)
            cursor.moveToNext()
        }
        cursor.close()

        return questionList
    }

    override fun fetchAllQuestionsByExam(exams: List<Int>): List<Question> {
        var selection = ""
        val selectedArgs = exams.map { it.toString() }.toTypedArray()
        val questionList = mutableListOf<Question>()

        if (selectedArgs.isNotEmpty()) {
            for (i in 0 until selectedArgs.size) {
                if (i != 0) selection += " OR "
                selection += "$COLUMN_YEAR = ?"
            }

            val cursor = super.query(TABLE_NAME, COLUMNS, selection, selectedArgs, COLUMN_ID)

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val question = cursorToEntity(cursor)
                questionList.add(question)
                cursor.moveToNext()
            }
            cursor.close()
        }

        return questionList
    }

    override fun fetchAllQuestionsByCategories(categories: List<String>): List<Question> {
        var selection = ""
        val selectedArgs = categories.toTypedArray()
        val questionList = mutableListOf<Question>()

        if (selectedArgs.isNotEmpty()) {
            for (i in 0 until selectedArgs.size) {
                if (i != 0) selection += " OR "
                selection += "$COLUMN_TAGS LIKE '%${selectedArgs[i]}%'"
            }

            val cursor = super.query(TABLE_NAME, COLUMNS, selection, null, COLUMN_ID)

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val question = cursorToEntity(cursor)
                questionList.add(question)
                cursor.moveToNext()
            }
            cursor.close()
        }

        return questionList
    }

    override fun fetchRandomQuestions(limit: Int): List<Question> {
        val questionList = mutableListOf<Question>()
        val cursor = super.query(TABLE_NAME, COLUMNS, null, null, "Random()", limit.toString())

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val question = cursorToEntity(cursor)
            questionList.add(question)
            cursor.moveToNext()
        }
        cursor.close()

        return questionList
    }

    override fun addQuestion(question: Question): Boolean = super.insert(TABLE_NAME, entityToContentValues(question)) > 0

    override fun addQuestions(questions: List<Question>) = super.insertBulk(TABLE_NAME, questions)

    override fun updateQuestion(question: Question): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateQuestions(questions: List<Question>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteQuestionById(questionId: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteQuestionByRef(questionRef: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllQuestions(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllYears(): List<Int> {
        val yearList = mutableListOf<Int>()
        val cursor = super.query(TABLE_NAME, arrayOf(COLUMN_YEAR), null, null, COLUMN_YEAR, null, null, null)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            cursor.getColumnIndex(COLUMN_YEAR).let {
                if (it != -1) yearList.add(cursor.getInt(it))
            }
            cursor.moveToNext()
        }
        cursor.close()

        return yearList
    }

    override fun getAllCategories(): List<String> {
        val categoryList = mutableListOf<String>()
        val cursor = super.query(TABLE_NAME, arrayOf(COLUMN_TAGS), null, null, COLUMN_TAGS, null, null, null)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            cursor.getColumnIndex(COLUMN_TAGS).let {
                if (it != -1) categoryList.addAll(TextUtils.convertStringToArray(cursor.getString(it)))
            }
            cursor.moveToNext()
        }
        cursor.close()

        return categoryList.distinct()
    }

}