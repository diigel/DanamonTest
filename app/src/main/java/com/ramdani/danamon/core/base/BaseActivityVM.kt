package com.ramdani.danamon.core.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding

abstract class BaseActivityVM <VB : ViewBinding,VM : BaseViewModel> : BaseActivity<VB>() {

    protected var baseViewModel: VM? = null

    abstract fun bindViewModel(): VM

    abstract fun observeViewModel(viewModel: VM)

    override fun onCreate(savedInstanceState: Bundle?) {
        baseViewModel = bindViewModel()
        super.onCreate(savedInstanceState)
        baseViewModel?.let { observeViewModel(it) }
    }

    protected fun handleLoading(showLoading: Boolean?) {
        if (showLoading == true) showProgress() else hideProgress()
    }

    override fun onDestroy() {
        super.onDestroy()
        baseViewModel = null
    }
}