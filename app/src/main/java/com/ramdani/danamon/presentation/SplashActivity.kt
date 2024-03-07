package com.ramdani.danamon.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.ramdani.danamon.core.base.BaseActivity
import com.ramdani.danamon.databinding.ActivitySplashBinding
import com.ramdani.danamon.core.extenstions.delay
import com.ramdani.danamon.presentation.auth.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getUiBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onFirstLaunch(savedInstanceState: Bundle?) {
       viewBinding?.run {

           disposable.delay(5000){
               startActivity(Intent(this@SplashActivity, LoginActivity::class.java).apply {
                   addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                   addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
               })
           }
       }
    }

    override fun initUiListener() {}
}