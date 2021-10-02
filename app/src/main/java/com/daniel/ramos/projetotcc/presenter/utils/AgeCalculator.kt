package com.daniel.ramos.projetotcc.presenter.utils

import java.util.*

class AgeCalculator {
    companion object {
        fun calcularIdadePorData(date: String): String {
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()

            dob.time = DateUtils.convertStringToDate(date)
            val year = dob[Calendar.YEAR]
            val month = dob[Calendar.MONTH]
            val day = dob[Calendar.DAY_OF_MONTH]

            dob[year, month + 1] = day

            var age = today[Calendar.YEAR] - dob[Calendar.YEAR]

            if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
                age--
            }

            if (age < 0)
                age = 0
            return age.toString()
        }
    }
}