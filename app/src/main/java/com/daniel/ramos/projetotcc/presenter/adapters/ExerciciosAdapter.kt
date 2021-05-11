package com.daniel.ramos.projetotcc.presenter.adapters

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
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class ExerciciosAdapter(private val exercicios: RealmResults<Exercicio>, autoUpdate: Boolean) :
    RealmRecyclerViewAdapter<Exercicio, ExerciciosAdapter.ViewHolder>(exercicios, autoUpdate) {

    private var _binding: RowExercicioBinding? = null
    private val binding get() = _binding!!
    private val exercicioRepository = ExercicioRepository()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomeExercicio = binding.tvExercicioNome
        var numFitSpots = binding.numFitSpots
        var tipoExercicio = binding.tipoExercicio
        var duracaoExercicio = binding.duracaoExercicio
        var timeoutDelay = binding.timeoutDelay
        var deletarItem = binding.ivDelete
        var iniciarExercicio = binding.iniciarExercicio
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding =
            RowExercicioBinding.inflate(LayoutInflater.from(MainActivity.instance), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercicio: Exercicio? = getItem(position)
        holder.nomeExercicio.text = exercicio!!.nomeExercicio
        holder.numFitSpots.text =
            MainActivity.instance!!.getString(R.string.numeroDeFitSpots, exercicio.sensoresUsados)
        holder.tipoExercicio.text =
            MainActivity.instance!!.getString(R.string.tipoExercicio, exercicio.tipoExericicio)
        holder.duracaoExercicio.text =
            MainActivity.instance!!.getString(R.string.duracaoExercicio, exercicio.exercicioDuracao)
        holder.timeoutDelay.text = if (exercicio.timeOutSensor!!) MainActivity.instance!!.getString(
            R.string.timeoutDelay,
            exercicio.timeout
        )
        else "Desabilitado"
        holder.deletarItem.setOnClickListener{
            openDialogDeletarExercicio(exercicio.nomeExercicio, position)
        }
        holder.iniciarExercicio.setOnClickListener {
            //TODO: CONFIGURAr dialog para selecionar um paciente e entao iniciar
        }
    }

    override fun getItemCount(): Int {
        return exercicios.size
    }

    private fun openDialogDeletarExercicio(exercicioNome: String, position: Int) {
        val dialog: Dialog = MaterialAlertDialogBuilder(MainActivity.instance!!)
            .setTitle(R.string.deletarPaciente)
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
}