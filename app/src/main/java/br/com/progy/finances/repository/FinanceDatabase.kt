package br.com.progy.finances.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.progy.finances.model.Revenue
import br.com.progy.finances.model.Type
import br.com.progy.finances.model.User

@Database(entities = [User::class, Revenue::class, Type::class], version = 1)
abstract class FinanceDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getRevenueDao(): RevenueDao

    abstract fun getTypeDao(): TypeDao
}