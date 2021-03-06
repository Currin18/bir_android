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

    private var bottomNavigation : BottomNavigationView? = null

    var years: List<Int> = arrayListOf()
    var categories: List<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.toolbar_name_filter)
        setSupportActionBar(toolbar)

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
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

    override fun onFilterInteraction(years: List<Int>, categories: List<String>, words: List<String>, includeAnswers: Boolean, random: Boolean) {
//        Toast.makeText(this, "Advanced Filters: years-> $years, categories-> $categories, words-> $words, includeAnswers-> $includeAnswers", Toast.LENGTH_SHORT).show()
        val filters = Filters()
        if (years.isNotEmpty()) filters.years = years.toTypedArray()
        if (categories.isNotEmpty()) filters.categories = categories.toTypedArray()
        if (words.isNotEmpty()) filters.words = words.toTypedArray()
        filters.includeAnswers = includeAnswers
        filters.random = random

        startExam(filters)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

        }
        return super.onOptionsItemSelected(item)
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

    private fun goToExamGrid(): Boolean {
        updateFragment(YearsGridFragment())
        return true
    }

    private fun goToCategoriesList(): Boolean {
        updateFragment(CategoriesListFragment())
        return true
    }

    private fun goToAdvancedFilters(): Boolean {
        updateFragment(AdvancedFiltersFragment())
        return true
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, FilterActivity::class.java)
        }
    }
}
