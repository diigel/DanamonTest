package com.ramdani.danamon.presentation.auth.login

import android.content.Intent
import android.os.Bundle
import com.ramdani.danamon.R
import com.ramdani.danamon.core.base.BaseActivityVM
import com.ramdani.danamon.core.enums.UserRole
import com.ramdani.danamon.core.extenstions.observe
import com.ramdani.danamon.core.extenstions.showToast
import com.ramdani.danamon.core.utils.Failure
import com.ramdani.danamon.data.local.entity.AccountEntity
import com.ramdani.danamon.databinding.ActivityLoginBinding
import com.ramdani.danamon.presentation.auth.register.RegisterActivity
import com.ramdani.danamon.presentation.main.admin.AdminMainActivity
import com.ramdani.danamon.presentation.main.user.UserMainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivityVM<ActivityLoginBinding, LoginVM>() {

    override fun bindViewModel(): LoginVM {
        val viewModel: LoginVM by viewModel()
        return viewModel
    }

    override fun observeViewModel(viewModel: LoginVM) {
        observe(viewModel.failureLiveData, ::handleFailure)
        observe(viewModel.isLoadingLiveData, ::handleLoading)
        observe(viewModel.getAccount, ::handleGetAccount)
    }

    private fun handleGetAccount(accountEntity: AccountEntity?) {
        if (accountEntity != null) {
            baseViewModel?.let {
                it.saveIsActive(true)
                it.saveUsername(accountEntity.username)
                it.savePassword(accountEntity.password)
                it.saveUserId(accountEntity.id)
            }
            if (accountEntity.role == UserRole.ADMIN){
                startActivity(AdminMainActivity.createIntent(this@LoginActivity, true, accountEntity))
            }else {
                startActivity(UserMainActivity.createIntent(this@LoginActivity, true, accountEntity))
            }
        } else {
            showToast(getString(R.string.account_not_found_text))
        }
    }


    override fun getUiBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onFirstLaunch(savedInstanceState: Bundle?) {}

    override fun initUiListener() {
        viewBinding?.run {
            btnLogin.setOnClickListener {
                baseViewModel?.getAccount(etUsername.text.toString(), etPassword.text.toString())
            }
            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    override fun handleFailure(failure: Failure?) {
        if (failure is Failure.DataNotExist) {
            showToast(getString(R.string.account_not_found_text))
        } else {
            super.handleFailure(failure)
        }
    }

}