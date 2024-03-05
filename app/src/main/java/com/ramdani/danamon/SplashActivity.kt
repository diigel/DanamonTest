package com.ramdani.danamon

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.ramdani.danamon.base.BaseActivity
import com.ramdani.danamon.databinding.ActivitySplashBinding
import com.ramdani.danamon.utils.extenstions.delay
import com.ramdani.danamon.utils.extenstions.loadImage

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun bindToolbar(): Toolbar? {
        return null
    }

    override fun getUiBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun enableBackButton(): Boolean = true

    override fun onFirstLaunch(savedInstanceState: Bundle?) {
       viewBinding?.run {
           imgSplash.loadImage(BuildConfig.logoUrl)

           showProgress()
           disposable.delay(5000){
               hideProgress()
               startActivity(Intent(this@SplashActivity,LoginActivity::class.java).apply {
                   addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                   addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
               })
           }
       }
    }

    override fun initUiListener() {}
}