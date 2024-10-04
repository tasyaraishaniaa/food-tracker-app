package com.example.foodtracker.network

import com.example.foodtracker.data.model.LoginRequest
import com.example.foodtracker.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("api/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
}