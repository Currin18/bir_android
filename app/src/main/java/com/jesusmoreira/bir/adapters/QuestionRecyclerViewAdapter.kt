package com.jesusmoreira.bir.adapters

import android.content.Context
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jesusmoreira.bir.R


import com.jesusmoreira.bir.views.exam.QuestionsListFragment.OnListFragmentInteractionListener
import com.jesusmoreira.bir.dummy.DummyContent.DummyItem
import com.jesusmoreira.bir.model.Question
import com.jesusmoreira.bir.utils.TextUtils

import kotlinx.android.synthetic.main.fragment_question.view.*

/**
 * [RecyclerView.Adapter] that can display a [Question] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class QuestionRecyclerViewAdapter(
        private val context: Context,
        private val mValues: Array<Question>,
        private val mAnswers: Array<Int>,
        private val mQuestionId: Int?,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<QuestionRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Int
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onClickQuestion(item, mValues[item])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_question, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        val color = when {
            item.impugned -> context.getColor(R.color.grey)
            mAnswers[position] == 0 -> context.getColor(R.color.white)
            mAnswers[position] == item.correctAnswer -> context.getColor(R.color.colorPrimary)
            else -> context.getColor(R.color.red)
        }
        holder.mStatusView.setBackgroundColor(color)
        holder.mIdView.text = "#${item.ref}"
        holder.mContentView.text = TextUtils.parseToHtml(item.statement)

//        if (mQuestionId != null && mQuestionId == item.id) {
//            holder.mIdView.let{ it.setTypeface(it.typeface, Typeface.BOLD) }
//            holder.mContentView.let{ it.setTypeface(it.typeface, Typeface.BOLD) }
//        }

        with(holder.mView) {
            tag = position
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mStatusView: View = mView.question_status
        val mIdView: TextView = mView.question_id
        val mContentView: TextView = mView.question_content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
