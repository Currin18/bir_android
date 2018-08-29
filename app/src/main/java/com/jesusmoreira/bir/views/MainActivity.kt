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
import org.json.JSONArray
import org.json.JSONObject

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

        fab = findViewById(R.id.fab)
        fab?.setOnClickListener {
            Toast.makeText(this, "FAB: ${collection?.exams?.get(0)?.questions?.get(0)?.statement}", Toast.LENGTH_SHORT).show()
        }

        goToQuestionsList()

        val json = JSONArray("[\n" +
                "  {\n" +
                "    \"id\": \"2014-1\",\n" +
                "    \"statement\": \"Confiere a la membrana plasmática alta permeabilidad al agua:\",\n" +
                "    \"answers\": [\n" +
                "      \"Acuaporinas\",\n" +
                "      \"Canales iónicos\",\n" +
                "      \"La Na^+/K^+ -ATPasa\",\n" +
                "      \"Intercambiador Cl/HCO_3\",\n" +
                "      \"Su composición lipídica\"\n" +
                "    ],\n" +
                "    \"correct-answer\": 1,\n" +
                "    \"tags\": [\"fisiologia\",\"bioquimica\",\"citologia\"]\n" +
                "  }\n" +
                "]")

        var exam = Exam(json, "2014", "1")
        var arry = ArrayList<Exam>()
        arry.add(exam)
        collection = Collection("", "", arry)
    }

    override fun onClickQuestion(item: DummyContent.DummyItem?) {
        Toast.makeText(this, "Question: " + item.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onClickExam(item: DummyContent.DummyItem?) {
        Toast.makeText(this, "Exam: " + item.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onClickCategory(item: DummyContent.DummyItem?) {
        Toast.makeText(this, "Category: " + item.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateFragment(fragment: Fragment) {
        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }

    private fun goToQuestionsList(): Boolean {
        fab?.show()
        supportActionBar?.setTitle(R.string.text_questions)
        updateFragment(QuestionsListFragment())
        return true
    }

    private fun goToExamGrid(): Boolean {
        fab?.hide()
        supportActionBar?.setTitle(R.string.text_exams)
        updateFragment(ExamsGridFragment())
        return true
    }

    private fun goToCategoriesList(): Boolean {
        fab?.hide()
        supportActionBar?.setTitle(R.string.text_categories)
        updateFragment(CategoriesListFragment())
        return true
    }
}
