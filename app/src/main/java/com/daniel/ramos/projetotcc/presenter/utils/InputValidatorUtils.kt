package com.daniel.ramos.projetotcc.presenter.utils

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

class InputValidatorUtils {

    companion object {
        fun validarMaterialTextInput(_component: TextInputLayout): Boolean {
            var isValid = true
            val _editText = _component.editText!!
            if (_editText.text.isNullOrEmpty()) {
                _component.error = "O campo acima é obrigatório"
                isValid = false
            }
            return isValid
        }

        fun validarRadioGroup(_component: RadioGroup): Boolean {
            var isValid = true
            if (_component.checkedRadioButtonId == -1) {
                (_component.getChildAt(_component.childCount - 1) as RadioButton).error = "Selecione uma opção"
                isValid = false
            }
            return isValid
        }

        fun addTextErrorListener(_component: TextInputLayout) {
            _component.editText?.doOnTextChanged { text, start, before, count ->
                if (count > 0)
                    _component.isErrorEnabled = false
            }
        }
    }
}