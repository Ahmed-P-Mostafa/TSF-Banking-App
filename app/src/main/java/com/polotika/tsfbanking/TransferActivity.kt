package com.polotika.tsfbanking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.polotika.tsfbanking.databinding.ActivityTransferBinding

class TransferActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransferBinding
    private val transferViewModel : TransferViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_transfer)
        binding.vm = transferViewModel
        val idExtra = intent.getIntExtra("Account_id",1)
        val senderAccount = transferViewModel.getSender(idExtra,this)
        binding.sender = senderAccount
    }
}