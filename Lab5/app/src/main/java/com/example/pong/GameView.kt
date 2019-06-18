package com.example.pong

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.*
import android.view.MotionEvent
import android.content.Context.MODE_PRIVATE
import android.R.id.edit
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE








@RequiresApi(Build.VERSION_CODES.O)
class GameView(context: Context) : android.view.SurfaceView(context), android.view.SurfaceHolder.Callback{
    private var gameThread : GameThread
    private lateinit var playerOnePaddleSprite : PaddleSprite
    private lateinit var playerTwoPaddleSprite : PaddleSprite
    private lateinit var ball: BallSprite
    private var paddleWidth = 100F
    private var paddleHeight = 10F
    private var ballRadius = 4F
    private var paddleSpeed = 8F
    private val PREFS_FILENAME = "com.example.pong.prefs"
    var editor = this.context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE).edit()
    var prefs = this.context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

    init {
        holder.addCallback(this)
        gameThread = GameThread(holder, this)
        isFocusable = true

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        var retry = true
        while(retry){
            try{
                gameThread.setRunning(false)
                gameThread.join()
            } catch (e : InterruptedException){
                e.printStackTrace()
            }
            retry = false
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        playerOnePaddleSprite = PaddleSprite(width/2 - paddleWidth/2, 10F, paddleWidth, paddleHeight)
        playerTwoPaddleSprite = PaddleSprite(width/2 - paddleWidth/2, height-15F, paddleWidth, paddleHeight)
        ball = BallSprite(width/2 - ballRadius, height/2 - ballRadius, ballRadius)
        editor.putInt("plOne", 0)
        editor.putInt("plTwo", 0)
        editor.apply()
        gameThread.setRunning(true)
        gameThread.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
//        if (event.action == MotionEvent.ACTION_HOVER_ENTER) {
            if (y < height/2)
                if (x < width/2){
                    playerOnePaddleSprite.left -= paddleSpeed
                } else {
                    playerOnePaddleSprite.left += paddleSpeed
                }
            else
                if (x < width/2){
                    playerTwoPaddleSprite.left -= paddleSpeed
                } else {
                    playerTwoPaddleSprite.left += paddleSpeed
                }
//        }
        return true
    }

    fun update(){
        if (ball.left <= 0 || ball.left + 2*ball.radius >= width) ball.dirVecHor *= -1
        if (ball.top <= playerOnePaddleSprite.top - paddleHeight-paddleHeight/2){
            editor.putInt("plTwo", prefs.getInt("plTwo", 0) + 1)
            editor.apply()
            restart()
        }
        else if (ball.top <= playerOnePaddleSprite.top + 2 &&
                    ball.left + ballRadius > playerOnePaddleSprite.left &&
                    ball.left < playerOnePaddleSprite.left + paddleWidth){
                ball.dirVecHor = ((ball.left + ballRadius - playerOnePaddleSprite.left - paddleWidth/2 ) / (paddleWidth/2))/2
                ball.dirVecVer = 1 - ball.dirVecHor
            }
        else if (ball.top >= playerTwoPaddleSprite.top + paddleHeight-paddleHeight/2){
            editor.putInt("plOne", prefs.getInt("plOne", 0) + 1)
            editor.apply()
            restart()
        }
        else if (ball.top >= playerTwoPaddleSprite.top - 2 &&
                    ball.left + ballRadius > playerTwoPaddleSprite.left &&
                    ball.left < playerTwoPaddleSprite.left + paddleWidth){
            ball.dirVecHor = ((ball.left + ballRadius - playerTwoPaddleSprite.left - paddleWidth/2) / (paddleWidth/2))/2
            ball.dirVecVer = ball.dirVecHor - 1
        }

        playerOnePaddleSprite.update()
        playerTwoPaddleSprite.update()
        ball.update()
    }

    fun restart(){
        ball = BallSprite(width/2 - ballRadius, height/2 - ballRadius, ballRadius)
    }

    override fun draw(canvas : Canvas){
        super.draw(canvas)
        if (canvas != null){
            ball.draw(canvas)
            playerOnePaddleSprite.draw(canvas)
            playerTwoPaddleSprite.draw(canvas)
            val paint = Paint()
            paint.color = Color.WHITE
            paint.textSize = 64F
            canvas.drawText(prefs.getInt("plOne", 0).toString(), 20F, 60F, paint)
            canvas.drawText(prefs.getInt("plTwo", 0).toString(), width - 40F, 60F, paint)

//            debug(canvas)
        }
    }

    fun debug(canvas : Canvas){
        val paint = Paint()
        paint.color = Color.WHITE
        paint.textSize = 32F
        canvas.drawText(ball.left.toString() + ", " + ball.top.toString(), 20F, 30F, paint)
        canvas.drawText(playerOnePaddleSprite.left.toString() + ", " + playerTwoPaddleSprite.top.toString(), 20F, 60F, paint)
        canvas.drawText((ball.top <= playerOnePaddleSprite.top + 2).toString() + ", " +
                (ball.left + ballRadius > playerOnePaddleSprite.left).toString() + ", " +
                (ball.left < playerOnePaddleSprite.left + paddleWidth).toString(), 20F, 90F, paint)
        canvas.drawText((ball.top >= playerTwoPaddleSprite.top - 2).toString() + ", " +
                (ball.left + ballRadius > playerTwoPaddleSprite.left).toString() + ", " +
                (ball.left < playerTwoPaddleSprite.left + paddleWidth).toString(), 20F, 120F, paint)
    }
}