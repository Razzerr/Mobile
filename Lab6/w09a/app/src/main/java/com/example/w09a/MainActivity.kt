package com.example.w09a

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Ion.with(this)
            .load("https://api.thecatapi.com/api/images/get?format=json&size=med&results_per_page=10")
            .asString()
            .setCallback { e, result ->
                //textView.text = result
                processData(result)
            }

    }

    private fun processData(data : String) {

        try {
            val jsonArray = JSONArray(data)

            for (i in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(i)
                val url = json.getString("url")
                //textView.text = url
                Log.i("am2019","$url")
                val img = ImageView(this)
                grid.addView(img)
                Picasso.get()
                    .load(url)
                    .into(img)


            }

        } catch ( e: JSONException) {
            Log.wtf("json",e)
        }
    }
}
