package com.ramdani.danamon.base

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.ramdani.danamon.LoginActivity
import com.ramdani.danamon.R
import com.ramdani.danamon.utils.extenstions.showToast
import com.ramdani.danamon.utils.Failure
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var mToolbar: Toolbar? = null
    var disposable = CompositeDisposable()
    var viewBinding: VB? = null
    private lateinit var progress: Dialog
    private lateinit var rootView: ViewGroup

    abstract fun bindToolbar(): Toolbar?
    abstract fun getUiBinding(): VB
    abstract fun enableBackButton(): Boolean
    abstract fun onFirstLaunch(savedInstanceState: Bundle?)
    abstract fun initUiListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = getUiBinding()
        setContentView(viewBinding?.root)
        rootView = window.decorView.findViewById(android.R.id.content)
        setupToolbar()
        initProgressDialog()
        onFirstLaunch(savedInstanceState)
        initUiListener()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
        viewBinding = null
    }

    private fun setupToolbar() {
        bindToolbar()?.let {
            mToolbar = it
            setSupportActionBar(mToolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(enableBackButton())
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            }
        }
    }

    private fun initProgressDialog() {
        if (!::progress.isInitialized) {
            progress = Dialog(this)
            progress.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progress.setContentView(LayoutInflater.from(this).inflate(R.layout.item_progress, rootView, false))
            progress.setCancelable(false)
            val mainView = progress.window?.decorView?.findViewById<View>(android.R.id.content)

            mainView?.let {
                val density = resources.displayMetrics.density
                val marginSizeInDp = 16
                val marginSizeInPx = (marginSizeInDp * density).toInt()

                val layoutParams = it.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.marginStart = marginSizeInPx
                layoutParams.marginEnd = marginSizeInPx
                it.layoutParams = layoutParams
            }

            progress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progress.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    fun showProgress() {
        if (!progress.isShowing) {
            progress.show()
        }
    }

    fun hideProgress() {
        if (progress.isShowing) progress.dismiss()
    }

    open fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> showToast(getString(R.string.error_disconnect))

            is Failure.ServerError -> {
                showToast(failure.message)
            }

            is Failure.ExpiredSession -> {
                showToast(getString(R.string.session_expired_error_toast))
                startActivity(Intent(this,LoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                })
                finish()
            }

            is Failure.DataNotExist -> {
                showToast(failure.message)
            }

            else -> {
                showToast(getString(R.string.error_default))
            }
        }
    }
}