package com.example.smartflowassessment.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "items")
data class Item(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val brand: String?,
    val category: String,
    @SerializedName("thumbnail") val imageUrl: String,
    val updatedAt: Long = System.currentTimeMillis(),
)
