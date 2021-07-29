package com.daniel.ramos.projetotcc.presenter.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.widget.Button
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.presenter.listeners.OnFiltroSelecionado
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.dialogs.DialogFiltrarPesquisaView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogFiltrarPesquisa(private val onFiltroSelecionado: OnFiltroSelecionado) {
    val view = DialogFiltrarPesquisaView()
    private lateinit var dialog: AlertDialog

    init {
        configureDialog()
    }

    private val onButtonClick = DialogInterface.OnClickListener { dialog, which ->
        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (isValido()) {
                view.removerErros()
                onFiltroSelecionado.executar(
                    view.getExercicioSelecionado(),
                    view.getDataInicio(),
                    view.getDataFim()
                )
            }
        }

        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss()
        }
    }

    private fun isValido(): Boolean {
        var isValid = true
        if (view.getExercicioSelecionado() == null) {
            isValid = false
            view.setErrorExercicio()
        }

        if (view.getDataFim() == null || view.getDataFim() == null) {
            isValid = false
            view.setErrorData()
        }
        return isValid
    }


    private fun configureDialog() {
        dialog = AlertDialog.Builder(MainActivity.instance!!)
            .setTitle(R.string.title_filtrar_exercicio)
            .setView(view.getView())
            .setPositiveButton(R.string.aplicar_filtragem, null)
            .setNegativeButton(R.string.cancelar, null)
            .setCancelable(false)
            .create()

        dialog.setOnShowListener(DialogInterface.OnShowListener {
            val b: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            b.setOnClickListener {
                if (isValido()) {
                    view.removerErros()
                    onFiltroSelecionado.executar(
                        view.getExercicioSelecionado(),
                        view.getDataInicio(),
                        view.getDataFim()
                    )
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
}