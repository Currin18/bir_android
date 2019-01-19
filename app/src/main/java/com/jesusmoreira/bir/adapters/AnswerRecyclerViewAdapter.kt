package com.jesusmoreira.bir.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jesusmoreira.bir.R


import com.jesusmoreira.bir.dummy.DummyContent.DummyItem
import com.jesusmoreira.bir.utils.TextUtils
import com.jesusmoreira.bir.views.exam.QuestionExamFragment

import kotlinx.android.synthetic.main.fragment_category.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class AnswerRecyclerViewAdapter(
        private val context: Context,
        private val questionId: Int,
        private val mValues: Array<String>,
        private val correctAnswer: Int?,
        private val selectedAnswer: Int?,
        private val mListener: QuestionExamFragment.OnQuestionExamInteractionListener?)
    : RecyclerView.Adapter<AnswerRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Int
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            if (correctAnswer == null && selectedAnswer == null)
                mListener?.onClickAnswer(questionId, item + 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_question_answer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = TextUtils.parseToHtml(item)

        if (position == correctAnswer) {
            holder.mView.findViewById<View>(R.id.cardLayout).setBackgroundColor(context.getColor(R.color.colorPrimaryLight))
        } else if (position == selectedAnswer) {
            holder.mView.findViewById<View>(R.id.cardLayout).setBackgroundColor(context.getColor(R.color.red))
        }

        with(holder.mView) {
            tag = position
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
