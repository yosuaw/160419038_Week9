package com.ubaya.todo.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ubaya.todo.util.MIGRATION_1_2
import com.ubaya.todo.util.MIGRATION_2_3

@Database(entities = arrayOf(Todo::class), version = 3)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile private var instance: TodoDatabase ?= null
        private val LOCK = Any()

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                                 TodoDatabase::class.java,
                                  "tododb")
                                 .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                                 .build()

        operator fun invoke(context: Context) {
            if(instance != null) {
                synchronized(LOCK) { // Jangan sampai ada thread lain yang menjalankan ini
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }
            }
        }
    }
}