package com.example.recyclerviews
import java.text.SimpleDateFormat
import java.util.*

data class Place(val name: String, val reason: String, val dateAdded: Date = Date()) {

    fun formattedDate(): String {
        return SimpleDateFormat("EEE, d MMMM yyy", Locale.getDefault()).format(dateAdded)
    }
}