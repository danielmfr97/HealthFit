package com.daniel.ramos.projetotcc

import android.app.Application
import android.content.Intent
import com.daniel.ramos.projetotcc.model.database.RealmUpgrade
import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA
import io.realm.Realm

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        inicializarBluetoothService();
        //TODO: Configurar realm ou Room
        //TODO: Inicializar firebase
    }

    private fun inicializarBluetoothService() {
        startService(Intent(this, BluetoothServiceA::class.java))
    }

    companion object {
        fun configurarRealm() {
            val configuration =
                if (!BuildConfig.DEBUG)
                    RealmUpgrade.getRealmConfigNoMigration()
                else RealmUpgrade.getRealmConfig()
            Realm.setDefaultConfiguration(configuration)
        }
    }
}