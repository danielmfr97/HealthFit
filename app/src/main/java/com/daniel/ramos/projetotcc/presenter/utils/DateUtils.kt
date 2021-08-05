package com.daniel.ramos.projetotcc.presenter.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DateUtils {
    companion object{
        private fun getLocaleBR(): Locale {return Locale("pt", "BR")}

        fun convertDateToString(date: Date?): String {
            if (date == null)
                return "N/A"
            return SimpleDateFormat("dd/MM/yyyy", getLocaleBR()).format(date)
        }

        fun convertLongMsToString(dateInMs: Long): String {
            val utcTime = Date(dateInMs)
            val format = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(format, getLocaleBR())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmtTime = SimpleDateFormat(format, Locale.getDefault()).parse(sdf.format(utcTime))
            return convertDateToString(gmtTime)
        }

        @SuppressLint("SimpleDateFormat")
        fun convertStringToDate(date: String): Date {
            return SimpleDateFormat("dd/MM/yyyy").parse(date)
        }

        fun convertLongMsToSeconds(toLong: Long): String {
            val time = (toLong/1000).toString()
            return "$time Segundos"
        }
    }
}