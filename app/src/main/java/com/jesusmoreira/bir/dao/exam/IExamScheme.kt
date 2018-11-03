package com.jesusmoreira.bir.dao.exam

object IExamScheme {
    const val TABLE_NAME = "exam"
    const val COLUMN_ID = "_id"
    const val COLUMN_DATE = "date"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_FILTERS = "filters"
    const val COLUMN_QUESTIONS = "answers"

    const val TABLE_CREATE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_DATE TEXT,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_FILTERS TEXT,
            $COLUMN_QUESTIONS BLOB
        )
    """

    val COLUMNS = arrayOf(COLUMN_ID, COLUMN_DATE, COLUMN_DESCRIPTION, COLUMN_FILTERS, COLUMN_QUESTIONS)
}