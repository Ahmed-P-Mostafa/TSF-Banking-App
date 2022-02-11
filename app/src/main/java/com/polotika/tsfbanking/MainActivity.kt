package com.polotika.tsfbanking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.polotika.tsfbanking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AccountsListAdapter.AccountClickListener {

    private val transactionResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.data?.getBooleanExtra("STATE",false)?:false) {
                true -> {
                    mainViewModel.getAllAccounts(this)
                    Snackbar.make(binding.root, "Transaction Done!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    private val TAG = javaClass.simpleName
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // todo update list after transaction
        Log.d(TAG, "onCreate: ")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = mainViewModel
        mainViewModel.getAllAccounts(this)
        mainViewModel.accountsListAdapter.listener = this
    }

    override fun onAccountClickListener(id: Int) {
        val transferIntent = Intent(this, TransferActivity::class.java)
        transferIntent.putExtra("Account_id", id)
        //transferIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        //startActivity(transferIntent)

        transactionResult.launch(transferIntent)
    }
}