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

    private lateinit var exercicioIdSelecionado: String
    private var _dataFim by Delegates.notNull<Long>()
    private var _dataInicio by Delegates.notNull<Long>()


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
                when (TipoExercicio.values()[position].nome) {
                    TipoExercicio.ALEATORIO.nome -> {
                        exercicioIdSelecionado = TipoExercicio.ALEATORIO.nome
                    }
                    TipoExercicio.SEQUENCIA.nome -> {
                        exercicioIdSelecionado = TipoExercicio.SEQUENCIA.nome
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
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .setCalendarConstraints(constraintsBuilder.build())
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {
            _dataInicio = it.first as Long
            _dataFim = it.second as Long
            binding.tiePeriodoExercicios.setText(
                "De ${DateUtils.convertLongMsToString(_dataInicio)} até ${DateUtils.convertLongMsToString(_dataFim)}"
            )
        }
        picker.show(MainActivity.instance!!.supportFragmentManager, null)
    }

    fun getExercicioSelecionado(): String {
        return exercicioIdSelecionado
    }

    fun getDataInicio(): Long {
        return _dataInicio
    }

    fun getDataFim(): Long {
        return _dataFim
    }

    fun getView(): View {
        return view
    }
}