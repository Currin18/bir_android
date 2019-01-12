package com.jesusmoreira.bir.views.main

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import com.jesusmoreira.bir.R
import com.jesusmoreira.bir.dao.Database
import com.jesusmoreira.bir.model.Collection
import com.jesusmoreira.bir.model.Exam
import com.jesusmoreira.bir.model.Filters
import com.jesusmoreira.bir.model.Question
import com.jesusmoreira.bir.utils.FileUtils
import com.jesusmoreira.bir.utils.PreferencesUtils
import com.jesusmoreira.bir.views.exam.ExamActivity
import com.jesusmoreira.bir.views.filter.FilterActivity

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val MAX_QUESTIONS: Int = 235
    }

    private var database = Database(this)
//    private var collection: List<Question>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        database.open()

        fab.setOnClickListener { fab ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            if (fab_filters.visibility == View.VISIBLE) {
                if (fab is FloatingActionButton) {
                    fab.setImageDrawable(getDrawable(R.drawable.ic_add))
                }
                fab_filters.hide()
                label_filters.visibility = View.INVISIBLE

                fab_random.hide()
                label_random.visibility = View.INVISIBLE
            } else {
                if (fab is FloatingActionButton) {
                    fab.setImageDrawable(getDrawable(R.drawable.ic_clear))
                }
                fab_filters.show()
                label_filters.visibility = View.VISIBLE

                fab_random.show()
                label_random.visibility = View.VISIBLE
            }
        }

        fab_random.setOnClickListener{
            goToRandomExam()
        }

        fab_filters.setOnClickListener {
            goToFilters()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        loadInitialData()
    }

    private fun loadInitialData() {
        if (PreferencesUtils.getDbLastUpdate(this) > 0) {
            database.questionDao?.fetchAllQuestions()
        } else {
            FileUtils.loadInitialData(this).toList().let {
                if (it.isNotEmpty()) {
                    database.questionDao?.addQuestions(it)
                    PreferencesUtils.setDbLastUpdate(this, System.currentTimeMillis())
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getRandomQuestions(collection: List<Question>?): List<Question> {
        var questions = emptyList<Question>()
        var copy = collection
        if (copy != null && copy.isNotEmpty()) {
            val random = java.util.Random(System.currentTimeMillis())
            var question: Question?
            do {
                val rand = random.nextInt(copy!!.size)
                questions = questions.plus(copy[rand])
                copy = copy.minus(copy[rand])
            } while (questions.size < MAX_QUESTIONS && copy!!.isNotEmpty())
        }
        return questions
    }

    private fun goToRandomExam() {
            startActivity(ExamActivity.newIntent(this, Filters(true).toString()))
    }

    private fun goToFilters() {
        startActivity(FilterActivity.newIntent(this))
    }
}
