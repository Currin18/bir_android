package com.jesusmoreira.bir.views.exam

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.dao.Database
import com.jesusmoreira.bir.model.Exam
import com.jesusmoreira.bir.model.Filters
import com.jesusmoreira.bir.model.Question
import kotlinx.android.synthetic.main.toolbar_exam.*
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
    var menuVisibility = true
    var questionPosition : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

        if (intent != null && intent.hasExtra(EXTRA_FILTERS)) {
            filters = Filters(JSONObject(intent.getStringExtra(EXTRA_FILTERS)))
        }

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        database.open()

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
        updateToolbarCounts()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_exam, menu)
        if (!menuVisibility && menu != null) {
            menu.findItem(R.id.action_question_list).setVisible(false)
            menu.findItem(R.id.action_report_error).isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            R.id.action_question_list -> {
//                updateFragment(QuestionsListFragment.newInstance(exam!!.questions), true)
//                updateMenu(false)
//            }
//        }
        return true
    }

    private fun updateMenu(visible: Boolean) {
        menuVisibility = visible
        invalidateOptionsMenu()
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
        updateMenu(true)
    }

    override fun onResume(items: Array<Question>) {
        if (exam.created == null) {

        }
    }

    override fun onBackQuestion() {
        updateMenu(true)
    }

    override fun onClickAnswer(answerSelected: Int) {
//        val buttonLetPass = findViewById<Button>(R.id.btn_let_pass)
//        val buttonContinue = findViewById<Button>(R.id.btn_continue)
//        if (buttonContinue.visibility == View.GONE) {
            val question = exam.questions[questionPosition!!]
//            exam.setOnClickAnswer(exam.getPositionById(question.id!!), answerSelected)

            updateFragment(QuestionExamFragment.newInstance(question.toJson().toString(), answerSelected))

            updateToolbarCounts()
//            buttonLetPass.visibility = View.GONE
//            buttonContinue.visibility = View.VISIBLE
//        }


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
        /*val question = exam!!.nextQuestion()
        if (question != null) {
            updateFragment(QuestionExamFragment.newInstance(question.toJson().toString()))
            updateToolbarCounts()
        } else {
            Toast.makeText(applicationContext, "Exam finished", Toast.LENGTH_SHORT).show()
        }*/
        val question = exam.nextQuestion(questionId)
        if (question != null) {
            updateFragment(QuestionExamFragment.newInstance(question.toJson().toString(), exam.getSelectedAnswer(question.id!!)))
            questionPosition = exam.getPositionById(question.id!!)
            updateToolbarCounts()
        }
    }

    private fun goToQuestion(position : Int) {
        questionPosition = position
        updateFragment(QuestionExamFragment.newInstance(exam.questions[position].toJson().toString(), exam.selectedAnswers[position]))
        updateToolbarCounts()
    }

    private fun updateToolbarCounts() {
        if (questionPosition != null) toolbar_id_question.text = "#${exam.questions[questionPosition!!].ref}"
        toolbar_count_question.text = "${getString(R.string.toolbar_count_question)} ${exam.selectedAnswers.count { it != 0 }}/${exam.questions.size}"
        toolbar_answer_success.text = "${getString(R.string.toolbar_answer_success)} ${exam.countCorrect}"
        toolbar_answer_error.text = "${getString(R.string.toolbar_answer_error)} ${exam.countErrors}"
        toolbar_answer_passed.text = "${getString(R.string.toolbar_answer_passed)} ${exam.selectedAnswers.count { it == -1 }}"
        toolbar_count_points.text = "${getString(R.string.toolbar_count_points)} ${(exam.countCorrect.toFloat()) - (exam.countErrors.toFloat() / 3)}"
    }
}
