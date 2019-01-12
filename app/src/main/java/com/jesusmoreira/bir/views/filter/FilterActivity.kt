package com.jesusmoreira.bir.views.filter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.dao.Database
import com.jesusmoreira.bir.model.*
import com.jesusmoreira.bir.views.exam.ExamActivity
import com.jesusmoreira.bir.views.exam.QuestionsListFragment

class FilterActivity : AppCompatActivity(),
        YearsGridFragment.OnListFragmentInteractionListener,
        CategoriesListFragment.OnListFragmentInteractionListener,
        AdvancedFiltersFragment.OnFragmentInteractionListener {

    private var database = Database(this)

    var fab : FloatingActionButton? = null
    var fabFilters : FloatingActionButton? = null
    var bottomNavigation : BottomNavigationView? = null

    var years: List<Int>? = null
    var categories: List<String>? = null

    var examSelected: Exam? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.toolbar_name_filter)
        setSupportActionBar(toolbar)

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.action_questions -> {
//                    goToQuestionsList()
//                }
                R.id.action_exams -> {
                    goToExamGrid()
                }
                R.id.action_categories -> {
                    goToCategoriesList()
                }
                R.id.action_filters -> {
                    goToAdvancedFilters()
                }
                else -> {
                    true
                }
            }
        }

        fab = this.findViewById(R.id.fab)
        fab?.setOnClickListener { _ ->
            //            Toast.makeText(this, "FAB: ${collection?.exams?.get(0)?.questions?.get(0)?.statement}", Toast.LENGTH_SHORT).show()
            examSelected?.let {
//                startExam(it)
            }
        }

        fabFilters = this.findViewById(R.id.filters_fab)

        database.open()
        years = database.questionDao?.getAllYears() ?: listOf()
        categories = database.questionDao?.getAllCategories() ?: listOf()

        goToExamGrid()
    }

    override fun onClickExam(item: Int) {
//        Toast.makeText(this, "Exam: " + item.year, Toast.LENGTH_SHORT).show()
        startExam(Filters(years = arrayOf(item)))
    }

    override fun onClickCategory(item: String) {
//        Toast.makeText(this, "Category: " + item.name, Toast.LENGTH_SHORT).show()
        startExam(Filters(categories = arrayOf(item)))
    }

    override fun onFilterInteraction(uri: Uri) {
        Toast.makeText(this, "Advanced Filters: " + "interaction", Toast.LENGTH_SHORT).show()
    }

//    var mSearch: MenuItem? = null
//    var mSearchView: SearchView? = null
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.toolbar_filter, menu)
//        mSearch = menu?.findItem(R.id.action_search)
//        mSearchView = mSearch?.actionView as SearchView?
//
//        mSearchView?.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//
//        })
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

    fun uploadExam(examSelected: Exam) { this.examSelected = examSelected }

    private fun initialView() {
        fab?.hide()
        fabFilters?.hide()
        bottomNavigation?.visibility = VISIBLE
    }

    private fun startExam(filters: Filters) {
        startActivity(ExamActivity.newIntent(this, filters.toString()))
    }

    private fun updateFragment(fragment: Fragment, stacked: Boolean = false) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        if (stacked) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    private fun goToQuestionsList(questions: Array<Question>): Boolean {
        initialView()
        bottomNavigation?.visibility = GONE
        fab?.show()
//        supportActionBar?.setTitle(R.string.text_questions)
//        if (questions == null) questions = collection!!.getAllQuestions()
//        updateFragment(QuestionsListFragment.newInstance(questions!!), true)
        return true
    }

    private fun goToExamGrid(): Boolean {
        initialView()
//        fab?.show()
//        supportActionBar?.setTitle(R.string.text_exams)
        updateFragment(YearsGridFragment())
        return true
    }

    private fun goToCategoriesList(): Boolean {
        initialView()
//        fab?.show()
//        supportActionBar?.setTitle(R.string.text_categories)
        updateFragment(CategoriesListFragment())
        return true
    }

    private fun goToAdvancedFilters(): Boolean {
        initialView()
        fab?.hide()
        fabFilters?.show()
//        supportActionBar?.setTitle(R.string.text_advanced_filters)
        updateFragment(AdvancedFiltersFragment.newInstance())
        return true
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, FilterActivity::class.java)
        }
    }
}
