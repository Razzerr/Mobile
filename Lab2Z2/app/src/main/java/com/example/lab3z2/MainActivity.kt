package com.example.lab3z2

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val images = intArrayOf(R.drawable.img_0, R.drawable.img_1, R.drawable.img_2, R.drawable.img_3,
        R.drawable.img_4, R.drawable.img_5, R.drawable.img_6, R.drawable.img_7,
        R.drawable.img_8, R.drawable.img_9, R.drawable.img_10, R.drawable.img_11)

    var index = 0
    var answ = ""
    var endGame = false
    var shown = "".toCharArray()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var words = resources.getStringArray(R.array.words)
        answ = words.random()
        shown = "_".repeat(answ.length).toCharArray()
        setText(shown)
    }

    fun correct(c : Char){
        for (i in 0..((answ.length)-1)){
            if (answ[i].toLowerCase() == c){
                shown[i] = c
            }
        }
        setText(shown)
        endGame = true
        for (char in shown){
            if (char == '_') {
                endGame =false
                break
            }
        }
        if (endGame){
            imageView.setImageResource(R.drawable.win)
        }
    }

    fun wrong(){
        index++
        imageView.setImageResource(images[index])
        if (index == 11) endGame = true
    }

    fun setText(s : CharArray){
        var temp = ""
        for (i in 0..(s.size-2)){
            temp += s[i]
            temp += " "
        }
        temp += s[s.size-1]
        answer.text = temp.toUpperCase()
    }


    fun send(v: View?){
        if (!endGame && guess.text.isNotEmpty()) {
            val guessed = guess.text[0].toLowerCase()
            if (guessed in answ) correct(guessed)
            else wrong()
        }

    }
}
