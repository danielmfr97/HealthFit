package com.daniel.ramos.projetotcc.presenter


import com.daniel.ramos.projetotcc.MyApplication
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.fragment.ConfigurarAppFragment

class ConfigurarAppPresenter(private val view: ConfigurarAppFragment) {
    val blueComm = (MainActivity.context.applicationContext as MyApplication).myBlueComm

}
