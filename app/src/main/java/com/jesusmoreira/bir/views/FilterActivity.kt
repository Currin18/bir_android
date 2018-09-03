package com.jesusmoreira.bir.views

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import org.json.JSONArray

class FilterActivity : AppCompatActivity(),
        QuestionsListFragment.OnListFragmentInteractionListener,
        ExamsGridFragment.OnListFragmentInteractionListener,
        CategoriesListFragment.OnListFragmentInteractionListener,
        AdvanceFilterFragment.OnFragmentInteractionListener {

    var fab : FloatingActionButton? = null
    var fabFilters : FloatingActionButton? = null
    var bottomNavigation : BottomNavigationView? = null

    var collection: Collection? = null
    var questions: Array<Question>? = null
    var exams: Array<Exam>? = null
    var categories: Array<Category>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
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
        fab?.setOnClickListener {
            Toast.makeText(this, "FAB: ${collection?.exams?.get(0)?.questions?.get(0)?.statement}", Toast.LENGTH_SHORT).show()
        }

        fabFilters = this.findViewById(R.id.filters_fab)

        loadInitialData()

        goToExamGrid()

        collection!!.groupByCategories()
    }

    override fun onClickQuestion(item: Question) {
        Toast.makeText(this, "Question: " + item.id, Toast.LENGTH_SHORT).show()
    }

    override fun onBackQuestion() {
        bottomNavigation?.visibility = VISIBLE
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
            exams.sortBy { it.year }
            collection = Collection("", "", exams.toTypedArray())
        }
    }

    private fun initialView() {
        fab?.hide()
        fabFilters?.hide()
        bottomNavigation?.visibility = VISIBLE
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
        fab?.show()
//        supportActionBar?.setTitle(R.string.text_exams)
        if (exams == null) exams = collection!!.exams
        updateFragment(ExamsGridFragment.newInstance(exams!!))
        return true
    }

    private fun goToCategoriesList(): Boolean {
        initialView()
        fab?.show()
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
        updateFragment(AdvanceFilterFragment.newInstance())
        return true
    }
}
