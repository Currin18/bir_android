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

    var questions: List<Question> = listOf()
    var exam: Exam? = null
    var filters: Filters? = null
    var menuVisibility = true
//    private var questionPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

        if (intent != null && intent.hasExtra(EXTRA_FILTERS)) {
            filters = Filters(JSONObject(intent.getStringExtra(EXTRA_FILTERS)))
        }

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        database.open()
        if (filters != null) {
            questions = when {
                filters!!.countFilters() > 1 -> database.questionDao?.fetchFilteredQuestions(filters!!) ?: listOf()
                filters!!.random -> database.questionDao?.fetchRandomQuestions() ?: listOf()
                filters!!.years.isNotEmpty() -> database.questionDao?.fetchAllQuestionsByExam(filters!!.years.toList()) ?: listOf()
                filters!!.categories.isNotEmpty() -> database.questionDao?.fetchAllQuestionsByCategories(filters!!.categories.toList()) ?: listOf()
                filters!!.words.isNotEmpty() -> database.questionDao?.fetchAllQuestionsByWords(filters!!.words.toList(), filters!!.includeAnswers) ?: listOf()
                else -> listOf()
            }
        }

        goToListQuestions()
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

    }

    override fun onBackQuestion() {
        updateMenu(true)
    }

    override fun onClickAnswer(item: Int) {
        val buttonLetPass = findViewById<Button>(R.id.btn_let_pass)
        val buttonContinue = findViewById<Button>(R.id.btn_continue)
        if (buttonContinue.visibility == View.GONE) {
            /*val setted = exam!!.setOnClickAnswer(item)

            val answers: RecyclerView? = findViewById(R.id.answers)
            if (answers != null && setted) {
                val manager = (answers.layoutManager as LinearLayoutManager)
                val correctRow= manager.findViewByPosition(exam!!.questions[exam!!.questionPosition].correctAnswer)!!
                correctRow.cardLayout.setBackgroundColor(getColor(R.color.colorPrimaryLight))

                if (item != exam!!.questions[exam!!.questionPosition].correctAnswer) {
                    val selectedRow = manager.findViewByPosition(item)!!
                    selectedRow.cardLayout.setBackgroundColor(getColor(R.color.red))
                    exam!!.questionsError++
                } else {
                    exam!!.questionsSuccess++
                }
                updateToolbarCounts()
            }*/
            buttonLetPass.visibility = View.GONE
            buttonContinue.visibility = View.VISIBLE
        }
    }

    override fun onLetPassInteraction() {
//        exam!!.setLetPassInteraction()
        goToNextQuestion()
    }

    override fun onContinueInteraction() {
//        exam!!.setContinueInteraction()
        goToNextQuestion()
    }

    private fun goToListQuestions() {
        updateFragment(QuestionsListFragment())
    }

    private fun goToNextQuestion() {
        /*val question = exam!!.nextQuestion()
        if (question != null) {
            updateFragment(QuestionExamFragment.newInstance(question.toJson().toString()))
            updateToolbarCounts()
        } else {
            Toast.makeText(applicationContext, "Exam finished", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun goToQuestion(position : Int) {
//        exam!!.questionPosition = position
//        updateFragment(QuestionExamFragment.newInstance(exam!!.questions[position].toJson().toString()))
//        updateToolbarCounts()
    }

    @SuppressLint("SetTextI18n")
    private fun updateToolbarCounts() {
        /*toolbar_id_question.text = "#${exam!!.questions[exam!!.questionPosition].id}"
        toolbar_count_question.text = "${getString(R.string.toolbar_count_question)} ${(exam!!.questionCount)}/${exam?.questions?.size}"
        toolbar_answer_success.text = "${getString(R.string.toolbar_answer_success)} ${exam?.questionsSuccess}"
        toolbar_answer_error.text = "${getString(R.string.toolbar_answer_error)} ${exam?.questionsError}"
        toolbar_answer_passed.text = "${getString(R.string.toolbar_answer_passed)} ${exam?.questionsPassed}"
        toolbar_count_points.text = "${getString(R.string.toolbar_count_points)} ${(exam!!.questionsSuccess - (exam!!.questionsError / 3))}"*/
    }
}
