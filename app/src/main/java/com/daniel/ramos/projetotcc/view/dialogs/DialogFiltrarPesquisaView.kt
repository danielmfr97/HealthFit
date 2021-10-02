package com.daniel.ramos.projetotcc.view.dialogs

import android.annotation.SuppressLint
import android.text.InputType
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.util.Pair
import com.daniel.ramos.projetotcc.R
import com.daniel.ramos.projetotcc.databinding.DialogFiltrarPesquisaBinding
import com.daniel.ramos.projetotcc.presenter.enums.TipoExercicio
import com.daniel.ramos.projetotcc.presenter.utils.DateUtils
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlin.properties.Delegates

class DialogFiltrarPesquisaView {
    private var binding = DialogFiltrarPesquisaBinding.inflate(
        MainActivity.instance!!.layoutInflater
    )
    val view = binding.root

    private var exercicioIdSelecionado: String? = null
    private var _dataFim: Long? = null
    private var _dataInicio: Long? = null

    init {
        configurarSpinnerExercicios()
        configurarDateRange()
    }

    private fun configurarSpinnerExercicios() {
        val items = listOf(TipoExercicio.ALEATORIO.nome, TipoExercicio.SEQUENCIA.nome)
        val adapter =
            ArrayAdapter(view.context, R.layout.support_simple_spinner_dropdown_item, items)
        (binding.spExercicios.editText as MaterialAutoCompleteTextView).apply {
            setAdapter(adapter)
            setOnItemClickListener { parent, view, position, id ->
                binding.spExercicios.isErrorEnabled = false
                when (TipoExercicio.values()[position].nome) {
                    TipoExercicio.ALEATORIO.nome -> {
                        exercicioIdSelecionado = TipoExercicio.ALEATORIO.id
                    }
                    TipoExercicio.SEQUENCIA.nome -> {
                        exercicioIdSelecionado = TipoExercicio.SEQUENCIA.id
                    }
                }
            }
        }
    }

    private fun configurarDateRange() {
        binding.tiePeriodoExercicios.apply {
            inputType = InputType.TYPE_NULL
            keyListener = null
            onFocusChangeListener =
                View.OnFocusChangeListener { v, hasFocus ->
                    if (hasFocus)
                        openDateRangePicker()
                }
            setOnClickListener {
                openDateRangePicker()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {
            _dataInicio = it.first as Long
            _dataFim = it.second as Long
            binding.tiePeriodoExercicios.setText(
                "De ${DateUtils.convertLongMsToString(_dataInicio!!)} até ${DateUtils.convertLongMsToString(_dataFim!!)}"
            )
            binding.tilPeriodoExercicios.isErrorEnabled = false
        }
        picker.show(MainActivity.instance!!.supportFragmentManager, null)
    }

    fun getExercicioSelecionado(): String? {
        return exercicioIdSelecionado
    }

    fun setErrorExercicio() {
        binding.spExercicios.error = "Selecione um exercício"
    }

    fun getDataInicio(): Long? {
        return _dataInicio
    }

    fun getDataFim(): Long? {
        //Adiciona (24*60*60*1000) para adicionar mais um dia e contabilizar até 00 do dia subsequente a query
        _dataFim = _dataFim?.plus((24*60*60*1000))
        return _dataFim
    }

    fun setErrorData() {
        binding.tilPeriodoExercicios.error = "Informe uma data"
    }

    fun getView(): View {
        return view
    }

    fun removerErros() {
        binding.spExercicios.isErrorEnabled = false
        binding.tilPeriodoExercicios.isErrorEnabled = false
    }
}