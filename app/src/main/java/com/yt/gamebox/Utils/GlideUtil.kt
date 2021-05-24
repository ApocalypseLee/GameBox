package com.yt.gamebox.Utils

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yt.gamebox.R
import java.io.ByteArrayOutputStream


object GlideUtil {
    fun displayImgByUri(context: Context, imgPath: String, view: View) {
        Glide.with(context).load(imgPath).placeholder(R.drawable.loading).error(R.drawable.loading).into(view as ImageView)
    }

    fun displayImgByResId(context: Context, imgPath: Int, view: View) {
        Glide.with(context).load(imgPath).placeholder(R.drawable.loading).error(R.drawable.loading).into(view as ImageView)
    }

    fun displayImgByUri(context: Context, imgPath: String, view: View, defaultImg: Int) {
        Glide.with(context).load(imgPath).placeholder(defaultImg).error(defaultImg)
            .into(view as ImageView)
    }

    fun displayImgByResId(context: Context, imgPath: Int, view: View, defaultImg: Int) {
        Glide.with(context).load(imgPath).placeholder(defaultImg).error(defaultImg)
            .into(view as ImageView)
    }

    fun displayRoundImgByPath(context: Context, imgPath: String, view: View, defaultImg: Int) {
        val optionsCircle = RequestOptions().circleCrop()
        Glide.with(context).load(imgPath).placeholder(defaultImg).error(defaultImg)
            .apply(optionsCircle).into(view as ImageView)
    }

    fun displayRoundImgByBitmap(context: Context, bitmap: Bitmap, view: View, defaultImg: Int) {
        val baos = ByteArrayOutputStream()
        val optionsCircle = RequestOptions().circleCrop()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val bytes = baos.toByteArray()
        Glide.with(context).load(bytes).placeholder(defaultImg).error(defaultImg)
            .apply(optionsCircle).into(view as ImageView)
    }
}