package com.daniel.ramos.projetotcc.presenter.dialogs

import android.app.AlertDialog
import android.content.DialogInterface.OnShowListener
import android.widget.Button
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.presenter.listeners.OnPacienteAdicionado
import com.daniel.ramos.projetotcc.presenter.utils.InputValidatorUtils
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.dialogs.DialogAddPacienteView

class DialogAddPaciente(private val onPacienteAdicionado: OnPacienteAdicionado) {
    val view = DialogAddPacienteView()
    private lateinit var dialog: AlertDialog

    init {
        configureDialog()
    }

    private fun configureDialog() {
        dialog = AlertDialog.Builder(MainActivity.instance!!)
            .setTitle(R.string.addPacienteTitle)
            .setView(view.getView())
            .setPositiveButton(R.string.confirmar, null)
            .setNegativeButton(R.string.cancelar, null)
            .setCancelable(false)
            .create()
        dialog.setOnShowListener(OnShowListener {
            val b: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            b.setOnClickListener {
                if (validarCampos()) {
                    val paciente = Paciente()
                    paciente.nome = view.getNome()
                    paciente.sexo = view.getSexoSelecionado()
                    paciente.dataNascimento = view.getDataNascimento()
                    onPacienteAdicionado.novoPaciente(paciente)
                    dialog.dismiss()
                }
            }

            val n: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            n.setOnClickListener {
                dialog.dismiss()
            }
        })
    }


    fun exibir() {
        dialog.show()
    }

    private fun validarCampos(): Boolean {
        var isValid = true
        if (!InputValidatorUtils.validarMaterialTextInput(view.getViewNome()))
            isValid = false
        if (!InputValidatorUtils.validarMaterialTextInput(view.getViewDataNascimento()))
            isValid = false
        if (!InputValidatorUtils.validarRadioGroup(view.getViewSexo()))
            isValid = false
        return isValid
    }
}