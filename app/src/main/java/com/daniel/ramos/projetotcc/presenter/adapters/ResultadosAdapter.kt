package com.daniel.ramos.projetotcc.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.daniel.ramos.projetotcc.databinding.RowResultadoBinding
import com.daniel.ramos.projetotcc.model.entities.Resultado
import com.daniel.ramos.projetotcc.model.factories.ModelFactory
import com.daniel.ramos.projetotcc.model.repositories.ResultadoRepository
import com.daniel.ramos.projetotcc.presenter.utils.DateUtils
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import java.util.*
import kotlin.properties.Delegates


class ResultadosAdapter(private var resultados: RealmResults<Resultado>, autoUpdate: Boolean) :
    RealmRecyclerViewAdapter<Resultado, ResultadosAdapter.ViewHolder>(resultados, autoUpdate),
    Filterable {

    private var _binding: RowResultadoBinding? = null
    private val binding get() = _binding!!
    private val exercicioModel = ModelFactory.getExercicioModel
    private val resultadoRepository = ResultadoRepository()

    private var queryExercicio: String? = null
    private var queryDataIni: Long? = null
    private var queryDataFim: Long? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomeExercicio = binding.nomeExercicio
        var numAcertos = binding.numAcertos
        var numErros = binding.numErros
        var tempoTotal = binding.tempoTotal
        var dataCriado = binding.dataCriado
    }

    inner class ResultadosFilter(private val mAdapter: ResultadosAdapter) : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return FilterResults()
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            mAdapter.filterResults()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding =
            RowResultadoBinding.inflate(LayoutInflater.from(MainActivity.instance), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultado: Resultado? = getItem(position)
        if (resultado != null) {
            changeVisibilityViews(resultado)
            val exercicio = exercicioModel.getExercicioPorId(resultado.exercicio_id)
            holder.nomeExercicio.text = exercicio?.nomeExercicio
            holder.numAcertos.text = resultado.acertos
            holder.numErros.text = resultado.erros
            holder.tempoTotal.text = resultado.tempo_total
            holder.dataCriado.text = DateUtils.convertDateToString(Date(resultado.created))
        }
    }

    override fun getItemCount(): Int {
        return resultados.size
    }

    private fun changeVisibilityViews(resultado: Resultado) {
        binding.labelAcertos.visibility = if (resultado.acertos != null) View.VISIBLE else View.GONE
        binding.numAcertos.visibility = if (resultado.acertos != null) View.VISIBLE else View.GONE
        binding.labelErros.visibility = if (resultado.erros != null) View.VISIBLE else View.GONE
        binding.numErros.visibility = if (resultado.erros != null) View.VISIBLE else View.GONE
    }

    fun filtrarDados(_queryExercicio: String?, _queryDataIni: Long?, _queryDataFim: Long?) {
        queryExercicio = _queryExercicio
        queryDataIni = _queryDataIni
        queryDataFim = _queryDataFim
        filterResults()
    }

    private fun filterResults() {
        val data = resultadoRepository.queryResultados(
            queryExercicio?.toLowerCase(Locale.ROOT)?.trim(),
            queryDataIni,
            queryDataFim
        )
        resultados = data
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return ResultadosFilter(this)
    }

}