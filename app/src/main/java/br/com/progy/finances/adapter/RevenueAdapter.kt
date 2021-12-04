package br.com.progy.finances.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.progy.finances.R
import br.com.progy.finances.model.Revenue
import br.com.progy.finances.util.Database
import br.com.progy.finances.util.RevenueListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RevenueAdapter(private val revenues: List<Revenue>, private val context: Context): RecyclerView.Adapter<RevenueAdapter.RevenueViewHolder>() {

    private var listener: RevenueListener? = null

    class RevenueViewHolder(item: View, listener: RevenueListener?): RecyclerView.ViewHolder(item) {
        val name: TextView = item.findViewById(R.id.item_revenue_name)
        val description: TextView = item.findViewById(R.id.item_revenue_description)
        val amount: TextView = item.findViewById(R.id.item_revenue_amount)
        val type: TextView = item.findViewById(R.id.item_revenue_type)

        init {
            item.setOnClickListener {
                listener?.onRevenueItemClick(it, adapterPosition)
            }

            item.setOnLongClickListener {
                listener?.onRevenueItemLongClick(it, adapterPosition)
                true
            }
        }
    }

    fun setOnRevenueListener(listener: RevenueListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RevenueViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_revenue_list, parent, false)

        return RevenueViewHolder(item, listener)
    }

    override fun onBindViewHolder(holder: RevenueViewHolder, position: Int) {
        holder.name.text = revenues[position].name
        holder.description.text = revenues[position].description
        holder.amount.text = "R$ ${revenues[position].amount.toString()}"

        GlobalScope.launch {
            val type = Database.getInstance(context).getTypeDao().findById(revenues[position].typeId)

            if (type != null) {
                holder.type.text = type.name
            } else {
                holder.type.text = "Desconhecido"
            }
        }
    }

    override fun getItemCount(): Int {
        return revenues.size
    }
}