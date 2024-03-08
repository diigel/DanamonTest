package com.ramdani.danamon.presentation.main.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramdani.danamon.core.base.BaseActivityVM
import com.ramdani.danamon.core.extenstions.observe
import com.ramdani.danamon.core.extenstions.parcelable
import com.ramdani.danamon.core.extenstions.showDeleteConfirmationDialog
import com.ramdani.danamon.core.extenstions.showToast
import com.ramdani.danamon.data.local.entity.AccountEntity
import com.ramdani.danamon.databinding.ActivityAdminMainBinding
import com.ramdani.danamon.presentation.auth.login.LoginActivity
import com.ramdani.danamon.presentation.auth.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminMainActivity : BaseActivityVM<ActivityAdminMainBinding, AdminMainVM>() {

    private val accountEntity by lazy {
        intent.parcelable<AccountEntity>(ACCOUNT_DATA)
    }

    private val accountAdapter by lazy {
        AccountAdapter()
    }

    override fun bindViewModel(): AdminMainVM {
       val viewModel : AdminMainVM by viewModel()
        return viewModel
    }

    override fun observeViewModel(viewModel: AdminMainVM) {
        observe(viewModel.failureLiveData,::handleFailure)
        observe(viewModel.isLoadingLiveData,::handleLoading)
        observe(viewModel.getAllAccount,::handleGetAllAccount)
        observe(viewModel.deletedAccount,::handleUpdateAccount)
        observe(viewModel.updateActiveAccountEvent, ::handleSignOut)
    }

    private fun handleSignOut(isSignOut: Boolean?) {
        if (isSignOut == true){
            startActivity(Intent(this@AdminMainActivity, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            })
        }
    }

    private fun handleUpdateAccount(pair: Pair<Boolean, String>?) {
        if (pair?.first == true){
            showToast(pair.second)
            accountAdapter.notifyDataSetChanged()
            baseViewModel?.getAllAccount()
        }else {
            showToast(pair?.second.orEmpty())
        }
    }

    private fun handleGetAllAccount(userEntities: List<AccountEntity>?) {
        if (userEntities?.isEmpty() == true){
            showToast("Data Not Found")
        }else {
            accountAdapter.addAdapterList(userEntities.orEmpty())
        }
    }

    override fun getUiBinding(): ActivityAdminMainBinding {
        return ActivityAdminMainBinding.inflate(layoutInflater)
    }

    override fun onFirstLaunch(savedInstanceState: Bundle?) {
        viewBinding?.run {
            rvAccount.apply {
                layoutManager = LinearLayoutManager(this@AdminMainActivity)
                adapter = accountAdapter
            }
            baseViewModel?.getAllAccount()
        }
    }

    override fun initUiListener() {
        viewBinding?.run {
            btnLogout.setOnClickListener {
                baseViewModel?.updateActiveAccount(accountEntity?.id ?: -1,accountEntity?.isActive ?: false)
            }
            accountAdapter.onItemClickEdit {
                updateAccountResult.launch(Intent(this@AdminMainActivity,RegisterActivity::class.java).apply {
                    putExtra("userEntity",it)
                })
            }

            accountAdapter.onItemClickDeleted {
                showDeleteConfirmationDialog { dialog ->
                    dialog.dismiss()
                    baseViewModel?.deleteAccount(it)
                }
            }
        }
    }

    private val updateAccountResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            baseViewModel?.getAllAccount()
        }
    }

    companion object {
        private const val ACCOUNT_DATA = "AdminMainActivity.Account.Data"

        fun createIntent(context: Context, flags: Boolean, accountEntity: AccountEntity) : Intent {
            return Intent(context, AdminMainActivity::class.java).apply {
                if (flags){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                putExtra(ACCOUNT_DATA,accountEntity)
            }
        }
    }
}