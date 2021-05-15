package com.daniel.ramos.projetotcc

import android.app.Application
import android.content.Intent
import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA
import io.realm.Realm
import io.realm.RealmConfiguration

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
            val configuration = RealmConfiguration.Builder()
                .name("projetotcc.realm").build()
            Realm.setDefaultConfiguration(configuration)
        }
    }
}