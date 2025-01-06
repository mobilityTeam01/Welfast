package com.example.welfast.Dashboard

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.welfast.Base.BaseActivity
import com.example.welfast.Base.Constance
import com.example.welfast.Base.PreferenceHelper
import com.example.welfast.Base.Retrofit.Urls
import com.example.welfast.BottomNavMenus.Home.HomeFragment
import com.example.welfast.Login.Login.LoginActivity
import com.example.welfast.NavDrawerMenus.About.About
import com.example.welfast.NavDrawerMenus.ContactUs.ContactUs
import com.example.welfast.NavDrawerMenus.EditProfile.EditProfile
import com.example.welfast.NavDrawerMenus.Notification.Notification
import com.example.welfast.NavDrawerMenus.PrivacyPolicy.PrivacyPolicy
import com.example.welfast.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class DashboardActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.navigationView)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set up Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up Navigation Controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up navigation drawer with NavController
        navView.setupWithNavController(navController)

        // Setup AppBarConfiguration for Navigation Drawer
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_editProfile, R.id.nav_notifications, R.id.nav_contactUs), drawerLayout
        )

        // Setup ActionBar with NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Connect NavigationView with NavController
        navView.setupWithNavController(navController)


        val headerView = navView.getHeaderView(0)

        val imgProfilePicture: CircleImageView = headerView.findViewById(R.id.imgProfile)
        val llEdit: LinearLayout = headerView.findViewById(R.id.llEdit)
        val llNotification: LinearLayout = headerView.findViewById(R.id.llNotification)
        val llContact: LinearLayout = headerView.findViewById(R.id.llContact)
        val llPrivacy: LinearLayout = headerView.findViewById(R.id.llPrivacy)
        val llAbout: LinearLayout = headerView.findViewById(R.id.llAbout)
        val llLogOut: LinearLayout = headerView.findViewById(R.id.llLogOut)

        imgProfilePicture.setOnClickListener {
            intentActivity(EditProfile())
        }
        llEdit.setOnClickListener {
            intentActivity(EditProfile())
        }
        llNotification.setOnClickListener {
            intentActivity(Notification())
        }
        llContact.setOnClickListener {
            intentActivity(ContactUs())
        }
        llPrivacy.setOnClickListener {
            intentActivity(PrivacyPolicy())
        }
        llAbout.setOnClickListener {
            intentActivity(About())
        }

        llLogOut.setOnClickListener {
            setLogout()
        }

        Glide.with(this)
            .load(Urls.IMAGE_BASE+PreferenceHelper.read(Constance.USER_PICTURE))
            .placeholder(R.drawable.circular_profile_pic)
            .into(imgProfilePicture)

        // Handle Bottom Navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.nav_booking -> navController.navigate(R.id.BookingsFragment)
                R.id.nav_doctors -> navController.navigate(R.id.DoctorsFragment)
                R.id.nav_health_package -> navController.navigate(R.id.HealthPackagesFragment)

                R.id.nav_home ->navController.navigate(R.id.HomeFragment)
            }
            true
        }
    }

    private fun setLogout() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        bottomSheetDialog.setContentView(R.layout.bottom_sheet_logout)

        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        val btnCancel = bottomSheetDialog.findViewById<Button>(R.id.btnCancel)
        val btnLogout = bottomSheetDialog.findViewById<Button>(R.id.btnLogout)

        btnCancel?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        btnLogout?.setOnClickListener {
            PreferenceHelper.clearPref()
            PreferenceHelper.writeBool(Constance.IS_LOGGED_IN, false)
            val intent1 = Intent(this, LoginActivity::class.java)
            startActivity(intent1)
            finish()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(drawerLayout) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }
}