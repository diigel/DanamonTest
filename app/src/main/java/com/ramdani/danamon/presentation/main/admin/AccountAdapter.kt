package com.ramdani.danamon.presentation.main.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ramdani.danamon.core.base.BaseDiffCallback
import com.ramdani.danamon.data.local.entity.AccountEntity
import com.ramdani.danamon.databinding.ItemListUserBinding

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    private val accountEntity: MutableList<AccountEntity> = mutableListOf()

    private var itemClickEdit: ((accountEntity: AccountEntity) -> Unit)? = null
    private var itemClickDeleted: ((accountEntity: AccountEntity) -> Unit)? = null

    fun addAdapterList(dataList: List<AccountEntity>) {
        val diffCallback = BaseDiffCallback(this.accountEntity, dataList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.accountEntity.clear()
        this.accountEntity.addAll(dataList)
        this.accountEntity.distinctBy { it.id }
        diffResult.dispatchUpdatesTo(this)
    }

    fun onItemClickEdit(itemClick: (data: AccountEntity) -> Unit) {
        this.itemClickEdit = itemClick
        notifyDataSetChanged()
    }

    fun onItemClickDeleted(itemClick: (data: AccountEntity) -> Unit) {
        this.itemClickDeleted = itemClick
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(ItemListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return accountEntity.size
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        return holder.bind(accountEntity[position], itemClickEdit, itemClickDeleted)
    }

    inner class AccountViewHolder(private val binding: ItemListUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            accountEntity: AccountEntity,
            itemClickEdit: ((accountEntity: AccountEntity) -> Unit)?,
            itemClickDeleted: ((accountEntity: AccountEntity) -> Unit)?
        ) {
            binding.run {
                txtUsername.text = accountEntity.username
                txtEmail.text = accountEntity.email
                imgEdit.setOnClickListener {
                    itemClickEdit?.invoke(accountEntity)
                }
                imgDeleted.setOnClickListener {
                    itemClickDeleted?.invoke(accountEntity)
                }
            }
        }
    }
}