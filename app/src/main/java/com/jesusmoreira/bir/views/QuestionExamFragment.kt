package com.jesusmoreira.bir.views

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.model.Question

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
        return inflater.inflate(R.layout.fragment_question_exam, container, false)
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
        fun onClickAnswer()
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
