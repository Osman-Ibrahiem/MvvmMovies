package com.osman.themoviedb.views

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.osman.themoviedb.BuildConfig
import com.osman.themoviedb.di.GlideApp

object DataBindingAdapter {

    interface OnImageLoad {
        fun onLoad(isLoad: Boolean)
    }

    @BindingAdapter("app:loadImage", "app:placeholder", "app:listener", requireAll = false)
    @JvmStatic
    fun ImageView.loadImage(
        imageUrl: String?,
        placeholder: Drawable?,
        listener: OnImageLoad?,
    ) {
        listener?.onLoad(false)
        GlideApp.with(context)
            .load(imageUrl.fixImageUrl())
            .placeholder(placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    listener?.onLoad(false)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    listener?.onLoad(true)
                    return false
                }
            })
            .thumbnail(0.1f)
            .into(this)
    }

    private fun String?.fixImageUrl(): String {
        if (this.isNullOrEmpty()) return ""
        if (URLUtil.isFileUrl(this) || URLUtil.isHttpsUrl(this)) return this
        if (URLUtil.isHttpUrl(this)) return this.replaceFirst("http://", "https://")
        return BuildConfig.IMAGE_URL + this
    }

    @BindingAdapter("app:progressbarColor")
    @JvmStatic
    fun CircleProgressBar.setProgressbarColor(colorString: String?) {
        colorString?.let { color = Color.parseColor(it) }
    }

    @BindingAdapter("app:progressbarWithAnimation")
    @JvmStatic
    fun CircleProgressBar.setProgressbarBinding(progressValue: Int?) {
        progressValue?.let { setProgressWithAnimation(it.toFloat()) }
    }
}