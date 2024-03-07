package com.ramdani.danamon.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ramdani.danamon.core.base.BaseActivityVM
import com.ramdani.danamon.core.extenstions.observe
import com.ramdani.danamon.core.extenstions.parcelable
import com.ramdani.danamon.data.local.entity.UserEntity
import com.ramdani.danamon.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminMainActivity : BaseActivityVM<ActivityMainBinding,AdminMainVM>() {

    private val userEntity by lazy {
        intent.parcelable<UserEntity>(USER_DATA)
    }
    override fun bindViewModel(): AdminMainVM {
       val viewModel : AdminMainVM by viewModel()
        return viewModel
    }

    override fun observeViewModel(viewModel: AdminMainVM) {
        observe(viewModel.failureLiveData,::handleFailure)
        observe(viewModel.isLoadingLiveData,::handleLoading)
    }

    override fun getUiBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onFirstLaunch(savedInstanceState: Bundle?) {
        viewBinding?.run {

        }
    }

    override fun initUiListener() {
        viewBinding?.run {

        }
    }

    companion object {
        private const val USER_DATA = "MainActivity.User.Data"

        fun createIntent(context: Context, flags: Boolean,userEntity: UserEntity) : Intent {
            return Intent(context,AdminMainActivity::class.java).apply {
                if (flags){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                putExtra(USER_DATA,userEntity)
            }
        }
    }
}