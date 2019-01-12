package com.jesusmoreira.bir.dao.question

object IQuestionScheme {
    const val TABLE_NAME = "question"
    const val COLUMN_ID = "_id"
    const val COLUMN_REF = "ref"
    const val COLUMN_YEAR = "year"
    const val COLUMN_NUMBER = "number"
    const val COLUMN_STATEMENT = "statement"
    const val COLUMN_ANSWERS = "answers"
    const val COLUMN_TAGS = "tags"
    const val COLUMN_CORRECT_ANSWER = "correctAnswer"
    const val COLUMN_IMPUGNED = "impugned"

    const val TABLE_CREATE = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_REF TEXT UNIQUE,
            $COLUMN_YEAR INTEGER,
            $COLUMN_NUMBER INTEGER,
            $COLUMN_STATEMENT TEXT NOT NULL,
            $COLUMN_ANSWERS TEXT,
            $COLUMN_TAGS TEXT COLLATE NOCASE,
            $COLUMN_CORRECT_ANSWER INTEGER,
            $COLUMN_IMPUGNED NUMERIC
        )
    """

    val COLUMNS = arrayOf(COLUMN_ID, COLUMN_REF, COLUMN_YEAR, COLUMN_NUMBER, COLUMN_STATEMENT, COLUMN_ANSWERS,
            COLUMN_TAGS, COLUMN_CORRECT_ANSWER, COLUMN_IMPUGNED)
}