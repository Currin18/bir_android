package com.jesusmoreira.bir.views.exam

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.model.Exam
import kotlinx.android.synthetic.main.fragment_question_answer.view.*
import kotlinx.android.synthetic.main.toolbar_exam.*

class ExamActivity : AppCompatActivity(), QuestionExamFragment.OnQuestionExamInteractionListener {

    companion object {
        const val EXTRA_EXAM = "EXTRA_EXAM"
        const val EXTRA_INITIAL_POSITION = "EXTRA_INITIAL_POSITION"
        const val EXTRA_RAND = "EXTRA_RAND"

        fun newIntent(context: Context, exam: Exam, initialPosition: Int = 0, rand: Boolean = false) : Intent {
            val intent = Intent(context, ExamActivity::class.java)
            intent.putExtra(EXTRA_EXAM, exam)
            intent.putExtra(EXTRA_INITIAL_POSITION, initialPosition - 1)
            intent.putExtra(EXTRA_RAND, rand)
            return intent
        }
    }

//    var question: Question? = null
    var exam: Exam? = null
    var rand = false

//    private var questionPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

        if (intent != null && intent.hasExtra(EXTRA_EXAM)) {
            exam = intent.getParcelableExtra(EXTRA_EXAM)

            if (intent.hasExtra(EXTRA_INITIAL_POSITION)) exam!!.questionPosition = intent.getIntExtra(EXTRA_INITIAL_POSITION, -1)
            if (intent.hasExtra(EXTRA_RAND)) exam!!.rand = intent.getBooleanExtra(EXTRA_RAND, false)
        }

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        goToNextQuestion()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_exam, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClickAnswer(item: Int) {
        val buttonLetPass = findViewById<Button>(R.id.btn_let_pass)
        val buttonContinue = findViewById<Button>(R.id.btn_continue)
        if (buttonContinue.visibility == View.GONE) {
            val setted = exam!!.setOnClickAnswer(item)

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
            }
            buttonLetPass.visibility = View.GONE
            buttonContinue.visibility = View.VISIBLE
        }
    }

    override fun onLetPassInteraction() {
        exam!!.setLetPassInteraction()
        goToNextQuestion()
    }

    override fun onContinueInteraction() {
        exam!!.setContinueInteraction()
        goToNextQuestion()
    }

    private fun goToNextQuestion() {
        val question = exam!!.nextQuestion()
        if (question != null) {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment, QuestionExamFragment.newInstance(question))
            fragmentTransaction.commit()
            updateToolbarCounts()
        } else {
            Toast.makeText(applicationContext, "Exam finished", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateToolbarCounts() {
        toolbar_id_question.text = "#${exam!!.questions[exam!!.questionPosition].id}"
        toolbar_count_question.text = "${getString(R.string.toolbar_count_question)} ${(exam!!.questionCount)}/${exam?.questions?.size}"
        toolbar_answer_success.text = "${getString(R.string.toolbar_answer_success)} ${exam?.questionsSuccess}"
        toolbar_answer_error.text = "${getString(R.string.toolbar_answer_error)} ${exam?.questionsError}"
        toolbar_answer_passed.text = "${getString(R.string.toolbar_answer_passed)} ${exam?.questionsPassed}"
        toolbar_count_points.text = "${getString(R.string.toolbar_count_points)} ${(exam!!.questionsSuccess - (exam!!.questionsError / 3))}"
    }
}