package com.polotika.tsfbanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.polotika.tsfbanking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),AccountsListAdapter.AccountClickListener  {
    private val mainViewModel:MainViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.vm = mainViewModel
        mainViewModel.getAllAccounts(this)
        mainViewModel.accountsListAdapter.listener = this
    }

    override fun onAccountClickListener(id: Int) {
        val transferIntent = Intent(this,TransferActivity::class.java)
        transferIntent.putExtra("Account_id",id)
        startActivity(transferIntent)
    }
}