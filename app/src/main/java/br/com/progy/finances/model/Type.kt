package br.com.progy.finances.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "types",
    indices = [
        Index("name", unique = true)
    ]
)
data class Type(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    val name: String
)