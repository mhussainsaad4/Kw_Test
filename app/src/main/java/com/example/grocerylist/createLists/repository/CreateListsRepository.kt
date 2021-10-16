package com.example.grocerylist.createLists.repository

import android.content.Context
import com.example.grocerylist.database.GroceryListAppDatabase
import com.example.grocerylist.database.entity.ListsEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateListsRepository @Inject constructor(@ApplicationContext val context: Context) {

    private lateinit var db: GroceryListAppDatabase

    init {
        db = GroceryListAppDatabase.getAppDatabase()
    }

    suspend fun insertNewListToDb(listsEntity: ListsEntity) {
        db.groceryListsDao().insertNewList(listsEntity)
    }
}