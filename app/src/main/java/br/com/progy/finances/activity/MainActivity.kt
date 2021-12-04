package br.com.progy.finances.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.room.Room
import br.com.progy.finances.R
import br.com.progy.finances.model.Type
import br.com.progy.finances.model.User
import br.com.progy.finances.repository.FinanceDatabase
import br.com.progy.finances.util.Database
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val types = listOf<Type>(Type(name = "Receita"), Type(name = "Despesa"))
        val typeDao = Database.getInstance(this).getTypeDao()

        GlobalScope.launch {
            for (type in types) {
                if (typeDao.findByName(type.name) == null) {
                    typeDao.insert(type)
                }
            }
        }

        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            val activity = Intent(MainActivity@this, RegisterActivity::class.java)
            startActivity(activity)
        }

        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            val activity = Intent(MainActivity@this, LoginActivity::class.java)
            startActivity(activity)
        }
    }
}