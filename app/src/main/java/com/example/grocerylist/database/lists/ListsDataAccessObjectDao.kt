package com.example.grocerylist.database.lists

import androidx.room.*

@Dao
interface ListsDataAccessObjectDao {

    @Query("SELECT * FROM UserGroceryLists")
    suspend fun getAll(): List<ListsEntity>

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