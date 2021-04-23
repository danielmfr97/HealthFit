package com.daniel.ramos.projetotcc

import android.app.Application
import com.daniel.ramos.projetotcc.presenter.BlueComm
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {

    lateinit var myBlueComm: BlueComm

    override fun onCreate() {
        super.onCreate()
        inicializarBluetoothComm()
        //TODO: Configurar realm ou Room
        //TODO: Inicializar firebase
    }

    // Desta forma eu crio uma instância da comunicação bluetooth que não será destruida em uma fragmento/activity
    // Como é um objeto público bastara acessar seus metodos recuperando o applicationContenxt
    private fun inicializarBluetoothComm() {
        myBlueComm = BlueComm()
    }

    private fun initRealm() {
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .name("projetotcc.realm").build()
        Realm.setDefaultConfiguration(configuration)
    }
}