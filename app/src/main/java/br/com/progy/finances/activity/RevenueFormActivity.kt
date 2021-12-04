package br.com.progy.finances.activity

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.*
import br.com.progy.finances.R
import br.com.progy.finances.model.Revenue
import br.com.progy.finances.util.Database
import br.com.progy.finances.util.RevenueListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RevenueFormActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    private var userId = -1
    private var revenueId = -1;
    private var spinnerSelection = "Receita"

    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var amount: EditText
    private lateinit var spinner: Spinner
    private lateinit var button: Button

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revenue_form)

        userId = intent.getIntExtra("user_id", -1)
        revenueId = intent.getIntExtra("revenue_id", -1)

        val title: TextView = findViewById(R.id.revenue_form_title)
        val width = title.paint.measureText(title.text.toString())
        val gradient: Shader = LinearGradient(0f, 20f, width, title.textSize, intArrayOf(
            Color.parseColor("#77DC9F"),
            Color.parseColor("#1AB8BD")
        ), null, Shader.TileMode.REPEAT)

        title.paint.shader = gradient
        if (revenueId == -1) {
            title.text = "Nova receita"
        } else {
            title.text = "Editar receita"

            GlobalScope.launch {
                val instance = Database.getInstance(applicationContext)
                val revenue = instance.getRevenueDao().findById(revenueId)

                if (revenue != null) {
                    val type = instance.getTypeDao().findById(revenue.typeId)

                    handler.post {
                        name.text = SpannableStringBuilder(revenue.name)
                        description.text = SpannableStringBuilder(revenue.description)
                        amount.text = SpannableStringBuilder(revenue.amount.toString())
                        spinner.setSelection(type.id!! - 1)
                        button.text = "Atualizar"
                    }
                }
            }
        }

        name = findViewById(R.id.revenue_form_name)
        description = findViewById(R.id.revenue_form_description)
        amount = findViewById(R.id.revenue_form_amount)
        spinner = findViewById(R.id.revenue_form_spinner)
        button = findViewById(R.id.revenue_form_button)

        val values = listOf<String>("Receita", "Despesa")
        val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, values)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.revenue_form_button -> {
                var isFormFilled = true
                val fields = listOf<EditText>(name, description, amount)
                val emptyField = "Este campo é obrigatório"

                for (field in fields) {
                    if (field.text.isEmpty()) {
                        field.error = emptyField
                        isFormFilled = false
                    }
                }

                if (isFormFilled) {
                    GlobalScope.launch {

                        if (revenueId == -1) {
                            val instance = Database.getInstance(applicationContext)
                            val typeId = instance.getTypeDao().findByName(spinnerSelection)

                            val revenue = Revenue(
                                name = name.text.toString(),
                                description = description.text.toString(),
                                amount = amount.text.toString().toInt(),
                                userId = userId,
                                typeId = typeId.id!!
                            )

                            instance.getRevenueDao().insert(revenue)

                            handler.post {
                                Toast.makeText(applicationContext, "Receita ${name.text.toString()} registrada com sucesso!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } else {
                            val instance = Database.getInstance(applicationContext)
                            val typeId = instance.getTypeDao().findByName(spinnerSelection)

                            val revenue = Revenue(
                                id = revenueId,
                                name = name.text.toString(),
                                description = description.text.toString(),
                                amount = amount.text.toString().toInt(),
                                userId = userId,
                                typeId = typeId.id!!
                            )

                            instance.getRevenueDao().update(revenue)

                            handler.post {
                                Toast.makeText(applicationContext, "Receita ${name.text.toString()} atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item: String = parent?.getItemAtPosition(position) as String

        spinnerSelection = item
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        spinnerSelection = "Receita"
    }
}