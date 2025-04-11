package com.example.smartflowassessment.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartflowassessment.data.model.Item

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
