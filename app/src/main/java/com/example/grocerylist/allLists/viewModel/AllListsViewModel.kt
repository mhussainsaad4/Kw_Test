package com.example.grocerylist.allLists.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllListsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

}