package com.jesusmoreira.bir.views.exam

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jesusmoreira.bir.adapters.QuestionRecyclerViewAdapter
import com.jesusmoreira.bir.R

import com.jesusmoreira.bir.model.Question

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [QuestionsListFragment.OnListFragmentInteractionListener] interface.
 */
class QuestionsListFragment : Fragment() {

//    private var items: Array<Question> = arrayOf()

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_question_list, container, false)

        var questionId: Int? = null
        (activity as ExamActivity).questionPosition.let { questionPosition ->
            if (questionPosition != null) {
                questionId = (activity as ExamActivity).exam.questions[questionPosition].id
            }
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager =  LinearLayoutManager(context)
                adapter = QuestionRecyclerViewAdapter(
                        context,
                        (activity as ExamActivity).exam.questions.toTypedArray(),
                        (activity as ExamActivity).exam.selectedAnswers,
                        questionId,
                        listener
                )

                (activity as ExamActivity).questionPosition.let {
                    if (it != null && layoutManager != null) (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(it, 0)
                }
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener?.onBackQuestion()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onClickQuestion(position: Int, item: Question)
        fun onResume(items: Array<Question>)
        fun onBackQuestion()
    }
}
