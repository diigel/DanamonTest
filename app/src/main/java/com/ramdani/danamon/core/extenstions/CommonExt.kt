package com.ramdani.danamon.core.extenstions

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import android.os.Parcelable
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ramdani.danamon.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun ImageView.loadImage(
    path: String?,
    @DrawableRes errorImage: Int = R.drawable.img_error,
    @DrawableRes placeholder: Int = R.drawable.img_placeholder
) {
    Glide.with(this)
        .load(path)
        .placeholder(placeholder)
        .error(errorImage)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(this)
}

fun CompositeDisposable.delay(long: Long, action: () -> Unit) {
    val disposable = Observable.just(long)
        .subscribeOn(Schedulers.io())
        .delay(long, TimeUnit.MILLISECONDS)
        .map {
            return@map action
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            it.invoke()
        }
    add(disposable)
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) = liveData.observe(this, Observer(body))

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}