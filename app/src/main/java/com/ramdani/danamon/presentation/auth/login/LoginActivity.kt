package com.ramdani.danamon.presentation.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ramdani.danamon.R
import com.ramdani.danamon.core.base.BaseActivity
import com.ramdani.danamon.core.base.BaseActivityVM
import com.ramdani.danamon.core.extenstions.observe
import com.ramdani.danamon.core.extenstions.showToast
import com.ramdani.danamon.core.utils.Failure
import com.ramdani.danamon.data.local.entity.UserEntity
import com.ramdani.danamon.databinding.ActivityLoginBinding
import com.ramdani.danamon.presentation.auth.register.RegisterActivity
import com.ramdani.danamon.presentation.main.MainActivity
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

    private fun handleGetAccount(userEntity: UserEntity?) {
        if (userEntity != null) {
            startActivity(Intent(this, MainActivity::class.java))
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
        }else {
            super.handleFailure(failure)
        }
    }

}