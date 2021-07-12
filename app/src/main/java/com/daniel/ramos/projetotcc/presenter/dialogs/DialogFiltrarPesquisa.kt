package com.daniel.ramos.projetotcc.presenter.dialogs

import android.app.Dialog
import android.content.DialogInterface
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.presenter.listeners.OnFiltroSelecionado
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.daniel.ramos.projetotcc.view.dialogs.DialogFiltrarPesquisaView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogFiltrarPesquisa(private val onFiltroSelecionado: OnFiltroSelecionado) {
    val view = DialogFiltrarPesquisaView()

    private val onButtonClick = DialogInterface.OnClickListener { dialog, which ->
        if (which == DialogInterface.BUTTON_POSITIVE){
            // TODO: Pegar dados do exercicio, data de inicio e fim para filtrar. Dispara error caso não esteja preenchido
            onFiltroSelecionado.executar(view.getExercicioSelecionado(), view.getDataInicio(), view.getDataFim())
        }

        if (which == DialogInterface.BUTTON_NEGATIVE){
            dialog.dismiss()
        }
    }

    val dialog: Dialog = MaterialAlertDialogBuilder(MainActivity.instance!!)
        .setTitle(R.string.title_filtrar_exercicio)
        .setView(view.getView())
        .setPositiveButton(R.string.aplicar_filtragem, onButtonClick)
        .setNegativeButton(R.string.cancelar, onButtonClick)
        .setCancelable(false)
        .create()

    fun exibir() {
        dialog.show()
    }
}