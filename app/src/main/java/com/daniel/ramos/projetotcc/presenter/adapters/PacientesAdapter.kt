package com.daniel.ramos.projetotcc.presenter.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.RowPacienteBinding
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.model.repositories.PacienteRepository
import com.daniel.ramos.projetotcc.model.repositories.RealmRepository
import com.daniel.ramos.projetotcc.presenter.utils.AgeCalculator
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class PacientesAdapter(private val pacientes: RealmResults<Paciente>, autoUpdate: Boolean) :
    RealmRecyclerViewAdapter<Paciente, PacientesAdapter.ViewHolder>(pacientes, autoUpdate) {

    private var _binding: RowPacienteBinding? = null
    private val binding get() = _binding!!
    private val pacienteRepository = PacienteRepository()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nome = binding.tvNome
        var sexo = binding.tvSexo
        var idade = binding.tvIdade
        var delete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding =
            RowPacienteBinding.inflate(LayoutInflater.from(MainActivity.instance), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paciente: Paciente? = getItem(position)
        holder.nome.text = paciente!!.nome
        holder.sexo.text = paciente.sexo
        holder.idade.text = MainActivity.instance!!.getString(
            R.string.idadePaciente,
            AgeCalculator.calcularIdadePorData(paciente.dataNascimento)
        )
        holder.delete.setOnClickListener {
            openDialogDeletarPaciente(paciente.nome, position)
        }
    }

    override fun getItemCount(): Int {
        return pacientes.size
    }

    private fun openDialogDeletarPaciente(pacienteNome: String, position: Int) {
        val dialog: Dialog = MaterialAlertDialogBuilder(MainActivity.instance!!)
            .setTitle(R.string.deletarPaciente)
            .setMessage("O paciente $pacienteNome será deletado. Deseja continuar com esta ação?")
            .setPositiveButton(R.string.confirmar) { dialogInterface, i ->
                pacienteRepository.atualizarObject(object : RealmRepository.AtualizarListener {
                    override fun atualizar() {
                        pacientes.deleteFromRealm(position)
                    }
                })
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
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