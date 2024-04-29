package com.example.recyclerviews

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlacesViewModel: ViewModel() {

    private val places = mutableListOf<Place>(Place("Auckland", "it's pretty"), Place("Patagonia", "it's also pretty"))

    private val placeRepository = PlaceRepository()

    val allPlaces = MutableLiveData(listOf<Place>())
    val userMessage = MutableLiveData<String?>()

    init {
        getPlaces()
    }
    fun getPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            val apiResult = placeRepository.getAllPlaces()
            if (apiResult.status == ApiStatus.SUCCESS) {
                allPlaces.postValue(apiResult.data)
            }
            userMessage.postValue(apiResult.message)
        }
    }

    fun addNewPlace(place: Place) {
        viewModelScope.launch {
            val result = placeRepository.addPlace(place)
            userMessage.postValue(result.message)
            getPlaces()
        }
    }

    fun updatePlace(place: Place) {
        viewModelScope.launch {
            val result = placeRepository.updatePlace(place)
            userMessage.postValue(result.message)
            getPlaces()
        }
    }

    fun deletePlace(place: Place) {
        viewModelScope.launch {
            val result = placeRepository.deletePlace(place)
            userMessage.postValue(result.message)
            getPlaces()
        }
    }
//    fun addNewPlace(place: Place, position: Int? = null): Int {
//        if (places.any { it.name.uppercase() == place.name.uppercase()}) {
//            return -1
//        }
//
//        return if (position != null) {
//            places.add(position, place)
//            position
//        } else {
//            places.add(place)
//            places.lastIndex
//        }
//    }

    fun movePlace(from: Int, to: Int) {
        val place = places.removeAt(from)
        places.add(to, place)
    }
}