package com.msamedcagli.delsaappson

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class ItemDataBase : RoomDatabase() {
    abstract fun itemdao(): ItemDAO

    companion object {
        @Volatile
        private var INSTANCE: ItemDataBase? = null

        fun getDatabase(context: Context): ItemDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemDataBase::class.java,
                    "item_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}