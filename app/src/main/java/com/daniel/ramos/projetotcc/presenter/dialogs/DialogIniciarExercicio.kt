package com.daniel.ramos.projetotcc.presenter.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.widget.Toast
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.model.entities.Exercicio
import com.daniel.ramos.projetotcc.presenter.BluetoothServiceA
import com.daniel.ramos.projetotcc.presenter.listeners.OnExercicioIniciado
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.dialogs.DialogIniciarExercicioView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogIniciarExercicio(
    exercicio: Exercicio,
    onExercicioIniciado: OnExercicioIniciado
) {
    val view = DialogIniciarExercicioView()

    private val onButtonClick = DialogInterface.OnClickListener { dialog, which ->
        if (which == DialogInterface.BUTTON_POSITIVE){
            if (view.isPacienteSelecionado()) {
                val blueStatus = MainActivity.statusBlueDevice.value
                if (blueStatus == BluetoothServiceA.STATE_CONNECTED) {
                    onExercicioIniciado.iniciar(exercicio, view.getPacienteSelecionado())
                    dialog.dismiss()
                } else {
                    MainActivity.openToastShort("Verifique sua conexão com FitSpot")
                }
            } else {
                MainActivity.openToastShort("Selecione um paciente")
            }
        }

        if (which == DialogInterface.BUTTON_NEGATIVE){
            dialog.dismiss()
        }
    }

    val dialog: Dialog = MaterialAlertDialogBuilder(MainActivity.instance!!)
        .setTitle(R.string.titleIniciarExecicio)
        .setView(view.getView())
        .setPositiveButton(R.string.iniciar_exercicio, onButtonClick)
        .setNegativeButton(R.string.cancelar, onButtonClick)
        .setCancelable(false)
        .create()

    fun exibir() {
        dialog.show()
    }
}
