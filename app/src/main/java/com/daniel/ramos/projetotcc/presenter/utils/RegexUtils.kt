package com.daniel.ramos.projetotcc.presenter.utils

class RegexUtils {
    companion object {
        fun removerVirgulasAsStringArray(data: String): List<List<String>> {
            return listOf(data.split(","))
        }
    }
}