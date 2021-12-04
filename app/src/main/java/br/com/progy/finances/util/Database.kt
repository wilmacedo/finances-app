package br.com.progy.finances.util

import android.content.Context
import androidx.room.Room
import br.com.progy.finances.repository.FinanceDatabase

object Database {

    private var instance: FinanceDatabase? = null

    fun getInstance(context: Context): FinanceDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context,
                FinanceDatabase::class.java,
                "finance.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        return instance!!
    }
}