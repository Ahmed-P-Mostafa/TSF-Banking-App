package com.polotika.tsfbanking

import android.content.Intent
import android.content.Intent.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.polotika.tsfbanking.databinding.ActivityTransferBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

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
        observers()
    }

    private fun observers(){
        lifecycleScope.launchWhenResumed {
            transferViewModel.uiFlow.collectLatest {
                when(it){
                    is TransferViewModel.TransferState.Failed -> {
                        showSnackBar(it.message,false)
                    }
                    is TransferViewModel.TransferState.Success -> {
                        transferViewModel.updateAccountsBalance(this@TransferActivity)
                        showSnackBar(it.message,true)
                    }
                }
            }
        }

    }

    private fun showSnackBar(message:String, state:Boolean){
        Log.d("TAG", "showSnackBar: $message")
        Log.d("TAG", "showSnackBar: $state")
        val snackbar = Snackbar.make(binding.root,message,Snackbar.LENGTH_SHORT)

        if (state){
               /*snackbar.addCallback(
                    object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            Log.d("TAG", "onDismissed: ")
                            val intent = Intent(this@TransferActivity,MainActivity::class.java)
                            intent.putExtra("STATE",true)
                            setResult(0,intent)
                            finish()


                            *//*intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()*//*
                        }
                    })*/
            val intent = Intent(this@TransferActivity,MainActivity::class.java)
            intent.putExtra("STATE",true)
            setResult(0,intent)
            finish()
            }
        snackbar.show()
    }
}