package com.example.myapplication.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Database(entities = [DesktopItem::class, OrderModel::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun desktopDao(): DesktopDao
    abstract fun orderDao(): OrderDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "3d_print_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}