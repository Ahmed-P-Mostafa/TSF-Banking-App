package com.polotika.tsfbanking

import android.R
import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel

class TransferViewModel : ViewModel() {

    fun getSender(id: Int, context: Context): Account? {
        val db = AccountsDatabaseHelper(context)
        val senderAccount = db.getAccount(id = id)
        db.close()
        return senderAccount
    }

    val accountsListener = AdapterView.OnItemClickListener { _, _, position, _ ->

    }

}

@BindingAdapter("setListener")
fun setListener(view: AutoCompleteTextView, listener: AdapterView.OnItemClickListener) {
    view.onItemClickListener = listener
    val accounts = AccountsDatabaseHelper(view.context).getAvailableAccountsToTransfer(view.tag as Int).map {
        "${it.name + " Balance: " + it.balance}"
    }
    val adapter = ArrayAdapter(view.context, R.layout.simple_dropdown_item_1line, accounts)
    view.setAdapter(adapter)
}