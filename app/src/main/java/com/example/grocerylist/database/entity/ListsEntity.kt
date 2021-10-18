package com.example.grocerylist.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "UserGroceryLists")            //But we can also create a Separate Table name which is different from Entity name
data class ListsEntity(
    @PrimaryKey(autoGenerate = true) val listId: Int = 0,           //auto increment
    @ColumnInfo(name = "listName") val listName: String?,
    @ColumnInfo(name = "listEntries") val listEntries: MutableList<String>?,           //will be added by type converters
    @ColumnInfo(name = "status") val status: String?
) : Parcelable {
    constructor(name: String, listEntries: MutableList<String>, status: String) : this(0, name, listEntries, status)

    constructor() : this(0, "", mutableListOf<String>(""), "")
}
