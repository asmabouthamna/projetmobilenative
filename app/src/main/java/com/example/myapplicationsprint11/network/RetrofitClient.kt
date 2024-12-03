package com.example.myapplicationsprint11.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.158.90:8080/") // Remplacez par votre URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: MedicamentService = retrofit.create(MedicamentService::class.java)
}
