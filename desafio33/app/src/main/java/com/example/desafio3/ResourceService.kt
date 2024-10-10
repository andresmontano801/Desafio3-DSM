package com.example.desafio3

import retrofit2.Call
import retrofit2.http.*

interface ResourceService {
    @GET("resources")
    fun getAllResources(): Call<List<Recurso>>

    @GET("resources/{id}")
    fun getResourceById(@Path("id") id: String): Call<Recurso>

    @POST("resources")
    fun createResource(@Body recurso: Recurso): Call<Recurso>

    @PUT("resources/{id}")
    fun updateResource(@Path("id") id: String, @Body recurso: Recurso): Call<Recurso>

    @DELETE("resources/{id}")
    fun deleteResource(@Path("id") id: String): Call<Void>
}
