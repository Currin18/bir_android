package com.jesusmoreira.bir.views.filter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

import com.jesusmoreira.bir.R
import kotlinx.android.synthetic.main.fragment_advance_filter.view.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AdvancedFiltersFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AdvancedFiltersFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AdvancedFiltersFragment : Fragment() {

//    private var yearsChecked: BooleanArray = booleanArrayOf()

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_advance_filter, container, false)
        v.input_years_button.setOnClickListener {
            context?.let {context ->
                val years = (activity as FilterActivity).years
                launchDialog(context, v.input_years, years.map { it -> it.toString() }.toTypedArray(), BooleanArray(years.size))
            }
        }

        v.input_categories_button.setOnClickListener {
            context?.let {context ->
                val categories = (activity as FilterActivity).categories
                launchDialog(context, v.input_categories, categories.toTypedArray(), BooleanArray(categories.size))
            }
        }

        v.filters_fab.setOnClickListener {
            onSearchPressed(
                    v.input_years.text.toString(),
                    v.input_categories.text.toString(),
                    v.input_words.text.toString(),
                    v.input_words_checkbox.isChecked,
                    v.input_random_checkbox.isChecked
            )
        }
        return v
    }

    private fun launchDialog(context: Context, editText: EditText, list: Array<String>, booleanArray: BooleanArray) {
        val builder = AlertDialog.Builder(context)

        editText.text.toString().split(", ", ",").let {inputArray ->
            for (i in 0 until list.size) {
                booleanArray[i] = list[i] in inputArray
            }
        }

        builder.setMultiChoiceItems(list, booleanArray) { _, which, isChecked ->
            // Update the current focused item's checked status
            booleanArray[which] = isChecked
        }

        builder.setCancelable(false)
        // set title
//                builder.setTitle("")

        builder.setPositiveButton("Select") { _, _ ->
            list.filterIndexed { i, _ -> booleanArray[i] }.let { it ->
                editText.setText(it.joinToString())
            }
        }

        builder.setNegativeButton("Cancel") { _, _ ->

        }

        builder.setNeutralButton("Select all") { _, _ ->
            editText.setText("")
        }

        builder.create().show()
    }

    private fun onSearchPressed(years: String, categories: String, words: String, includeAnswers: Boolean = true, random: Boolean =  true) {
        listener?.onFilterInteraction(
                when (years.isNotBlank()) {
                    true -> years.trim().split(", ", ",").map { year -> year.toInt() }
                    else -> arrayListOf()
                },
                when (categories.isNotBlank()) {
                    true -> categories.trim().split(", ", ",")
                    else -> arrayListOf()
                },
                words.trim().split(", ", ","),
                includeAnswers,
                random
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
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
    interface OnFragmentInteractionListener {
        fun onFilterInteraction(years: List<Int>, categories: List<String>, words: List<String>, includeAnswers: Boolean, random: Boolean)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AdvanceFilterFragment.
         */
//        @JvmStatic
//        fun newInstance() =
//                AdvancedFiltersFragment().apply {
//                    arguments = Bundle().apply {
//                    }
//                }
    }
}
