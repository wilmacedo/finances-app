package br.com.progy.finances.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index("email", unique = true),
    ]
)
data class User(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    var email: String,
    val password: String,
    val name: String,
    val phone: String,
)