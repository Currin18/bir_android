package com.jesusmoreira.bir.utils

import android.content.Context
import com.jesusmoreira.bir.model.Collection
import com.jesusmoreira.bir.model.Exam
import com.jesusmoreira.bir.model.Question
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.Charset

class FileUtils {
    companion object {
        fun listJSONAssets(context: Context, path: String): Array<String>? {
            var files: Array<String>? = null
            try {
                files = context.assets.list(path)
                files = files?.filter { it.contains(".json", true) }?.toTypedArray()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return files
        }

        fun readJSONFromAsset(context: Context, file: String = ""): String? {
            var json: String? = null
            try {
                val inputStream = context.assets.open(file)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charset.forName("UTF-8"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return json
        }

        fun loadInitialData(context: Context, path: String = ""): Array<Question> {
            var collection: Array<Question> = arrayOf()
            val files = listJSONAssets(context, path)
            if (files != null && files.isNotEmpty()) {
                for (i in 0 until files.size) {
                    JSONArray(readJSONFromAsset(context, files[i])).let {
                        for (i in 0 until it.length()) {
                            collection = collection.plus(Question(it.getJSONObject(i)))
                        }
                    }


//                    val exam = Exam(json, files[i].replace(".json", ""))
//                    exams.add(exam)
                }
//                exams.sortBy { it.year }
//                collection = Collection("", "", exams.toTypedArray())
            }
            return collection
        }
    }
}