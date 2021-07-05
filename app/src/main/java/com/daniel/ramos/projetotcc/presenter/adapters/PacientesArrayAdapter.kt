package com.daniel.ramos.projetotcc.presenter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.daniel.ramos.projetotcc.databinding.ListPacientesBinding
import com.daniel.ramos.projetotcc.model.entities.Paciente
import com.daniel.ramos.projetotcc.presenter.listeners.OnPacienteSelecionado
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import io.realm.RealmResults

class PacientesArrayAdapter(
    context: Context,
    resource: Int,
    private val values: RealmResults<Paciente>,
    private val onPacienteSelecionado: OnPacienteSelecionado
) :
    ArrayAdapter<Paciente>(context, resource, values) {

    private class ViewHolder internal constructor(binding: ListPacientesBinding) {
        var view: View = binding.root
        val binding: ListPacientesBinding = binding
    }

    override fun getItem(position: Int): Paciente? = values[position]

    override fun getCount(): Int {
        return values.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        if (convertView == null) {
            val itemBinding = ListPacientesBinding.inflate(LayoutInflater.from(context), parent, false)
            holder = ViewHolder(itemBinding)
            holder.view = itemBinding.root
            holder.view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val paciente: Paciente? = values[position]

        holder.binding.parentView.setOnClickListener {
            Toast.makeText(MainActivity.context, "Paciente selecionado ${paciente!!.nome}", Toast.LENGTH_SHORT).show()
            onPacienteSelecionado.executar(values[position]!!)
        }
        holder.binding.index.text = (position + 1).toString()
        holder.binding.nomePaciente.text = paciente?.nome

        return holder.view
    }
}