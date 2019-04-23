package com.example.gallery

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

class ImageObject (var name: String, var desc: String, var img : Int, var rating: Double) {
    fun getDraw(context : Context): Drawable? {
        return ContextCompat.getDrawable(context, img)

    }
}
