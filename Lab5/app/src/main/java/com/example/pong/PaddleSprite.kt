package com.example.pong

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class PaddleSprite(var left: Float, var top: Float, var width : Float, var height : Float) {
    private var paint = Paint()

    fun draw(canvas : Canvas){
        paint.color = Color.parseColor("#FFFFFF")
        paint.strokeWidth = 2F
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawRect(left, top, left+width, top+height, paint)
    }

    fun update(){

    }

    fun getPos() : Array<Float> {
        return arrayOf(left, top)
    }

}