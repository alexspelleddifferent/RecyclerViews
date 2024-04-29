package com.example.recyclerviews

enum class ApiStatus { SUCCESS, SERVER_ERROR, NETWORK_ERROR}
class ApiResult<out T> (val status: ApiStatus, val data: T?, val message: String?)