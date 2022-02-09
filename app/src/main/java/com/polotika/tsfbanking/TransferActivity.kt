package com.polotika.tsfbanking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.polotika.tsfbanking.databinding.ActivityTransferBinding

class TransferActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransferBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_transfer)
        val idExtra = intent.getIntExtra("Account_id",1)
        binding.sender = Account(1,"Name","EMail",1000)
        Toast.makeText(this, "id$idExtra", Toast.LENGTH_SHORT).show()
    }
}