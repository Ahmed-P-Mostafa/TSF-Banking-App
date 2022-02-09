package com.polotika.tsfbanking

import android.content.Context
import androidx.lifecycle.ViewModel

class MainViewModel :ViewModel() {
    var accountsListAdapter = AccountsListAdapter()

    fun getAllAccounts(context: Context){
        val db = AccountsDatabaseHelper(context)
        val accounts = db.getAllAccounts()
        accountsListAdapter = if (accounts.size>5){
            AccountsListAdapter(accounts)
        }else{
            val dummyAccounts = getDummyAccounts()
            for (account:Account in dummyAccounts){
                db.insertAccount(account)
            }
            val accounts = db.getAllAccounts()
            AccountsListAdapter(accounts)
        }
    }

    private fun getDummyAccounts(): List<Account> {
        val accounts = arrayListOf<Account>()
        for (i in 1..10){
            accounts.add(Account(name = "Account $i", email = "Account${i}_Email@Gmail.com", balance = 1000*i))
        }
        return accounts
    }


}