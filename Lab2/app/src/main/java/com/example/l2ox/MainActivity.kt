package com.example.l2ox

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.max


class MainActivity : Activity(), View.OnClickListener {
    private var buttons = arrayOfNulls<Array<SquareButton?>>(5)
    private var end:String = ""

    override fun onClick(v: View?) {
        butRefresh(v)
    }

    private fun butRefresh(v: View?){
        val butRef = v as Button
        if ((butRef.text != "") || (end != "")) return
        val text = xo.text
        butRef.text = text
        xo.text = if (xo.text == "O") "X" else "O"
        val column = v.id.rem(10)
        val row = (v.id - column)/10
        when (row){
            0 -> rowCheck1[v.id] = text.toString()
            1 -> rowCheck2[v.id] = text.toString()
            2 -> rowCheck3[v.id] = text.toString()
            3 -> rowCheck4[v.id] = text.toString()
            4 -> rowCheck5[v.id] = text.toString()
        }
        when (column){
            0 -> colCheck1[v.id] = text.toString()
            1 -> colCheck2[v.id] = text.toString()
            2 -> colCheck3[v.id] = text.toString()
            3 -> colCheck4[v.id] = text.toString()
            4 -> colCheck5[v.id] = text.toString()
        }
        if (column == row) diagCheck1[v.id] = text.toString()
        if (column + row == 4) diagCheck2[v.id] = text.toString()
        checkWin()
        if (xo.text == "O") aiMove()
    }

    private fun checkWin() {
        for (line in lines){
            if (line.size == 5 && line.all{x -> x.value == line.values.first()}) end = line.values.first()
        }

        if (end != ""){
            val com: String = end + "s WIN!"
            xo.textSize = 40F
            xo.text = com
        }
    }

    private fun aiMove() {
        val rates : HashMap<Int, Int> = hashMapOf()

        for (line in lines){
            if (line.size > 0) {
                var xs = 0
                var os = 0

                for (char in line) {
                    when (char.value) {
                        "X" -> xs += 1
                        "O" -> os += 1
                    }
                }

                for (char in line) {
                    var sum = 0
                    if (xs + os == 0) {
                        sum = 150
                    } else {
                        if (os == 0) {
                            when (xs) {
                                1 -> sum = 10
                                2 -> sum = 100
                                3 -> sum = 5000
                                4 -> sum = 10000
                            }
                        } else {
                            if (xs == 0) {
                                when (os) {
                                    1 -> sum = 50
                                    2 -> sum = 500
                                    3 -> sum = 1000
                                    4 -> sum = 50000
                                }
                            } else {
                                sum = 0
                            }
                        }
                    }
                    rates[char.key] = if (!rates.containsKey(char.key)) sum else max(sum, rates[char.key]!!)
                }
            }
        }

        for (i in 0..4){
            for (j in 0..4){
                if (buttons[i]!![j]!!.text != "") rates[i*10+j] = -1
            }
        }
        val max = rates.values.max()!!
        if (max == -1){
            xo.text = "TIE"
        }

        var idx = -1
        for (rate in rates){
            if (rate.value ==  max){
                idx = rate.key
                break
            }
        }

        butRefresh(findViewById<Button>(idx))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..4) {
            buttons[i] = arrayOfNulls(5)
            for (j in 0..4) {
                buttons[i]!![j] = SquareButton(this)
                buttons[i]!![j]!!.background = getDrawable(R.drawable.button_bg)
                buttons[i]!![j]!!.setOnClickListener(this)
                buttons[i]!![j]!!.id = 10 * i + j
                when (i) {
                    0 -> row1.addView(buttons[i]!![j])
                    1 -> row2.addView(buttons[i]!![j])
                    2 -> row3.addView(buttons[i]!![j])
                    3 -> row4.addView(buttons[i]!![j])
                    4 -> row5.addView(buttons[i]!![j])

                }

                when (i){
                    0 -> rowCheck1[i*10+j] = ""
                    1 -> rowCheck2[i*10+j] = ""
                    2 -> rowCheck3[i*10+j] = ""
                    3 -> rowCheck4[i*10+j] = ""
                    4 -> rowCheck5[i*10+j] = ""
                }
                when (j){
                    0 -> colCheck1[i*10+j] = ""
                    1 -> colCheck2[i*10+j] = ""
                    2 -> colCheck3[i*10+j] = ""
                    3 -> colCheck4[i*10+j] = ""
                    4 -> colCheck5[i*10+j] = ""
                }
                if (i == j) diagCheck1[i*10+j] = ""
                if (i + j == 4) diagCheck2[i*10+j] = ""
            }
        }
    }

    var diagCheck1 = hashMapOf<Int, String>()
    var diagCheck2 = hashMapOf<Int, String>()
    var rowCheck1 = hashMapOf<Int, String>()
    var rowCheck2 = hashMapOf<Int, String>()
    var rowCheck3 = hashMapOf<Int, String>()
    var rowCheck4 = hashMapOf<Int, String>()
    var rowCheck5 = hashMapOf<Int, String>()
    var colCheck1 = hashMapOf<Int, String>()
    var colCheck2 = hashMapOf<Int, String>()
    var colCheck3 = hashMapOf<Int, String>()
    var colCheck4 = hashMapOf<Int, String>()
    var colCheck5 = hashMapOf<Int, String>()
    val lines = arrayOf(diagCheck1, diagCheck2, rowCheck1, rowCheck2, rowCheck3, rowCheck4, rowCheck5, colCheck1, colCheck2, colCheck3, colCheck4, colCheck5)

}

class SquareButton(context: Context) : Button(context) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        setMeasuredDimension(width, width)
    }
}
