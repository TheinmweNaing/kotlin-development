package com.team.attendancekt

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var toggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed)
        toggle?.syncState()
        toggle?.setToolbarNavigationClickListener {
            Navigation.findNavController(this, R.id.my_nav_host_fragment).navigateUp()
        }
        NavigationUI.setupWithNavController(navigationView,Navigation.findNavController(this, R.id.my_nav_host_fragment))
    }

    fun switchToggle(enable: Boolean) {
        toggle?.isDrawerIndicatorEnabled = enable
    }

    fun hideKeyBoard() {
        val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view == null) View(this)
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
