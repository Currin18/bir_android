package com.jesusmoreira.bir.dao.exam

object IExamScheme {
    const val TABLE_NAME = "exam"
    const val COLUMN_ID = "_id"
    const val COLUMN_CREATE_DATE = "createDate"
    const val COLUMN_FINISH_DATE = "finishDate"
    const val COLUMN_FILTERS = "filters"
    const val COLUMN_QUESTIONS = "answers"
    const val COLUMN_SELECTED_ANSWERS = "selectedAnswers"

    const val TABLE_CREATE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_CREATE_DATE INTEGER,
            $COLUMN_FINISH_DATE INTEGER,
            $COLUMN_FILTERS TEXT,
            $COLUMN_QUESTIONS TEXT,
            $COLUMN_SELECTED_ANSWERS TEXT
        )
    """

    val COLUMNS = arrayOf(COLUMN_ID, COLUMN_CREATE_DATE, COLUMN_FINISH_DATE, COLUMN_FILTERS, COLUMN_QUESTIONS, COLUMN_SELECTED_ANSWERS)
}