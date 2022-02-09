package com.polotika.tsfbanking

import android.R
import android.content.Context
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class TransferViewModel : ViewModel() {
    private val TAG = javaClass.name

    val moneyAmount = MutableLiveData<String>()
    private var senderAccountFlow = Account()
    private var receiverAccount = Account()
    private var accounts: List<Account> = listOf()

    private val _uieFlow = MutableSharedFlow<TransferState>()
    val uiFlow = _uieFlow.asSharedFlow()

    fun getSender(id: Int, context: Context): Account? {
        var senderAccount: Account? = null
        val db = AccountsDatabaseHelper(context)
        senderAccount = db.getAccount(id = id)
        accounts = db.getAvailableAccountsToTransfer(id)

        senderAccountFlow = senderAccount!!

        db.close()

        return senderAccount
    }

    fun transfer() {
        Log.d(TAG, "transfer: ${moneyAmount.value} value")
        if (moneyAmount.value?.toDoubleOrNull() ?: 0.0 < 1 ||
            moneyAmount.value?.toIntOrNull() ?: 0 > senderAccountFlow.balance!!
        ) {
            Log.d(TAG, "transfer: not valid")
            setUiFlow(TransferState.Failed("Value not accepted!"))

        } else {

            setUiFlow(TransferState.Success("Transaction Done!"))

        }

    }

    fun updateAccountsBalance(context: Context) {
        viewModelScope.launch {
            senderAccountFlow.balance -= moneyAmount.value?.toInt()!!
            receiverAccount.balance += moneyAmount.value?.toInt()!!
            val db = AccountsDatabaseHelper(context)
            db.updateAccount(senderAccountFlow)
            db.updateAccount(receiverAccount)
        }
    }

    private fun setUiFlow(state: TransferState) {
        viewModelScope.launch {
            _uieFlow.emit(state)
        }
    }

    val accountsListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        receiverAccount = accounts.get(position)
    }

    sealed class TransferState() {
        class Success(val message: String) : TransferState()
        class Failed(val message: String) : TransferState()
    }

}

@BindingAdapter("setListener")
fun setListener(view: AutoCompleteTextView, listener: AdapterView.OnItemClickListener) {
    view.onItemClickListener = listener
    val accounts =
        AccountsDatabaseHelper(view.context).getAvailableAccountsToTransfer(view.tag as Int).map {
            "${it.name + " Balance: " + it.balance}"
        }

    val adapter = ArrayAdapter(view.context, R.layout.simple_dropdown_item_1line, accounts)
    view.setAdapter(adapter)
}