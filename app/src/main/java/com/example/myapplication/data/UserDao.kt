package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // Consulta para validar el login buscando usuario y contraseña
    @Query("SELECT * FROM users_table WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    // Para verificar si ya existe el usuario antes de registrarlo
    @Query("SELECT * FROM users_table WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?
}