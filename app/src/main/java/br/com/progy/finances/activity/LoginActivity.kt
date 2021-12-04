package br.com.progy.finances.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import br.com.progy.finances.R
import br.com.progy.finances.util.Database
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var button: Button

    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val title: TextView = findViewById(R.id.loginTitle)
        val width = title.paint.measureText(title.text.toString())
        val gradient: Shader = LinearGradient(0f, 20f, width, title.textSize, intArrayOf(
            Color.parseColor("#77DC9F"),
            Color.parseColor("#1AB8BD")
        ), null, Shader.TileMode.REPEAT)

        title.paint.shader = gradient

        email = findViewById(R.id.login_email_input)
        password = findViewById(R.id.login_password_input)
        button = findViewById(R.id.login_button)

        button.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_button -> {
                var isFormFilled = true
                val fields = listOf<EditText>(email, password)
                val emptyField = "Este campo é obrigatório"

                for (field in fields) {
                    if (field.text.isEmpty()) {
                        field.error = emptyField
                        isFormFilled = false
                    }
                }

                if (isFormFilled) {
                    GlobalScope.launch {
                        val user = Database
                            .getInstance(applicationContext)
                            .getUserDao()
                            .findByEmail(email.text.toString())

                        if (user != null && user.password == password.text.toString()) {
                            handler.post {
                                val activity = Intent(applicationContext, RevenueListActivity::class.java)
                                activity.putExtra("user_id", user.id)
                                startActivity(activity)
                                finish()
                            }
                        } else {
                            handler.post {
                                Toast.makeText(applicationContext, "Usuário ou senha inválido", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}