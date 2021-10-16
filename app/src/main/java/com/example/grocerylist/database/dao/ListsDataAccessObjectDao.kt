package com.example.grocerylist.database.dao

import androidx.room.*
import com.example.grocerylist.database.entity.ListsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListsDataAccessObjectDao {

    @Query("SELECT * FROM UserGroceryLists")
    suspend fun getAllGroceryLists(): List<ListsEntity>

    @Query("SELECT * FROM UserGroceryLists")
    fun getAllGroceryListsRealtime(): Flow<MutableList<ListsEntity>>

    @Insert
    suspend fun insertNewList(listsEntity: ListsEntity)

    @Insert
    suspend fun insertMultipleList(vararg userList: ListsEntity)           //var arg means variable number of arguments from Kotlin

    @Delete
    suspend fun delete(userLists: ListsEntity)

    @Query("DELETE FROM UserGroceryLists")
    suspend fun deleteGroceryListsTable()

    @Transaction
    suspend fun deleteAndInsert(listsList: ListsEntity) {
        deleteGroceryListsTable()
        insertNewList(listsList)
    }
}