package com.jesusmoreira.bir.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.model.Question

class ExamActivity : AppCompatActivity(), QuestionExamFragment.OnQuestionExamInteractionListener {

    companion object {
        const val EXTRA_QUESTION = "EXTRA_QUESTION"
        const val EXTRA_RAND = "EXTRA_RAND"

        fun newIntent(context: Context, question: Question, rand: Boolean = false) : Intent {
            val intent = Intent(context, ExamActivity::class.java)
            intent.putExtra(EXTRA_QUESTION, question)
            intent.putExtra(EXTRA_RAND, rand)
            return intent
        }
    }

    var question: Question? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

        if (intent != null) {
            if (intent.hasExtra(EXTRA_QUESTION)) question = intent.getParcelableExtra(EXTRA_QUESTION)
        }

        goToQuestion(question)
    }

    override fun onClickAnswer(item: Int) {
        Toast.makeText(applicationContext, "onClickAnswer", Toast.LENGTH_SHORT).show()
    }

    override fun onLetPassInteraction() {
        Toast.makeText(applicationContext, "onLetPassInteraction", Toast.LENGTH_SHORT).show()
    }

    override fun onContinueInteraction() {
        Toast.makeText(applicationContext, "onContinueInteraction", Toast.LENGTH_SHORT).show()
    }

    private fun goToQuestion(item: Question?) {
        if (item != null) {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment, QuestionExamFragment.newInstance(item))
            fragmentTransaction.commit()
        }
    }
}
