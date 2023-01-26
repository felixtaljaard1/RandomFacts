package com.example.randomfactsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.randomfactsapp.model.Fact

@Database(
    entities = [Fact::class],
    version = 1,
    exportSchema = false
)
abstract class FactsDatabase : RoomDatabase() {
    abstract fun getDao(): FactDao
}