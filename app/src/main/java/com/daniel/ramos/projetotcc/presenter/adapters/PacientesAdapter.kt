package com.daniel.ramos.projetotcc.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.RowPacienteBinding
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.presenter.utils.AgeCalculator
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class PacientesAdapter(private val pacientes: RealmResults<Paciente>, autoUpdate: Boolean) :
    RealmRecyclerViewAdapter<Paciente, PacientesAdapter.ViewHolder>(pacientes, autoUpdate) {

    private var _binding: RowPacienteBinding? = null
    private val binding get() = _binding!!

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nome = binding.tvNome
        var sexo = binding.tvSexo
        var idade = binding.tvIdade
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
        holder.idade.text = MainActivity.instance!!.getString(R.string.idadePaciente, AgeCalculator.calcularIdadePorData(paciente.dataNascimento))
    }

    override fun getItemCount(): Int {
        return pacientes.size
    }
}