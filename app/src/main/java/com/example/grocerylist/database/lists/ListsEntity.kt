package org.dropby.app.database.contacts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserGroceryLists")            //But we can also create a Separate Table name which is different from Entity name
data class ListsEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,           //auto increment
    @ColumnInfo(name = "id") val id: String?,
    @ColumnInfo(name = "listName") val listName: String?,
    @ColumnInfo(name = "listEntries") val listEntriesString: String?,           //will be added by type converters
) {
    constructor(id: String, name: String, listEntriesString: String) : this(0, id, name, listEntriesString)

    constructor() : this(0, "id", "name", "listEntriesString")
}
