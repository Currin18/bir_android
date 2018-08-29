package com.jesusmoreira.bir.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.dummy.DummyContent
import com.jesusmoreira.bir.model.Collection
import com.jesusmoreira.bir.model.Exam
import com.jesusmoreira.bir.model.Question
import com.jesusmoreira.bir.utils.FileUtils
import org.json.JSONArray

class MainActivity : AppCompatActivity(),
        QuestionsListFragment.OnListFragmentInteractionListener,
        ExamsGridFragment.OnListFragmentInteractionListener,
        CategoriesListFragment.OnListFragmentInteractionListener {

    var fab : FloatingActionButton? = null
    var collection: Collection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_questions -> {
                    goToQuestionsList()
                }
                R.id.action_exams -> {
                    goToExamGrid()
                }
                R.id.action_categories -> {
                    goToCategoriesList()
                }
                else -> {
                    true
                }
            }
        }

        fab = this.findViewById(R.id.fab)
        fab?.setOnClickListener {
            Toast.makeText(this, "FAB: ${collection?.exams?.get(0)?.questions?.get(0)?.statement}", Toast.LENGTH_SHORT).show()
        }

        loadInitialData()

        goToQuestionsList()
    }

    override fun onClickQuestion(item: Question) {
        Toast.makeText(this, "Question: " + item.id, Toast.LENGTH_SHORT).show()
    }

    override fun onClickExam(item: Exam) {
        Toast.makeText(this, "Exam: " + item.year, Toast.LENGTH_SHORT).show()
    }

    override fun onClickCategory(item: DummyContent.DummyItem?) {
        Toast.makeText(this, "Category: " + item.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadInitialData() {
        val path = ""
        val files = FileUtils.listJSONAssets(this, path)
        if (files != null && files.isNotEmpty()) {
            val exams = ArrayList<Exam>()
            for (i in 0 until files.size) {
                val json = JSONArray(FileUtils.readJSONFromAsset(this, files[i]))

                val exam = Exam(json, files[i].replace(".json", ""))
                exams.add(exam)
            }
            collection = Collection("", "", exams.toTypedArray())
        }
    }

    private fun updateFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }

    private fun goToQuestionsList(): Boolean {
        fab?.show()
        supportActionBar?.setTitle(R.string.text_questions)
        updateFragment(QuestionsListFragment.newInstance(collection!!.getQuestionsArray()))
        return true
    }

    private fun goToExamGrid(): Boolean {
        fab?.hide()
        supportActionBar?.setTitle(R.string.text_exams)
        updateFragment(ExamsGridFragment.newInstance(collection!!.exams))
        return true
    }

    private fun goToCategoriesList(): Boolean {
        fab?.hide()
        supportActionBar?.setTitle(R.string.text_categories)
        updateFragment(CategoriesListFragment())
        return true
    }
}
