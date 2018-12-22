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
import com.jesusmoreira.bir.model.Category
import com.jesusmoreira.bir.model.Collection
import com.jesusmoreira.bir.model.Exam
import com.jesusmoreira.bir.model.Question
import com.jesusmoreira.bir.utils.FileUtils
import com.jesusmoreira.bir.views.exam.ExamActivity
import com.jesusmoreira.bir.views.QuestionsListFragment

class FilterActivity : AppCompatActivity(),
        QuestionsListFragment.OnListFragmentInteractionListener,
        YearsGridFragment.OnListFragmentInteractionListener,
        CategoriesListFragment.OnListFragmentInteractionListener,
        AdvancedFiltersFragment.OnFragmentInteractionListener {

    var fab : FloatingActionButton? = null
    var fabFilters : FloatingActionButton? = null
    var bottomNavigation : BottomNavigationView? = null

    var collection: Collection? = null
    var exams: Array<Exam>? = null
    var categories: Array<Category>? = null

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
                startExam(it)
            }
        }

        fabFilters = this.findViewById(R.id.filters_fab)

        collection = FileUtils.loadInitialData(this)

        goToExamGrid()

        collection?.groupByCategories()
    }

    override fun onClickQuestion(position: Int, item: Question) {
//        Toast.makeText(this, "Question: " + item.id, Toast.LENGTH_SHORT).show()
//        val intent = ExamActivity.newIntent(applicationContext, item)
//        startActivity(intent)
        examSelected?.let {
            startExam(it, position)
        }
    }

    override fun onResume(items: Array<Question>) {
        val exam = Exam("", items)
        this.uploadExam(exam)
    }

    override fun onBackQuestion() {
        initialView()
//        bottomNavigation?.visibility = VISIBLE
    }

    override fun onClickExam(item: Exam) {
//        Toast.makeText(this, "Exam: " + item.year, Toast.LENGTH_SHORT).show()
        goToQuestionsList(item.questions)
    }

    override fun onClickCategory(item: Category) {
//        Toast.makeText(this, "Category: " + item.name, Toast.LENGTH_SHORT).show()
        goToQuestionsList(item.questions)
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

    private fun startExam(exam: Exam, initialPosition: Int = -1, rand: Boolean = false) {
        val intent = ExamActivity.newIntent(applicationContext, exam, initialPosition, rand)
        startActivity(intent)
        this.finish()
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
        updateFragment(QuestionsListFragment.newInstance(questions!!), true)
        return true
    }

    private fun goToExamGrid(): Boolean {
        initialView()
//        fab?.show()
//        supportActionBar?.setTitle(R.string.text_exams)
        if (exams == null) exams = collection!!.exams
        updateFragment(YearsGridFragment.newInstance(exams!!))
        return true
    }

    private fun goToCategoriesList(): Boolean {
        initialView()
//        fab?.show()
//        supportActionBar?.setTitle(R.string.text_categories)
        if (categories == null) categories = collection!!.groupByCategories()
        updateFragment(CategoriesListFragment.newInstance(categories!!))
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
