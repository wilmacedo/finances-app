package br.com.progy.finances.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import br.com.progy.finances.R
import br.com.progy.finances.model.User
import br.com.progy.finances.util.Database
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var name: EditText
    private lateinit var password: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var button: Button

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name = findViewById(R.id.name_register_input)
        password = findViewById(R.id.register_password_input)
        email = findViewById(R.id.register_email_input)
        phone = findViewById(R.id.register_phone_input)
        button = findViewById(R.id.register_button_input)

        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register_button_input -> {
                var isFormFilled = true
                val fields = listOf<EditText>(name, password, email, phone)
                val emptyField = "Este campo é obrigatório"

                for (field in fields) {
                    if (field.text.isEmpty()) {
                        field.error = emptyField
                        isFormFilled = false
                    }
                }

                if (isFormFilled) {
                    val user = User(
                        name = name.text.toString(),
                        password = password.text.toString(),
                        email = email.text.toString(),
                        phone = phone.text.toString()
                    )

                    handler.post {

                    }

                    GlobalScope.launch {
                        var userDao = Database.getInstance(applicationContext).getUserDao()

                        if (userDao.findByEmail(email.text.toString()) == null) {
                            Database
                                .getInstance(applicationContext)
                                .getUserDao()
                                .insert(user)

                            handler.post {
                                Toast.makeText(applicationContext, "Usuário incluido com sucesso!", Toast.LENGTH_SHORT).show()

                                finish()
                            }
                        } else {
                            handler.post {
                                Toast.makeText(applicationContext, "Usuário já existente", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}