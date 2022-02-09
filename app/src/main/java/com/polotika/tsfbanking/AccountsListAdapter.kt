package com.polotika.tsfbanking

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.polotika.tsfbanking.databinding.AccountItemBinding

class AccountsListAdapter(var list :List<Account>?= emptyList()) :RecyclerView.Adapter<AccountsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AccountItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account = list?.get(position)
        holder.bind(account!!)
        holder.binding.accountCard.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: ${holder?.binding?.account?.id?:0}")
            listener?.onAccountClickListener(holder?.binding?.account?.id?:0)
        }
    }

    override fun getItemCount() = list?.size?:0

    class ViewHolder(val binding: AccountItemBinding) :RecyclerView.ViewHolder(binding.root) {

        fun bind(account:Account){
            binding.account = account
            binding.invalidateAll()
        }

    }

    interface AccountClickListener {
        fun onAccountClickListener(id:Int)
    }

    var listener : AccountClickListener? = null

}