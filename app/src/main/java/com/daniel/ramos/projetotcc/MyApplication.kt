package com.daniel.ramos.projetotcc

import android.app.Application
import com.daniel.ramos.projetotcc.presenter.BlueComm
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {

    lateinit var myBlueComm: BlueComm

    override fun onCreate() {
        super.onCreate()
        //TODO: Configurar realm ou Room
        //TODO: Inicializar firebase
    }

    private fun initRealm() {
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .name("projetotcc.realm").build()
        Realm.setDefaultConfiguration(configuration)
    }
}