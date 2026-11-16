package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "desktop_items")
data class DesktopItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val material: String = "",
    val stock: Int,
    val price: Double
)
