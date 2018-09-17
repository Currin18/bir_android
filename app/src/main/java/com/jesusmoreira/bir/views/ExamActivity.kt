package com.jesusmoreira.bir.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.adapters.AnswerRecyclerViewAdapter
import com.jesusmoreira.bir.model.Exam
import com.jesusmoreira.bir.model.Question
import kotlinx.android.synthetic.main.fragment_question_answer.view.*

class ExamActivity : AppCompatActivity(), QuestionExamFragment.OnQuestionExamInteractionListener {

    companion object {
        const val EXTRA_EXAM = "EXTRA_EXAM"
        const val EXTRA_RAND = "EXTRA_RAND"

        fun newIntent(context: Context, exam: Exam, rand: Boolean = false) : Intent {
            val intent = Intent(context, ExamActivity::class.java)
            intent.putExtra(EXTRA_EXAM, exam)
            intent.putExtra(EXTRA_RAND, rand)
            return intent
        }
    }

    var question: Question? = null
    var exam: Exam? = null

    var questionCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

        if (intent != null) {
            if (intent.hasExtra(EXTRA_EXAM)) exam = intent.getParcelableExtra(EXTRA_EXAM)
        }

        goToNextQuestion()
    }

    override fun onClickAnswer(item: Int) {
        val answers: RecyclerView? = findViewById(R.id.answers)
        if (answers != null) {
            (answers.layoutManager as LinearLayoutManager).findViewByPosition(question!!.correctAnswer)!!.cardLayout.setBackgroundColor(getColor(R.color.colorPrimaryLight))
            if (item != question!!.correctAnswer) {
                (answers.layoutManager as LinearLayoutManager).findViewByPosition(item)!!.cardLayout.setBackgroundColor(getColor(R.color.red))
            }
        }
//        Toast.makeText(applicationContext, "onClickAnswer: $correct", Toast.LENGTH_SHORT).show()

    }

    override fun onLetPassInteraction() {
        Toast.makeText(applicationContext, "onLetPassInteraction", Toast.LENGTH_SHORT).show()
        goToNextQuestion()
    }

    override fun onContinueInteraction() {
        Toast.makeText(applicationContext, "onContinueInteraction", Toast.LENGTH_SHORT).show()
        goToNextQuestion()
    }

    private fun goToNextQuestion() {
        if (exam != null && questionCount < exam!!.questions.size) {
            question = exam!!.questions[questionCount]
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment, QuestionExamFragment.newInstance(question!!))
            fragmentTransaction.commit()
            questionCount++
        }
    }
}
