package com.daniel.ramos.projetotcc

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        //TODO: Configurar realm ou Room
        //TODO: Inicializar firebase
    }

    companion object {
        fun configurarRealm() {
            val configuration = RealmConfiguration.Builder()
                .name("projetotcc.realm").build()
            Realm.setDefaultConfiguration(configuration)
        }
    }
}