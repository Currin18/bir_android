package com.jesusmoreira.bir.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Category(val name: String, val questions: Array<Question>) :Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (name != other.name) return false
        if (!Arrays.equals(questions, other.questions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + Arrays.hashCode(questions)
        return result
    }
}