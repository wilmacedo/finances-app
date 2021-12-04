package br.com.progy.finances.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.progy.finances.R
import br.com.progy.finances.adapter.RevenueAdapter
import br.com.progy.finances.model.Revenue
import br.com.progy.finances.util.Database
import br.com.progy.finances.util.RevenueListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class RevenueListActivity : AppCompatActivity(), RevenueListener, View.OnClickListener {

    private var userId = -1

    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingButton: FloatingActionButton

    private lateinit var revenues: List<Revenue>
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        userId = intent.getIntExtra("user_id", -1)

        floatingButton = findViewById(R.id.revenue_floatbutton_add)
        recyclerView = findViewById(R.id.revenue_reciclyview)

        val title: TextView = findViewById(R.id.revenue_list_title)
        val width = title.paint.measureText(title.text.toString())
        val gradient: Shader = LinearGradient(0f, 20f, width, title.textSize, intArrayOf(
            Color.parseColor("#77DC9F"),
            Color.parseColor("#1AB8BD")
        ), null, Shader.TileMode.REPEAT)

        title.paint.shader = gradient

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        floatingButton.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            val instance = Database.getInstance(applicationContext)

            val localRevenues = mutableListOf<Revenue>()
            val types = listOf<String>("Receita", "Despesa")
            for (type in types) {
                val typeId = instance.getTypeDao().findByName(type)

                if (typeId != null) {
                    val typedRevenue = instance.getRevenueDao().findByType(typeId.id!!)

                    for (item in typedRevenue) {
                        localRevenues.add(item)
                    }
                }
            }

            revenues = localRevenues

            handler.post {
                val adapter = RevenueAdapter(revenues, applicationContext)
                adapter.setOnRevenueListener(this@RevenueListActivity)
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.revenue_floatbutton_add -> {
                val activity = Intent(applicationContext, RevenueFormActivity::class.java)
                activity.putExtra("user_id", userId)
                startActivity(activity)
            }
        }
    }

    override fun onRevenueItemClick(view: View, position: Int) {
        val activity = Intent(applicationContext, RevenueFormActivity::class.java)
        activity.putExtra("user_id", userId)
        activity.putExtra("revenue_id", revenues[position].id)
        startActivity(activity)
    }

    override fun onRevenueItemLongClick(view: View, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Excluir registro de receita")
        builder.setMessage("Voce tem certeza que deseja excluir esse registro de receita?")
        builder.setPositiveButton("Sim", DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
            GlobalScope.launch {
                Database.getInstance(applicationContext).getRevenueDao().delete(revenues[position])

                handler.post {
                    Toast.makeText(
                        applicationContext,
                        "Receita removida com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        builder.setNegativeButton("Não", DialogInterface.OnClickListener { _: DialogInterface, _: Int ->})
        builder.show()
    }
}