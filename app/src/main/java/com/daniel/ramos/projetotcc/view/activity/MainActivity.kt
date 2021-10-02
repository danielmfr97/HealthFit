package com.daniel.ramos.projetotcc.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.daniel.ramos.projetotcc.MyApplication
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.ActivityMainBinding
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA
import com.daniel.ramos.projetotcc.presenter.ConfigurarAppPresenter
import com.daniel.ramos.projetotcc.presenter.MainPresenter
import com.daniel.ramos.projetotcc.presenter.utils.Constants
import com.google.android.material.navigation.NavigationView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val REQUEST_CONNECT_DEVICE_SECURE = 1
    private val REQUEST_CONNECT_DEVICE_INSECURE = 2
    private val REQUEST_ENABLE_BT = 3

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var presenter: MainPresenter
    private var bluetoothServiceInst: BluetoothServiceA? = null
    private var deviceName = ""

    private var menuInflater: Menu? = null

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureToolbar()
        configureDrawer()
        configureNavController()
        requestUserPermissions()
    }

    override fun onStart() {
        super.onStart()
        inicializarServiceBluetooth()
    }

    override fun onResume() {
        super.onResume()
        bluetoothServiceInst!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
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
        MyApplication.configurarRealm()
        inicializarPresenter()
    }

    private fun inicializarServiceBluetooth() {
        initBluetooth()
        val bluetoothFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        val bluetoothFilter2 = IntentFilter()
        bluetoothFilter2.apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }
        context.registerReceiver(broadcastReceiverOnOffBT, bluetoothFilter)
        context.registerReceiver(broadcastReceiverActionState, bluetoothFilter2)
    }

    private val broadcastReceiverOnOffBT = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action!! == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_OFF -> Log.d(TAG, "onReceive: STATE OFF")
                    BluetoothAdapter.STATE_TURNING_OFF -> Log.d(
                        TAG,
                        "mBroadcastReceiver1: STATE TURNING OFF"
                    )
                    BluetoothAdapter.STATE_ON -> {
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON")
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> Log.d(
                        TAG,
                        "mBroadcastReceiver1: STATE TURNING ON"
                    )
                }
            }
        }
    }

    private val broadcastReceiverActionState = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            when (intent.action) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> Log.d(TAG, "Device connected: $device")
                BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED -> Log.d(
                    TAG,
                    "Device disconecting: $device"
                )
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    setStatus("Conecte-se a um FitSpot")
                    Log.d(TAG, "Device disconnected: $device")
                }
            }
        }
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
        bluetoothServiceInst!!.setmHandler(mHandler)
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
            R.id.nav_dashboard -> navController.navigate(R.id.dashboardFragment)
            R.id.nav_meus_pacientes -> navController.navigate(R.id.meusPacientesFragment)
            R.id.nav_reports -> navController.navigate(R.id.relatoriosFragment)
            R.id.nav_exercicios -> navController.navigate(R.id.exerciciosFragment)
            R.id.nav_aboutus -> navController.navigate(R.id.aboutUsFragment)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init()
                } else {
                    closeNow()
                }
            }
        }
    }

    private fun closeNow() {
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater = menu
        val menuInflater = getMenuInflater()
        return true
    }

    private fun setStatus(subTitle: CharSequence) {
        binding.toolbarMain.subtitle = subTitle
    }

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.MESSAGE_STATE_CHANGE -> when (msg.arg1) {
                    BluetoothServiceA.STATE_CONNECTED -> {
                        setStatus(getString(R.string.title_connected_to, deviceName))
                        openToastShort("Conectado com sucesso!")
                        statusBlueDevice.value = BluetoothServiceA.STATE_CONNECTED
                    }
                    BluetoothServiceA.STATE_CONNECTING -> {
                        setStatus(getString(R.string.title_connecting))
                        statusBlueDevice.value = BluetoothServiceA.STATE_CONNECTING
                    }
                    BluetoothServiceA.STATE_LISTEN, BluetoothServiceA.STATE_NONE -> {
                        setStatus(getString(R.string.title_not_connected))
                        statusBlueDevice.value = BluetoothServiceA.STATE_LISTEN
                    }
                }
                Constants.MESSAGE_DEVICE_NAME -> {
                    deviceName = msg.data.getString(Constants.DEVICE_NAME).toString()
                    supportActionBar?.subtitle = "Conectado a $deviceName"
                }
                Constants.MESSAGE_TOAST -> {
                    val string = msg.data.getString(Constants.TOAST).toString()
                    statusBlueDevice.value = BluetoothServiceA.STATE_NONE
                    openToastShort("Falha: $string")
                }
                Constants.MESSAGE_DEVICE_OFFLINE -> {
                    menuInflater?.getItem(0)?.icon =
                        ContextCompat.getDrawable(context, R.drawable.ic_bluetooth_off)
                    binding.toolbarMain.menu.getItem(0).title = "Desconectado"
                }
            }
        }
    }

    companion object {
        @JvmStatic
        var instance: MainActivity? = null

        @JvmStatic
                /**
                 * Variável que armazena o status da nossa comunicação Bluetooth
                 * 0 - Sem conexões ou ações
                 * 1 - Escutando possíveis conexões
                 * 2 - Iniciando a comunicação
                 * 3 - Conectado a um dispositivo
                 */
        var statusBlueDevice: MutableLiveData<Int> = MutableLiveData(0)

        @JvmStatic
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