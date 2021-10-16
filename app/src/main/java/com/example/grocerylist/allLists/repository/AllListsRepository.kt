package com.example.grocerylist.allLists.repository

import android.content.Context
import com.example.grocerylist.database.GroceryListAppDatabase
import com.example.grocerylist.database.entity.ListsEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class AllListsRepository @Inject constructor(@ApplicationContext val context: Context) {

    private lateinit var db: GroceryListAppDatabase

    init {
        db = GroceryListAppDatabase.getAppDatabase()
    }

    @ExperimentalCoroutinesApi
    suspend fun getAllGroceryListsRealtime(): Flow<MutableList<ListsEntity>> = channelFlow {
        db.groceryListsDao().getAllGroceryListsRealtime().collect { groceryLists ->
            send(groceryLists)
            close()
        }
        awaitClose()
    }
}