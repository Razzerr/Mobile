package com.example.pong

import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.SurfaceHolder

class GameThread(private var surfaceHolder: SurfaceHolder, private var gameView: GameView) : Thread() {
    private var running : Boolean = false
    companion object {
        private var canvas : Canvas?  = null
    }

    fun setRunning(run: Boolean){
        this.running = run
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run(){
        while(running){
            canvas = null
            try {
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder){
                    this.gameView.update()
                    this.gameView.draw(canvas!!)
                }
            } catch (e: Exception){
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}