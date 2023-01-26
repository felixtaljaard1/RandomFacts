package com.example.randomfactsapp.api

import com.example.randomfactsapp.model.Fact
import retrofit2.http.GET
import retrofit2.Response


interface FactsApi {

    @GET(ApiReferences.END_POINT_RANDOM)
    suspend fun getRandomFact(): Response<Fact>

    @GET(ApiReferences.END_POINT_TODAY)
    suspend fun getDailyFact(): Response<Fact>
}