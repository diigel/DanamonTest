package com.ramdani.danamon.presentation.main.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramdani.danamon.core.base.BaseActivityVM
import com.ramdani.danamon.core.extenstions.observe
import com.ramdani.danamon.data.local.entity.UserEntity
import com.ramdani.danamon.data.response.ResponseDataPhoto
import com.ramdani.danamon.databinding.ActivityUserMainBinding
import com.ramdani.danamon.presentation.main.AdminMainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserMainActivity : BaseActivityVM<ActivityUserMainBinding, UserMainVM>() {

    private val photoAdapter by lazy {
        PhotoAdapter()
    }
    override fun bindViewModel(): UserMainVM {
        val viewModel: UserMainVM by viewModel()
        return viewModel
    }

    override fun observeViewModel(viewModel: UserMainVM) {
        observe(viewModel.failureLiveData,::handleFailure)
        observe(viewModel.photoEvent, ::handlePhotoEvent)
        observe(viewModel.progressBarEvent, ::handleLoadingPaging)
    }

    private fun handleLoadingPaging(isShow: Boolean?) {
        viewBinding?.progress?.isVisible = isShow == true
    }

    private fun handlePhotoEvent(pagingData: PagingData<ResponseDataPhoto>?) {
        pagingData?.let { data ->
            photoAdapter.submitData(lifecycle,data)
        }
    }

    override fun getUiBinding(): ActivityUserMainBinding {
        return ActivityUserMainBinding.inflate(layoutInflater)
    }

    override fun onFirstLaunch(savedInstanceState: Bundle?) {
        viewBinding?.run {
            baseViewModel?.getPhoto()
            rvPhoto.apply {
                layoutManager = LinearLayoutManager(this@UserMainActivity)
                adapter = photoAdapter
            }
        }
    }

    override fun initUiListener() {
        viewBinding?.run {

        }
    }

    companion object {
        private const val USER_DATA = "UserMainActivity.User.Data"

        fun createIntent(context: Context, flags: Boolean, userEntity: UserEntity) : Intent {
            return Intent(context, UserMainActivity::class.java).apply {
                if (flags){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                putExtra(USER_DATA,userEntity)
            }
        }
    }
}