package com.jesusmoreira.bir.views.exam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.dao.Database
import com.jesusmoreira.bir.model.Exam
import com.jesusmoreira.bir.model.Filters
import com.jesusmoreira.bir.model.Question
import com.jesusmoreira.bir.utils.Constants
import org.json.JSONObject

class ExamActivity : AppCompatActivity(), QuestionExamFragment.OnQuestionExamInteractionListener, QuestionsListFragment.OnListFragmentInteractionListener {

    companion object {
        private const val EXTRA_FILTERS = "EXTRA_FILTERS"

        fun newIntent(context: Context, filters: String) : Intent {
            val intent = Intent(context, ExamActivity::class.java)
            intent.putExtra(EXTRA_FILTERS, filters)
            return intent
        }
    }

    private var database = Database(this)

    var exam: Exam = Exam()
    var filters: Filters = Filters()
    var questionPosition : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

        if (intent != null && intent.hasExtra(EXTRA_FILTERS)) {
            filters = Filters(JSONObject(intent.getStringExtra(EXTRA_FILTERS)))
        }

        database.open()

        initExam(filters)

        if (exam.questions.size > Constants.numberOfQuestions) {
            AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(getString(R.string.alert_too_many_questions)
                            .replace("$1", exam.questions.size.toString())
                            .replace("$2", Constants.numberOfQuestions.toString()))
                    .setPositiveButton(getString(R.string.button_accept)) { _, _ ->
                        filters.random = true
                        initExam(filters)
                    }
                    .setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
        }
    }

    private fun initExam(filters: Filters) {
        val questions: List<Question> = when {
            filters.countFilters() > 1 -> database.questionDao?.fetchFilteredQuestions(filters) ?: listOf()
            filters.random -> database.questionDao?.fetchRandomQuestions() ?: listOf()
            filters.years.isNotEmpty() -> database.questionDao?.fetchAllQuestionsByExam(filters.years.toList()) ?: listOf()
            filters.categories.isNotEmpty() -> database.questionDao?.fetchAllQuestionsByCategories(filters.categories.toList()) ?: listOf()
            filters.words.isNotEmpty() -> database.questionDao?.fetchAllQuestionsByWords(filters.words.toList(), filters.includeAnswers) ?: listOf()
            else -> listOf()
        }

        exam = Exam(filters, questions)

        goToListQuestions()
    }

    override fun onBackPressed() {
        if (exam.created != null) {
            AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(getString(R.string.alert_leave_exam))
                    .setPositiveButton(getString(R.string.button_accept)) { _, _ ->
                        super.onBackPressed()
                    }
                    .setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
        } else {
            super.onBackPressed()
        }
    }

    private fun updateFragment(fragment: Fragment, stacked: Boolean = false) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        if (stacked) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    override fun onClickQuestion(position: Int, item: Question) {
        goToQuestion(position)
    }

    override fun onResume(items: Array<Question>) {
        if (exam.created == null) {

        }
    }

    override fun onBackQuestion() {
//        updateMenu(true)
    }

    override fun onClickListAction() {
        goToListQuestions()
    }

    override fun onClickAnswer(questionId: Int, answerSelected: Int) {
        val question = exam.questions[questionPosition!!]
        exam.setOnClickAnswer(questionId, answerSelected)

        updateFragment(QuestionExamFragment.newInstance(question.toJson().toString(), answerSelected))
    }

    override fun onLetPassInteraction(questionId: Int) {
        exam.setLetPassInteraction(questionId)
        goToNextQuestion(questionId)
    }

    override fun onContinueInteraction(questionId: Int) {
        exam.setContinueInteraction(questionId)
        goToNextQuestion(questionId)
    }

    private fun goToListQuestions() {
        updateFragment(QuestionsListFragment())
    }

    private fun goToNextQuestion(questionId: Int) {
        val question = exam.nextQuestion(questionId)
        if (question != null) {
            questionPosition = exam.getPositionById(question.id!!)
            updateFragment(QuestionExamFragment.newInstance(question.toJson().toString(), exam.getSelectedAnswer(question.id!!)))
        }
    }

    private fun goToQuestion(position : Int) {
        questionPosition = position
        updateFragment(QuestionExamFragment.newInstance(exam.questions[position].toJson().toString(), exam.selectedAnswers[position]))
    }
}
