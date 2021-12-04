package br.com.progy.finances.repository

import androidx.room.*
import br.com.progy.finances.model.Type

@Dao
interface TypeDao {

    @Insert
    fun insert(type: Type)

    @Update
    fun update(type: Type)

    @Delete
    fun delete(type: Type)

    @Query("SELECT * FROM types WHERE id = :id")
    fun findById(id: Int): Type

    @Query("SELECT * FROM types WHERE name = :name")
    fun findByName(name: String): Type

    @Query("SELECT * FROM types")
    fun findAll(): List<Type>
}