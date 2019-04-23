package com.example.gallery

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.full_image_view.*





class ImageFullView : AppCompatActivity() {
    private lateinit var imgTitle: String
    private lateinit var imgDesc: String
    private var imgRating : Double = 0.0
    private var imgID     : Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_image_view)
        imgID = intent.getIntExtra("imgID", 0)
        imgTitle = intent.getStringExtra("imgName")
        imgDesc = intent.getStringExtra("imgDesc")
        imgRating = intent.getDoubleExtra("imgRating", 0.0)
        rbPhotoRating.setOnRatingBarChangeListener { _, _, _ -> ratingChange()}
        updateImage()
        updateDesctiption()
        updateRating()
    }

    override fun onBackPressed() {
        val intento = Intent()
        intento.putExtra("imgRating", imgRating)
        setResult(Activity.RESULT_OK, intento)
        finish()
    }

    fun updateDesctiption(){
        txDescription.text = imgDesc
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun updateImage(){
        imgFullImg.setImageDrawable(getDrawable(imgID))
    }

    fun updateRating(){
        rbPhotoRating.rating = imgRating.toFloat()
    }

    fun ratingChange(){
        imgRating = rbPhotoRating.rating.toDouble()
    }
}