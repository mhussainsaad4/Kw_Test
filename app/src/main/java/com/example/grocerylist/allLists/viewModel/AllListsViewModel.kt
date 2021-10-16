package com.example.grocerylist.allLists.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylist.allLists.repository.AllListsRepository
import com.example.grocerylist.database.entity.ListsEntity
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
class AllListsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var allListsRepository: AllListsRepository

    init {
        allListsRepository = AllListsRepository(application)
    }

    @ExperimentalCoroutinesApi
    fun getAllGroceryListsRealtime(): Flow<MutableList<ListsEntity>> = channelFlow {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allListsRepository.getAllGroceryListsRealtime().collect {
                    send(it)
                    close()
                }
            }
        }
        awaitClose()
    }
}