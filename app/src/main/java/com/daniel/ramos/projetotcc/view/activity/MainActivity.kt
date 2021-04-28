package com.daniel.ramos.projetotcc.view.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.daniel.ramos.projetotcc.MyApplication
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.ActivityMainBinding
import com.daniel.ramos.projetotcc.presenter.MainPresenter
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var presenter: MainPresenter

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestUserPermissions()
    }

    private fun requestUserPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )

        var isAllPermissionsGranted = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) isAllPermissionsGranted = false
            }
            if (isAllPermissionsGranted) init() else ActivityCompat.requestPermissions(
                this,
                permissions,
                101
            )
        } else init()
    }

    fun init() {
        val view = binding.root
        MyApplication.configurarRealm()
        setContentView(view)
        configureToolbar()
        configureDrawer()
        configureNavController()
        inicializarPresenter()
    }

    private fun inicializarPresenter() {
        presenter = MainPresenter(this)
    }

    private fun configureToolbar() {
        setSupportActionBar(binding.toolbarMain)
    }

    private fun configureDrawer() {
        binding.navView.setNavigationItemSelectedListener(this)
        val toogle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbarMain,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        actionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(false)
        }
    }

    private fun configureNavController() {
        navController = findNavController(R.id.navHostFragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.dashboardFragment -> binding.toolbarMain.visibility = View.VISIBLE
            }
        }
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.toolbarMain.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Informa para respeitar a pilha do navHostFragment
        // O onSupportNavigateUp faz com que seja respeitado as activities no androidManifest, caso haja mais de uma
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.nav_dashboard -> navController.navigate(R.id.action_open_dashboard_fragment)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {
        var instance: MainActivity? = null

        val context: Context
            get() = instance!!.baseContext

        fun openToastShort(text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        fun openToastLong(text: String) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }
}