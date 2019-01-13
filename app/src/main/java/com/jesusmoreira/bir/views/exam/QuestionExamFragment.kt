package com.jesusmoreira.bir.views.exam

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
import com.jesusmoreira.bir.model.Exam
import com.jesusmoreira.bir.model.Exam.Companion.QuestionStatus
import com.jesusmoreira.bir.model.Question
import com.jesusmoreira.bir.utils.TextUtils
import kotlinx.android.synthetic.main.fragment_question_answer.view.*
import kotlinx.android.synthetic.main.fragment_question_exam.view.*
import org.json.JSONObject

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
    private var question: Question = Question()
    private var selectedAnswer: Int = 0
    private var status: Exam.Companion.QuestionStatus = QuestionStatus.Default

    private var listener: OnQuestionExamInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(EXTRA_QUESTION)) {
                val jsonQuestion = JSONObject(it.getString(EXTRA_QUESTION));
                question = Question(jsonQuestion)
            }
            if (it.containsKey(EXTRA_SELECTED_ANSWER)) {
                selectedAnswer = it.getInt(EXTRA_SELECTED_ANSWER)

                status =  when (selectedAnswer) {
                    0 -> QuestionStatus.Default
                    -1 -> QuestionStatus.Passed
                    question.correctAnswer -> QuestionStatus.Correct
                    else -> QuestionStatus.Error
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_question_exam, container, false)

        v.statement.text = TextUtils.parseToHtml(question.statement)

        var correctAnswer: Int? = null
        var selectedRow: Int? = null

        when(status) {
            QuestionStatus.Correct -> {
                correctAnswer = question.correctAnswer -1
                selectedRow = question.correctAnswer -1
//                correctRow?.cardLayout?.setBackgroundColor(context!!.getColor(R.color.colorPrimaryLight))
            }
            QuestionStatus.Error -> {
                correctAnswer = question.correctAnswer -1
                selectedRow = selectedAnswer -1
//                correctRow?.cardLayout?.setBackgroundColor(context!!.getColor(R.color.colorPrimaryLight))
//
//                val selectedRow = manager.findViewByPosition(selectedAnswer)!!
//                selectedRow.cardLayout.setBackgroundColor(context!!.getColor(R.color.red))
            }
            QuestionStatus.Passed -> {

            }
            QuestionStatus.Default -> {

            }
        }

        if (v.answers is RecyclerView) {
            with(v.answers) {
                layoutManager = LinearLayoutManager(context)
                adapter = AnswerRecyclerViewAdapter(context, question.answers, correctAnswer, selectedRow, listener)
            }
        }

        if (question.impugned) {
            v.impugned.visibility = VISIBLE
            v.btn_let_pass.visibility = View.GONE
            v.btn_continue.visibility = View.VISIBLE
        }

        if (selectedAnswer > 0) {
            v.btn_let_pass.visibility = View.GONE
            v.btn_continue.visibility = View.VISIBLE
        }

        v.btn_let_pass.setOnClickListener { listener?.onLetPassInteraction(question.id ?: 0) }
        v.btn_continue.setOnClickListener { listener?.onContinueInteraction(question.id ?: 0) }

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
        fun onClickAnswer(answerSelected: Int)
        fun onLetPassInteraction(questionId: Int)
        fun onContinueInteraction(questionId: Int)
    }

    companion object {

        private const val EXTRA_QUESTION = "EXTRA_QUESTION"
        private const val EXTRA_SELECTED_ANSWER = "EXTRA_SELECTED_ANSWER"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param question String.
         * @return A new instance of fragment QuestionExamFragment.
         */
        @JvmStatic
        fun newInstance(question: String, selectedAnswer: Int) =
                QuestionExamFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_QUESTION, question)
                        putInt(EXTRA_SELECTED_ANSWER, selectedAnswer)
                    }
                }
    }
}
