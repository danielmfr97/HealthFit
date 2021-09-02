package com.daniel.ramos.projetotcc.presenter.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.RowExercicioBinding
import com.daniel.ramos.projetotcc.model.entities.Exercicio
import com.daniel.ramos.projetotcc.model.repositories.ExercicioRepository
import com.daniel.ramos.projetotcc.model.repositories.RealmRepository
import com.daniel.ramos.projetotcc.presenter.dialogs.DialogIniciarExercicio
import com.daniel.ramos.projetotcc.presenter.listeners.OnExercicioIniciado
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import kotlin.contracts.contract

class ExerciciosAdapter(val exercicios: RealmResults<Exercicio>, autoUpdate: Boolean) :
    RealmRecyclerViewAdapter<Exercicio, ExerciciosAdapter.ViewHolder>(exercicios, autoUpdate) {
    lateinit var onExercicioIniciado: OnExercicioIniciado

    constructor(exercicios: RealmResults<Exercicio>, autoUpdate: Boolean, onExercicioIniciado: OnExercicioIniciado): this(exercicios, autoUpdate) {
        this.onExercicioIniciado = onExercicioIniciado
    }

    private var _binding: RowExercicioBinding? = null
    private val binding get() = _binding!!
    private val exercicioRepository = ExercicioRepository()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomeExercicio = binding.tvExercicioNome
        var tipoExercicio = binding.tipoExercicio
        var numCiclos = binding.numeroCiclos
        var tempoRandom = binding.tempoRandom
        var timeoutDelay = binding.timeoutDelay
        var fitSpots = binding.numFitSpotsAtivos
        var deletarItem = binding.ivDelete
        var iniciarExercicio = binding.iniciarExercicio
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding =
            RowExercicioBinding.inflate(LayoutInflater.from(MainActivity.instance), parent, false)
        return ViewHolder(binding.root)
    }

    @SuppressLint("SetTextI18n", "StringFormatMatches")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercicio: Exercicio? = getItem(position)
        holder.nomeExercicio.text = exercicio!!.nomeExercicio
        holder.tipoExercicio.text =
            MainActivity.instance!!.getString(R.string.tipoExercicio, exercicio.tipoExercicio)
        if (exercicio.ciclosExercicio == null || exercicio.ciclosExercicio == 0) {
            holder.tempoRandom.visibility = View.VISIBLE
            holder.tempoRandom.text =
                MainActivity.instance!!.getString(R.string.tempoRandom, exercicio.tempoRandom)
        }
        else {
            holder.numCiclos.visibility = View.VISIBLE
            holder.numCiclos.text = MainActivity.instance!!.getString(
                R.string.numeroDeCiclos,
                exercicio.ciclosExercicio
            )
        }
        holder.fitSpots.text = "FitSpots: 1-${sensorOnOrOff(exercicio.sensor1!!)} 2-${sensorOnOrOff(exercicio.sensor2!!)} 3-${sensorOnOrOff(exercicio.sensor3!!)} 4-${sensorOnOrOff(exercicio.sensor4!!)}"
        holder.timeoutDelay.text = if (exercicio.timeOutSensor!!) MainActivity.instance!!.getString(
            R.string.timeoutDelay,
            exercicio.timeout
        )
        else "Desabilitado"
        holder.deletarItem.setOnClickListener{
            openDialogDeletarExercicio(exercicio.nomeExercicio, position)
        }
        holder.iniciarExercicio.setOnClickListener {
            openDialogIniciarExercicio(exercicio)
        }
    }

    private fun sensorOnOrOff(boolean: Boolean): String {
        var result = "On"
        if (!boolean)
            result = "Off"
        return result
    }

    override fun getItemCount(): Int {
        return exercicios.size
    }

    private fun openDialogDeletarExercicio(exercicioNome: String, position: Int) {
        val dialog: Dialog = MaterialAlertDialogBuilder(MainActivity.instance!!)
            .setTitle(R.string.deletarExercicio)
            .setMessage("O exercício $exercicioNome será deletado. Deseja continuar com esta ação?")
            .setPositiveButton(R.string.confirmar) { dialogInterface, i ->
                exercicioRepository.atualizarObject(object : RealmRepository.AtualizarListener {
                    override fun atualizar() {
                        exercicios.deleteFromRealm(position)
                    }
                })
                notifyItemRemoved(position)
                dialogInterface.dismiss()
            }
            .setNegativeButton(R.string.cancelar) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .setCancelable(false)
            .create()
        dialog.show()
    }

    private fun openDialogIniciarExercicio(exercicio: Exercicio) {
        DialogIniciarExercicio(exercicio, onExercicioIniciado).exibir()
    }
}