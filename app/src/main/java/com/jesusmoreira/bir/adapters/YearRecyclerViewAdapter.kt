package com.jesusmoreira.bir.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jesusmoreira.bir.R


import com.jesusmoreira.bir.views.filter.YearsGridFragment.OnListFragmentInteractionListener
import com.jesusmoreira.bir.dummy.DummyContent.DummyItem
import com.jesusmoreira.bir.model.Exam

import kotlinx.android.synthetic.main.fragment_exam.view.*

/**
 * [RecyclerView.Adapter] that can display a [Int] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class YearRecyclerViewAdapter(
        private val items: List<Int>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<YearRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Int
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onClickExam(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_exam, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.mContentView.text = item.toString()

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
