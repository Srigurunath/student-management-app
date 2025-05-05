package com.student.management

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.student.management.models.PaymentRecord

class PaymentHistoryAdapter(private val paymentList: List<PaymentRecord>) :
    RecyclerView.Adapter<PaymentHistoryAdapter.PaymentViewHolder>() {

    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val paymentAmount: TextView = itemView.findViewById(R.id.paymentAmount)
        val paymentDate: TextView = itemView.findViewById(R.id.paymentDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_history, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = paymentList[position]
        holder.paymentAmount.text = "Amount: Rs. ${payment.amount}"
        holder.paymentDate.text = "Date: ${payment.date}"
    }

    override fun getItemCount(): Int = paymentList.size
}
