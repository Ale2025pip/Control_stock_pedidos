package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DesktopDao {
    @Query("SELECT * FROM desktop_items")
    fun getAllStock(): Flow<List<DesktopItem>>

    @Query("SELECT * FROM desktop_items WHERE id = :id")
    suspend fun getItemById(id: Int): DesktopItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: DesktopItem)

    @Delete
    suspend fun deleteItem(item: DesktopItem)

    @Update
    suspend fun updateItem(item: DesktopItem)
}