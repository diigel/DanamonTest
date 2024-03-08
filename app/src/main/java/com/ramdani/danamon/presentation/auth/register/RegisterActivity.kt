package com.ramdani.danamon.presentation.auth.register

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import com.ramdani.danamon.R
import com.ramdani.danamon.core.base.BaseActivityVM
import com.ramdani.danamon.core.enums.UserRole
import com.ramdani.danamon.core.extenstions.disposedBy
import com.ramdani.danamon.core.extenstions.observe
import com.ramdani.danamon.core.extenstions.parcelable
import com.ramdani.danamon.core.extenstions.showToast
import com.ramdani.danamon.data.local.entity.AccountEntity
import com.ramdani.danamon.databinding.ActivityRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class RegisterActivity : BaseActivityVM<ActivityRegisterBinding, RegisterVM>() {

    private val accountEntity by lazy {
        intent?.parcelable<AccountEntity>("userEntity")
    }
    private val spinnerList by lazy {
        listOf(
            "-- Pilih --",
            "Admin",
            "User"
        )
    }

    private val spinnerAdapter by lazy {
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerList
        )
    }

    override fun bindViewModel(): RegisterVM {
        val viewModel: RegisterVM by viewModel()
        return viewModel
    }

    override fun observeViewModel(viewModel: RegisterVM) {
        observe(viewModel.isLoadingLiveData, ::handleLoading)
        observe(viewModel.failureLiveData, ::handleFailure)
        observe(viewModel.emailEvent, ::handleEmailEvent)
        observe(viewModel.usernameEvent, ::handleUsernameEvent)
        observe(viewModel.passwordEvent, ::handlePasswordEvent)
        observe(viewModel.spinnerEvent, ::handleRoleEvent)
        observe(viewModel.createAccount, ::handleCreateAccount)
        observe(viewModel.updateAccount, ::handleUpdateAccount)
    }

    private fun handleUpdateAccount(pair: Pair<Boolean, String>?) {
        if (pair?.first == true) {
            showToast(pair.second)
            setResult(RESULT_OK)
            finish()
        } else {
            showToast(pair?.second.orEmpty())
        }
    }

    private fun handleRoleEvent(userRole: UserRole?) {
        viewBinding?.txtErrorSpinner?.isGone = userRole != null && userRole != UserRole.NONE
    }

    private fun handleCreateAccount(pair: Pair<Boolean, String>?) {
        if (pair?.first == true) {
            showToast(pair.second)
            finish()
        } else {
            showToast(pair?.second.orEmpty())
        }
    }

    private fun handlePasswordEvent(password: String?) {
        viewBinding?.txtInputPassword?.apply {
            error = if (password.isNullOrEmpty()) "Please input password" else null
        }
    }

    private fun handleUsernameEvent(username: String?) {
        viewBinding?.txtInputUsername?.apply {
            error = if (username.isNullOrEmpty()) "Please input username" else null
        }
    }

    private fun handleEmailEvent(email: String?) {
        viewBinding?.txtInputEmail?.apply {
            error = if (email.isNullOrEmpty()) "Please input email" else null
        }
    }

    override fun getUiBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onFirstLaunch(savedInstanceState: Bundle?) {
        viewBinding?.run {
            spinnerRole.adapter = spinnerAdapter

            if (accountEntity != null) {
                spinnerRole.setSelection(if (accountEntity?.role == UserRole.ADMIN) 1 else 2)
                etEmail.setText(accountEntity?.email)
                etUsername.setText(accountEntity?.username)
                etPassword.setText(accountEntity?.password)
                txtTitle.text = getString(R.string.update_user_text)
                btnRegister.text = getString(R.string.update_user_text)

                baseViewModel?.emailEvent?.value = accountEntity?.email
                baseViewModel?.usernameEvent?.value = accountEntity?.username
                baseViewModel?.passwordEvent?.value = accountEntity?.password
                baseViewModel?.spinnerEvent?.value = accountEntity?.role
            }
        }
    }

    override fun initUiListener() {
        viewBinding?.run {
            imgBack.setOnClickListener {
                finish()
            }
            spinnerRole.itemSelections()
                .skipInitialValue()
                .subscribe {
                    when (it) {
                        1 -> {
                            baseViewModel?.spinnerEvent?.value = UserRole.ADMIN
                        }

                        2 -> {
                            baseViewModel?.spinnerEvent?.value = UserRole.USER
                        }
                    }
                }.disposedBy(disposable)

            etEmail.textChanges()
                .skipInitialValue()
                .map { it.toString() }
                .subscribe { username ->
                    baseViewModel?.emailEvent?.value = username.ifEmpty { null }
                }.disposedBy(disposable)

            etUsername.textChanges()
                .skipInitialValue()
                .map { it.toString() }
                .subscribe { username ->
                    baseViewModel?.usernameEvent?.value = username.ifEmpty { null }
                }.disposedBy(disposable)

            etPassword.textChanges()
                .skipInitialValue()
                .map { it.toString() }
                .subscribe { password ->
                    baseViewModel?.passwordEvent?.value = password.ifEmpty { null }
                }.disposedBy(disposable)

            btnRegister.setOnClickListener {
                val email = baseViewModel?.emailEvent?.value.orEmpty()
                val username = baseViewModel?.usernameEvent?.value.orEmpty()
                val password = baseViewModel?.passwordEvent?.value.orEmpty()
                val role = baseViewModel?.spinnerEvent?.value ?: UserRole.NONE

                when {
                    email.isEmpty() -> handleUsernameEvent(username)
                    username.isEmpty() -> handleUsernameEvent(username)
                    password.isEmpty() -> handlePasswordEvent(password)
                    role == UserRole.NONE -> handleRoleEvent(role)

                    else -> {
                        if (accountEntity != null) {
                            baseViewModel?.updateAccount(
                                AccountEntity(
                                    email = email,
                                    username = username,
                                    password = password,
                                    role = role,
                                    isActive = accountEntity?.isActive ?: false
                                )
                            )
                        } else {
                            baseViewModel?.createAccount(
                                AccountEntity(
                                    email = email,
                                    username = username,
                                    password = password,
                                    role = role,
                                    isActive = false
                                )
                            )
                        }
                    }
                }

            }
        }
    }

}