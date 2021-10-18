package com.example.grocerylist.groceryLists.repository

import android.content.Context
import com.example.grocerylist.database.GroceryListAppDatabase
import com.example.grocerylist.database.entity.ListsEntity
import com.example.grocerylist.utils.K.Constants.Companion.STATUS_COMPLETED
import com.example.grocerylist.utils.K.Constants.Companion.STATUS_PENDING
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class GroceryListsRepository @Inject constructor(@ApplicationContext val context: Context) {
    private lateinit var db: GroceryListAppDatabase

    init {
        db = GroceryListAppDatabase.getAppDatabase()
    }

    @ExperimentalCoroutinesApi
    suspend fun getLastPendingList(): Flow<ListsEntity> = channelFlow {
        val listsEntity = db.groceryListsDao().getLastPendingList(STATUS_PENDING)
        if (listsEntity == null) send(ListsEntity())
        else send(listsEntity)
        close()
    }

    suspend fun updateCompletedListStatus(listId: String): Int {
        val updatedRows = db.groceryListsDao().updateCompletedListStatus(listId, STATUS_COMPLETED)
        return updatedRows
    }
}