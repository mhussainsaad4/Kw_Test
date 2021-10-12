package org.dropby.app.database.contacts

import androidx.room.*

@Dao
interface ListsDataAccessObjectDao {

    @Query("SELECT * FROM UserGroceryLists")
    suspend fun getAll(): List<ListsEntity>

    @Insert
    suspend fun insertAll(listsList: MutableList<ListsEntity>)

    @Insert
    suspend fun insertAll(vararg userList: ListsEntity)           //var arg means variable number of arguments from Kotlin

    @Delete
    suspend fun delete(userLists: ListsEntity)

    @Query("DELETE FROM UserGroceryLists")
    suspend fun deleteContactsTable()

    @Transaction
    suspend fun deleteAndInsert(listsList: MutableList<ListsEntity>) {
        deleteContactsTable()
        insertAll(listsList)
    }
}