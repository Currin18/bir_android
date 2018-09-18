package com.jesusmoreira.bir.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.adapters.AnswerRecyclerViewAdapter
import com.jesusmoreira.bir.model.Question
import kotlinx.android.synthetic.main.fragment_question_answer.view.*
import kotlinx.android.synthetic.main.fragment_question_exam.view.*

private const val EXTRA_QUESTION = "EXTRA_QUESTION"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QuestionExamFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QuestionExamFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class QuestionExamFragment : Fragment() {
    private var question: Question? = null
    private var listener: OnQuestionExamInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getParcelable(EXTRA_QUESTION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_question_exam, container, false)
        if (question != null) {
            v.statement.text = question!!.statement

            if (v.answers is RecyclerView) {
                with(v.answers) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = AnswerRecyclerViewAdapter(question!!.answers!!, listener)
                }
            }

            if (question!!.impugned) {
                v.impugned.visibility = VISIBLE
            }

            v.btn_let_pass.setOnClickListener { listener?.onLetPassInteraction() }
            v.btn_continue.setOnClickListener { listener?.onContinueInteraction() }

//            if (question!!.selectedAnswer != null) {
//                (v.answers.layoutManager as LinearLayoutManager).findViewByPosition(question!!.correctAnswer)!!.cardLayout.setBackgroundColor(context.getColor(R.color.colorPrimaryLight))
//                if (question!!.selectedAnswer != question!!.correctAnswer) {
//                    (v.answers.layoutManager as LinearLayoutManager).findViewByPosition(question!!.selectedAnswer!!)!!.cardLayout.setBackgroundColor(context!!.getColor(R.color.red))
//                }
//
//                v.btn_let_pass.visibility = View.GONE
//                v.btn_continue.visibility = View.VISIBLE
//            }
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnQuestionExamInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnQuestionExamInteractionListener {
        fun onClickAnswer(item: Int)
        fun onLetPassInteraction()
        fun onContinueInteraction()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param question Question.
         * @return A new instance of fragment QuestionExamFragment.
         */
        @JvmStatic
        fun newInstance(question: Question) =
                QuestionExamFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(EXTRA_QUESTION, question)
                    }
                }
    }
}
