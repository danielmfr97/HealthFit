package com.daniel.ramos.projetotcc.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.daniel.ramos.projetotcc.MyApplication
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.ActivityMainBinding
import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA
import com.daniel.ramos.projetotcc.presenter.ConfigurarAppPresenter
import com.daniel.ramos.projetotcc.presenter.MainPresenter
import com.daniel.ramos.projetotcc.presenter.factory.ModelFactory
import com.daniel.ramos.projetotcc.presenter.utils.Constants
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val REQUEST_CONNECT_DEVICE_SECURE = 1
    private val REQUEST_CONNECT_DEVICE_INSECURE = 2
    private val REQUEST_ENABLE_BT = 3

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var presenter: MainPresenter
    private var bluetoothServiceInst: BluetoothServiceA? = null

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestUserPermissions()
    }

    override fun onStart() {
        super.onStart()
        inicializarServiceBluetooth()
    }

    override fun onResume() {
        super.onResume()
        bluetoothServiceInst!!.start(mHandler)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, BluetoothServiceA::class.java))
        ConfigurarAppPresenter(null).apply {
            unregisterReceiver(broadcastBondStateBT)
            unregisterReceiver(broadcastDiscoverBTDevices)
            unregisterReceiver(broadcastDiscoverable)
        }
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
        setContentView(view)
        configureToolbar()
        configureDrawer()
        configureNavController()
        inicializarPresenter()
    }

    private fun inicializarServiceBluetooth() {
        startService(Intent(this, BluetoothServiceA::class.java))
        initBluetooth()
    }

    // If BT is not on, request that it be enabled.
    // setupChat() will then be called during onActivityResult
    private fun initBluetooth() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(
                enableIntent,
                REQUEST_ENABLE_BT
            )
            // Otherwise, get bluetoothServiceInstance
        } else if (bluetoothServiceInst == null) {
            setupChat()
        }
    }

    /**
     * Set up the UI and background operations for communication.
     */
    private fun setupChat() {
        // Initialize the BluetoothChatService to perform bluetooth connections
        bluetoothServiceInst = ModelFactory.getBluetoothServiceA
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

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            val activity = this
            when (msg.what) {
                Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
                    BluetoothServiceA.STATE_CONNECTED -> {
                        openToastShort("Device conectado")
                    }
                    BluetoothServiceA.STATE_CONNECTING -> openToastShort("conectando")
                    BluetoothServiceA.STATE_LISTEN, BluetoothServiceA.STATE_NONE -> openToastShort(
                        "Não conectado"
                    )
                }
                Constants.MESSAGE_WRITE -> {
                    val writeBuf = msg.obj as ByteArray
                    // construct a string from the buffer
                    val writeMessage = String(writeBuf)
                    openToastShort("Mensagem: $writeMessage")
                }
                Constants.MESSAGE_READ -> {
                    val readBuf = msg.obj as ByteArray
                    // construct a string from the valid bytes in the buffer
                    val readMessage = String(readBuf, 0, msg.arg1)
                }
                Constants.MESSAGE_DEVICE_NAME -> {
                }
                Constants.MESSAGE_TOAST -> if (null != activity) {
                }
            }
        }
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