package com.msamedcagli.delsaappson

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDAO {
    @Query("SELECT*FROM Item")
    suspend fun getAll(): List<Item>

    @Query("SELECT * FROM Item WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): Item?

    @Insert
    suspend fun insert(item : Item):Long

    @Delete
    suspend fun delete(item : Item)

    @Update
    suspend fun update(item: Item)

}