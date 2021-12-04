package br.com.progy.finances.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "revenues",
    indices = [
        Index("name", unique = true)
    ]
)
data class Revenue(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    val name: String,
    val description: String,
    val amount: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "type_id")
    val typeId: Int
)