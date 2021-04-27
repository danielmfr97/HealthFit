package com.daniel.ramos.projetotcc.view.dialogs

import android.text.InputType
import android.view.View
import com.daniel.ramos.projetotcc.databinding.DialogAddPacienteBinding
import com.daniel.ramos.projetotcc.view.activity.MainActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import java.util.*


private const val TAG = "DialogAddPacienteView"

class DialogAddPacienteView {
    private var binding = DialogAddPacienteBinding.inflate(MainActivity.instance!!.layoutInflater)
    val view = binding.root


    init {
        binding.tieDataNascimento.apply {
            inputType = InputType.TYPE_NULL
            keyListener = null
            onFocusChangeListener =
                View.OnFocusChangeListener { v, hasFocus ->
                    if (hasFocus)
                        openDatePicker()
                }
            setOnClickListener {
                openDatePicker()
            }
        }

    }

    fun getView(): View {
        return view
    }

    fun getNome(): String {
        return binding.tilNome.editText!!.text.toString()
    }

    fun getSexoSelecionado(): String {
        var sexoSelecionado = ""
        val checkedId = binding.rgSexo.checkedRadioButtonId
        when(checkedId) {
            binding.rbMasculino.id -> sexoSelecionado = binding.rbMasculino.text.toString()
            binding.rbFeminino.id -> sexoSelecionado = binding.rbFeminino.text.toString()
        }
        return sexoSelecionado
    }

    fun getDataNascimento(): String {
        return binding.tieDataNascimento.text.toString()
    }

    private fun openDatePicker() {
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it as Long)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)
            val mes = calendar.get(Calendar.MONTH) + 1
            val ano = calendar.get(Calendar.YEAR)
            binding.tieDataNascimento.setText("$dia/$mes/$ano")
        }
        picker.show(MainActivity.instance!!.supportFragmentManager, null)

    }
}