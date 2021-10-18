package com.example.grocerylist.groceryLists.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylist.database.entity.ListsEntity
import com.example.grocerylist.groceryLists.repository.GroceryListsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroceryListsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var groceryListsRepository: GroceryListsRepository

    init {
        groceryListsRepository = GroceryListsRepository(application)
    }

    @ExperimentalCoroutinesApi
    fun getLastPendingList(): Flow<ListsEntity> = channelFlow {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                groceryListsRepository.getLastPendingList().collect {
                    send(it)
                    close()
                }
            }
        }
        awaitClose()
    }

    @ExperimentalCoroutinesApi
    suspend fun updateCompletedListStatus(listId: String): Flow<Int> = channelFlow {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val updatedRows = groceryListsRepository.updateCompletedListStatus(listId)
                send(updatedRows)
                close()
            }
        }
        awaitClose()
    }
}