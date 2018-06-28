package com.oldmen.stayintouch.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    fun formatStrToStr(inputFormat: String, outputFormat: String, dateStr: String): String {
        val format = SimpleDateFormat(inputFormat, Locale.getDefault())
        val date = format.parse(dateStr)
        return SimpleDateFormat(outputFormat, Locale.getDefault()).format(date)
    }

    fun formatDateToStr(inputFormat: String, date: Date): String {
        val sdf = SimpleDateFormat(inputFormat, Locale.getDefault())
        return sdf.format(date)
    }

    fun getDate(inputFormat: String, dateStr: String): Date {
        return SimpleDateFormat(inputFormat, Locale.getDefault()).parse(dateStr)
    }
}