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
import com.jesusmoreira.bir.views.main.MainActivity
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode

class ExamActivity : AppCompatActivity(), QuestionExamFragment.OnQuestionExamInteractionListener, QuestionsListFragment.OnListFragmentInteractionListener {

    companion object {
        private const val EXTRA_FILTERS = "EXTRA_FILTERS"
        private const val EXTRA_EXAM_ID = "EXTRA_EXAM_ID"

        fun newIntent(context: Context, filters: String) : Intent {
            val intent = Intent(context, ExamActivity::class.java)
            intent.putExtra(EXTRA_FILTERS, filters)
            return intent
        }

        fun newIntent(context: Context, examId: Int): Intent {
            val intent = Intent(context, ExamActivity::class.java)
            intent.putExtra(EXTRA_EXAM_ID, examId)
            return intent
        }
    }

    private var database = Database(this)

    var exam: Exam = Exam()
    var filters: Filters = Filters()
    var examId: Int? = null
    var questionPosition : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

        if (intent != null) {
            if (intent.hasExtra(EXTRA_FILTERS)) filters = Filters(JSONObject(intent.getStringExtra(EXTRA_FILTERS)))
            if (intent.hasExtra(EXTRA_EXAM_ID)) intent.getIntExtra(EXTRA_EXAM_ID, -1).let { if (it > 0) examId = it }
        }

        database.open()

        initExam()

        if (exam.questions.size > Constants.numberOfQuestions) {
            AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(getString(R.string.alert_too_many_questions)
                            .replace("$1", exam.questions.size.toString())
                            .replace("$2", Constants.numberOfQuestions.toString()))
                    .setPositiveButton(getString(R.string.button_accept)) { _, _ ->
                        filters.random = true
                        initExam()
                    }
                    .setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
        }
    }

    override fun onResume(items: Array<Question>) {
        if (exam.created == null) {

        }
    }

    override fun onPause() {
        if (exam.created != null) {
            if (exam.id != null) database.examDao?.updateExam(exam)
            else database.examDao?.addExam(exam)
        }
        super.onPause()
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

    private fun initExam() {

        if (examId != null) {
            database.examDao?.fetchExamById(examId!!).let {
                if (it!= null) {
                    exam = it
                    filters = it.filters
                }
            }
        } else {
            val questions: List<Question> = when {
                filters.countFilters() > 1 -> database.questionDao?.fetchFilteredQuestions(filters)
                        ?: listOf()
                filters.random -> database.questionDao?.fetchRandomQuestions() ?: listOf()
                filters.years.isNotEmpty() -> database.questionDao?.fetchAllQuestionsByExam(filters.years.toList())
                        ?: listOf()
                filters.categories.isNotEmpty() -> database.questionDao?.fetchAllQuestionsByCategories(filters.categories.toList())
                        ?: listOf()
                filters.words.isNotEmpty() -> database.questionDao?.fetchAllQuestionsByWords(filters.words.toList(), filters.includeAnswers)
                        ?: listOf()
                else -> listOf()
            }

            exam = Exam(filters, questions)
        }

        goToListQuestions()
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
        if (exam.created == null) exam.created = System.currentTimeMillis()
        goToQuestion(position)
    }

    override fun onBackQuestion() {}

    override fun onClickFinishExam() {
        showFinishExamAlert()
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

    private fun goToQuestion(position : Int) {
        questionPosition = position
        updateFragment(QuestionExamFragment.newInstance(exam.questions[position].toJson().toString(), exam.selectedAnswers[position]))
    }

    private fun goToNextQuestion(questionId: Int) {
        val question = exam.nextQuestion(questionId)
        if (question != null) {
            questionPosition = exam.getPositionById(question.id!!)
            updateFragment(QuestionExamFragment.newInstance(question.toJson().toString(), exam.getSelectedAnswer(question.id!!)))
        } else {
            showFinishExamAlert()
        }
    }

    private fun showFinishExamAlert() {
        val answeredQuestionsNumber = exam.calculateAnsweredQuestions()
        val impugnedQuestionsNumber = exam.calculateImpugnedQuestions()

        AlertDialog.Builder(this)
                .setMessage(getString(R.string.alert_finish_exam)
                        .replace("$1", "$answeredQuestionsNumber/${exam.questions.size}")
                        .replace("$2", "${exam.countCorrect}")
                        .replace("$3", "${exam.countErrors}")
                        .replace("$4", "${exam.questions.size - (answeredQuestionsNumber + impugnedQuestionsNumber)}")
                        .replace("$5", "$impugnedQuestionsNumber")
                        .replace("$6", "${BigDecimal(exam.calculateScore()).setScale(2, RoundingMode.HALF_EVEN)}"))
                .setPositiveButton(getString(R.string.button_accept)) { _, _ ->
                    exam.finished = System.currentTimeMillis()
                    startActivity(MainActivity.newIntent(this))
                }
                .setNegativeButton(getString(R.string.button_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

}
