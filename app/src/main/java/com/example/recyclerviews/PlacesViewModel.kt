package com.example.recyclerviews

import androidx.lifecycle.ViewModel

class PlacesViewModel: ViewModel() {

    private val places = mutableListOf<Place>(Place("Auckland", "it's pretty"), Place("Patagonia", "it's also pretty"))

    fun getPlaces(): List<Place> {
        return places
    }

    fun addNewPlace(place: Place, position: Int? = null): Int {
        if (places.any { it.name.uppercase() == place.name.uppercase()}) {
            return -1
        }

        return if (position != null) {
            places.add(position, place)
            position
        } else {
            places.add(place)
            places.lastIndex
        }
    }

    fun movePlace(from: Int, to: Int) {
        val place = places.removeAt(from)
        places.add(to, place)
    }

    fun deletePlace(position: Int): Place {
        return places.removeAt(position)
    }
}