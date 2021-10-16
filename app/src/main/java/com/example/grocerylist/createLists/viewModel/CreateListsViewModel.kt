package com.example.grocerylist.createLists.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylist.createLists.repository.CreateListsRepository
import com.example.grocerylist.database.entity.ListsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateListsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var createListsRepository: CreateListsRepository

    init {
        createListsRepository = CreateListsRepository(application)
    }

    suspend fun insertNewListToDb(listsEntity: ListsEntity){
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                createListsRepository.insertNewListToDb(listsEntity)
            }
        }
    }


}