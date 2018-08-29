package com.jesusmoreira.bir.model

data class Collection(
        var version: String = "",
        var timestamp: String = "",
        var exams: ArrayList<Exam> = ArrayList()
)