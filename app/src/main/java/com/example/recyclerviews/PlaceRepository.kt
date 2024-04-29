package com.example.recyclerviews
import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "PLACE_REPOSITORY"
class PlaceRepository {

    private val BASE_URL = "https://claraj.pythonanywhere.com/api/"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationHeaderInterceptor())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val placeService = retrofit.create(PlaceService::class.java)

    private suspend fun <T: Any> apiCall (apiCallFun: suspend () -> Response<T>, successMessage: String?,
                                          failMessage: String?) : ApiResult <T> {
        try {
            val response = apiCallFun()
            if (response.isSuccessful) {
                Log.d(TAG, "Response body ${response.body()}")
                return ApiResult(ApiStatus.SUCCESS, response.body(), successMessage)
            } else {
                Log.e(TAG, "Server error ${response.errorBody()}")
                return ApiResult(ApiStatus.SERVER_ERROR, null, failMessage)
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error connecting to API server", ex)
            return ApiResult(ApiStatus.NETWORK_ERROR, null, "Can't connect to server")
        }
    }

    suspend fun getAllPlaces(): ApiResult<List<Place>> {
        return apiCall(placeService::getAllPlaces, null, "Error getting places")
    }

    suspend fun addPlace(place: Place): ApiResult<Place?> {
        return apiCall( {placeService.addPlace(place)}, "Place created!", "Error adding place ${place.name}")
    }

    suspend fun updatePlace(place: Place): ApiResult<Any?> {
        return if (place.id == null) {
            ApiResult(ApiStatus.SERVER_ERROR, null, "Attempting to update a place with no ID")
        } else {
            apiCall({placeService.updatePlace(place, place.id)}, "Place updated!", "Error updating place ${place.name}")
        }
    }

    suspend fun deletePlace(place: Place): ApiResult<Any?> {
        return if (place.id == null) {
            ApiResult(ApiStatus.SERVER_ERROR, null, "Attempting to delete a place with no ID")
        } else {
            apiCall({placeService.deletePlace(place.id)}, "Place deleted", "Error deleting place ${place.name}")
        }
    }
}