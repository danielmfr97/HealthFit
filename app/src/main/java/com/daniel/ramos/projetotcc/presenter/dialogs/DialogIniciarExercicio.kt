package com.daniel.ramos.projetotcc.presenter.dialogs

import android.app.Dialog
import android.content.DialogInterface
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.dialogs.DialogIniciarExercicioView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogIniciarExercicio {
    val view = DialogIniciarExercicioView()

    private val onButtonClick = DialogInterface.OnClickListener { dialog, which ->
        if (which == DialogInterface.BUTTON_POSITIVE){
            //TODO: ABrir fragment de start da atividade passando no bundle o paciente
        }

        if (which == DialogInterface.BUTTON_NEGATIVE){

        }
    }

    val dialog: Dialog = MaterialAlertDialogBuilder(MainActivity.instance!!)
        .setTitle(R.string.titleIniciarExecicio)
        .setView(view.getView())
        .setPositiveButton(R.string.confirmar, onButtonClick)
        .setNegativeButton(R.string.cancelar, onButtonClick)
        .setCancelable(false)
        .create()

    fun exibir() {
        dialog.show()
    }
}
