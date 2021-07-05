package com.daniel.ramos.projetotcc.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.ramos.projetotcc.databinding.RowResultadoBinding
import com.daniel.ramos.projetotcc.model.entities.Resultado
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.model.repositories.ResultadoRepository
import com.daniel.ramos.projetotcc.presenter.utils.DateUtils
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class ResultadosAdapter(private val resultados: RealmResults<Resultado>, autoUpdate: Boolean) :
    RealmRecyclerViewAdapter<Resultado, ResultadosAdapter.ViewHolder>(resultados, autoUpdate) {

    private var _binding: RowResultadoBinding? = null
    private val binding get() = _binding!!
    private val resultadoRepository = ResultadoRepository()
    private val exercicioModel = ModelFactory.getExercicioModel

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomeExercicio = binding.nomeExercicio
        var numAcertos = binding.numAcertos
        var numErros = binding.numErros
        var tempoTotal = binding.tempoTotal
        var dataCriado = binding.dataCriado
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding =
            RowResultadoBinding.inflate(LayoutInflater.from(MainActivity.instance), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultado: Resultado? = getItem(position)
        changeVisibilityViews(resultado!!)
        val exercicio = exercicioModel.getExercicioPorId(resultado.exercicio_id)
        holder.nomeExercicio.text = exercicio?.nomeExercicio
        holder.numAcertos.text = resultado.acertos
        holder.numErros.text = resultado.erros
        holder.tempoTotal.text = resultado.tempo_total
        holder.dataCriado.text =  DateUtils.convertDateToString(resultado?.created)
    }

    override fun getItemCount(): Int {
        return resultados.size
    }

    private fun changeVisibilityViews(resultado: Resultado) {
        binding.labelAcertos.visibility = if(resultado.acertos != null) View.VISIBLE else View.GONE
        binding.numAcertos.visibility = if(resultado.acertos != null) View.VISIBLE else View.GONE
        binding.labelErros.visibility = if(resultado.erros != null) View.VISIBLE else View.GONE
        binding.numErros.visibility = if(resultado.erros != null) View.VISIBLE else View.GONE
    }
}