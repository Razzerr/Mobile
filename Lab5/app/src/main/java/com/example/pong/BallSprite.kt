package com.example.pong

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class BallSprite(var left: Float, var top: Float, var radius : Float) {
    private var speed = 8F
    var dirVecVer = 1F
    var dirVecHor = 0F
    private var paint : Paint = Paint()

    fun draw(canvas : Canvas){
        paint.color = Color.parseColor("#FFFFFF")
        paint.strokeWidth = 2F
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawRect(left, top, left+2*radius, top+2*radius, paint)
    }

    fun update(){
        this.left += dirVecHor * speed
        this.top += dirVecVer * speed
    }

    fun getPos() : Array<Float> {
        return arrayOf(left, top)
    }

}