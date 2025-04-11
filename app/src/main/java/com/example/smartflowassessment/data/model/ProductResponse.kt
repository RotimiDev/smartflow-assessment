package com.example.smartflowassessment.data.model

data class ProductResponse(
    val products: List<Item>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)
