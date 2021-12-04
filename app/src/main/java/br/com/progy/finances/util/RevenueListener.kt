package br.com.progy.finances.util

import android.view.View

interface RevenueListener {

    fun onRevenueItemClick(view: View, position: Int)

    fun onRevenueItemLongClick(view: View, position: Int)
}