package com.daniel.ramos.projetotcc.presenter.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesClass(val context: Context) {
    val PREFERENCE_NAME = "PREFERENCE_DATA"
    private var sharedpreferences: SharedPreferences? =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    /**
     * Método usado para verificar se é a primeira vez que o aplicativo é rodado, assim
     * cancelamos algumas operações que só devem aparecer uma vez quando o usuário instala o sistema (ou limpa os dados do aplicativo)
     */
    fun getFirstRunSpotlight(): Boolean {
        return sharedpreferences!!.getBoolean("firstRunSpotlight", true)
    }

    fun setFirstRunSpotlight(isFirstRun: Boolean) {
        val editor = sharedpreferences!!.edit()
        editor.putBoolean("firstRunSpotlight", isFirstRun)
        editor.apply()
    }

    fun getFirstRunDashboardSpotlight(): Boolean {
        return sharedpreferences!!.getBoolean("firstRunDashboardSpotlight", true)
    }

    fun setFirstRunDashboardSpotlight(isFirstRun: Boolean) {
        val editor = sharedpreferences!!.edit()
        editor.putBoolean("firstRunDashboardSpotlight", isFirstRun)
        editor.apply()
    }
}