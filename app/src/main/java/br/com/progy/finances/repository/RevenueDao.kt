package br.com.progy.finances.repository

import androidx.room.*
import br.com.progy.finances.model.Revenue
import br.com.progy.finances.model.User

@Dao
interface RevenueDao {

    @Insert
    fun insert(revenue: Revenue)

    @Update
    fun update(revenue: Revenue)

    @Delete
    fun delete(revenue: Revenue)

    @Query("SELECT * FROM revenues WHERE id = :id")
    fun findById(id: Int): Revenue

    @Query("SELECT * FROM revenues")
    fun findAll(): List<Revenue>

    @Query("SELECT * FROM revenues WHERE type_id = :typeId LIMIT 5")
    fun findByType(typeId: Int): List<Revenue>
}