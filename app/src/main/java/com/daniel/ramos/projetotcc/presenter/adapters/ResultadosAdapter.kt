package com.daniel.ramos.projetotcc.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.ramos.projetotcc.databinding.RowResultadoBinding
import com.daniel.ramos.projetotcc.model.entities.Resultado
import com.daniel.ramos.projetotcc.model.repositories.ResultadoRepository
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class ResultadosAdapter(private val resultados: RealmResults<Resultado>, autoUpdate: Boolean) :
    RealmRecyclerViewAdapter<Resultado, ResultadosAdapter.ViewHolder>(resultados, autoUpdate) {

    private var _binding: RowResultadoBinding? = null
    private val binding get() = _binding!!
    private val resultadoRepository = ResultadoRepository()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tipoExercicio = binding.tipoExercicio
        var numAcertos = binding.numAcertos
        var numErros = binding.numErros
        var velAcaoMedia = binding.velAcaoMedia
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding =
            RowResultadoBinding.inflate(LayoutInflater.from(MainActivity.instance), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultado: Resultado? = getItem(position)
        holder.tipoExercicio.text = resultado!!.tipo_exercicio
        holder.numAcertos.text = resultado.acertos
        holder.numErros.text = resultado.erros
        holder.velAcaoMedia.text = resultado.velocidade_acao_media
    }

    override fun getItemCount(): Int {
        return resultados.size
    }
}