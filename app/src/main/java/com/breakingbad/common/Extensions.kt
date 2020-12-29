package com.breakingbad.common

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.view.ViewCompat

//region - View
fun View.fadeOut(duration: Long = 1000L) {
    ViewCompat.animate(this)
        .alpha(0F)
        .setDuration(duration)
        .withEndAction { this.visibility = View.GONE }
        .start()
}

fun View.fadeIn(duration: Long = 1000L) {
    visibility = View.VISIBLE
    alpha = 0F
    ViewCompat.animate(this)
        .alpha(1F)
        .setDuration(duration)
        .start()
}
//endregion

//region - ImageView
fun ImageView.setRounded(imageSize: Int) {
    val bitmap = this.drawable as? BitmapDrawable
    bitmap?.let {
        val image = RoundedBitmapDrawableFactory.create(resources, it.bitmap)
        image.isCircular = true
        image.cornerRadius = imageSize / 2.0f
        this.setImageDrawable(image)
    }
}
//endregion

