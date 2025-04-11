package com.example.smartflowassessment.data.api

import com.example.smartflowassessment.data.model.Item
import com.example.smartflowassessment.data.model.ProductResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun getAllProducts(): ProductResponse

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
    ): Item

    @POST("products/add")
    suspend fun addProduct(
        @Body product: Item,
    ): Item

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: Item,
    ): Item

    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int,
    ): Item
}
