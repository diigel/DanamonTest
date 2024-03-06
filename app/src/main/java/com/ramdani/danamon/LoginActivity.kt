package com.ramdani.danamon

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.ramdani.danamon.base.BaseActivity
import com.ramdani.danamon.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun getUiBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onFirstLaunch(savedInstanceState: Bundle?) {
        viewBinding?.run {

        }
    }

    override fun initUiListener() {
        viewBinding?.run {

        }
    }

}