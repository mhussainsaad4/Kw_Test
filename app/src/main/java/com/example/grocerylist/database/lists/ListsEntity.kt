package com.example.grocerylist.database.lists

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserGroceryLists")            //But we can also create a Separate Table name which is different from Entity name
data class ListsEntity(
    @PrimaryKey(autoGenerate = true) val listId: Int = 0,           //auto increment
    @ColumnInfo(name = "listName") val listName: String?,
    @ColumnInfo(name = "listEntries") val listEntriesString: String?,           //will be added by type converters
    @ColumnInfo(name = "status") val status: String?
) {
    constructor(name: String, listEntriesString: String, status: String) : this(0, name, listEntriesString, status)

    constructor() : this(0, "name", "listEntriesString", "status")
}
