package com.ramdani.danamon.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.ramdani.danamon.R
import com.ramdani.danamon.core.base.BaseActivity
import com.ramdani.danamon.core.base.BaseActivityVM
import com.ramdani.danamon.core.enums.UserRole
import com.ramdani.danamon.databinding.ActivitySplashBinding
import com.ramdani.danamon.core.extenstions.delay
import com.ramdani.danamon.core.extenstions.observe
import com.ramdani.danamon.core.extenstions.showToast
import com.ramdani.danamon.core.utils.Failure
import com.ramdani.danamon.data.local.entity.AccountEntity
import com.ramdani.danamon.presentation.auth.login.LoginActivity
import com.ramdani.danamon.presentation.main.admin.AdminMainActivity
import com.ramdani.danamon.presentation.main.user.UserMainActivity
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivityVM<ActivitySplashBinding,SplashVM>() {
    override fun bindViewModel(): SplashVM {
        val viewModel : SplashVM by viewModel()
        return viewModel
    }

    override fun observeViewModel(viewModel: SplashVM) {
        observe(viewModel.failureLiveData, ::handleFailure)
        observe(viewModel.getAccount, ::handleGetAccount)
    }

    private fun handleGetAccount(accountEntity: AccountEntity?) {
        disposable.delay(2000){
            if (accountEntity?.isActive == true) {
                if (accountEntity.role == UserRole.ADMIN){
                    startActivity(AdminMainActivity.createIntent(this@SplashActivity, true, accountEntity))
                }else {
                    startActivity(UserMainActivity.createIntent(this@SplashActivity, true, accountEntity))
                }
            }else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                })
            }
        }
    }
    override fun getUiBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onFirstLaunch(savedInstanceState: Bundle?) {
       viewBinding?.run {
           baseViewModel?.getAccount()
       }
    }

    override fun initUiListener() {}

    override fun handleFailure(failure: Failure?) {
        if (failure is Failure.DataNotExist) {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            })
        }else {
            super.handleFailure(failure)
        }
    }
}