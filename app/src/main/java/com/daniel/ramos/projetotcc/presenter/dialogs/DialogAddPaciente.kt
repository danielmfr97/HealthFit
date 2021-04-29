package com.daniel.ramos.projetotcc.presenter.dialogs

import android.app.Dialog
import android.content.DialogInterface
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.presenter.OnPacienteAdicionado
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.dialogs.DialogAddPacienteView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogAddPaciente(private val onPacienteAdicionado: OnPacienteAdicionado) {
    val view = DialogAddPacienteView()

    private val onButtonClick = DialogInterface.OnClickListener { dialog, which ->
        if (which == DialogInterface.BUTTON_POSITIVE) {
            val paciente = Paciente()
            paciente.nome = view.getNome()
            paciente.sexo = view.getSexoSelecionado()
            paciente.dataNascimento = view.getDataNascimento()
            onPacienteAdicionado.novoPaciente(paciente)
        }
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss()
        }
    }

    val dialog: Dialog = MaterialAlertDialogBuilder(MainActivity.instance!!)
        .setTitle(R.string.addPacienteTitle)
        .setView(view.getView())
        .setPositiveButton(R.string.confirmar, onButtonClick)
        .setNegativeButton(R.string.cancelar, onButtonClick)
        .setCancelable(false)
        .create()

    fun exibir() {
        dialog.show()
    }

}