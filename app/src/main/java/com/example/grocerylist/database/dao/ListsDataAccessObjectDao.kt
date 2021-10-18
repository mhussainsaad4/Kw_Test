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

    //todo get last added pending status row
    @Query("SELECT * FROM UserGroceryLists WHERE status = :status  ORDER BY listId DESC LIMIT 1")
    suspend fun getLastPendingList(status: String): ListsEntity

    @Query("UPDATE UserGroceryLists SET status = :status WHERE listId = :listId")
    fun updateCompletedListStatus(listId: String, status: String): Int

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