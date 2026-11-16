package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders_table")
data class OrderModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientName: String,
    val description: String,
    val deliveryDate: String,
    val totalPrice: Double,
    val advancePayment: Double, // Seña (50%)
    val status: String          // Pendiente, En impresión, Entregado
)