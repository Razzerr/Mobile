package com.example.gallery
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.min


class MainActivity : AppCompatActivity() {
    private lateinit var images: ArrayList<ImageObject>
    private lateinit var adapter: ImageAdapter
    private var idx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        images = ArrayList()
        if (savedInstanceState == null) {
            images.add(ImageObject("canada", "Beautiful view from canada!", R.drawable.canada, 0.0))
            images.add(ImageObject("chile", "Beautiful view from chile!", R.drawable.chile, 0.0))
            images.add(ImageObject("dubai", "Beautiful view from dubai!", R.drawable.dubai, 0.0))
            images.add(ImageObject("iceland", "Beautiful view from iceland!", R.drawable.iceland, 0.0))
            images.add(ImageObject("india", "Beautiful view from india!", R.drawable.india, 0.0))
            images.add(ImageObject("new_zeland", "Beautiful view from new_zeland!", R.drawable.new_zeland, 0.0))
            images.add(ImageObject("san_francisco", "Beautiful view from san_francisco!", R.drawable.san_francisco, 0.0))
            setContentView(R.layout.activity_main)
            val metrics = DisplayMetrics()
            this.windowManager.defaultDisplay.getMetrics(metrics)
            adapter = ImageAdapter(this, images, min(metrics.heightPixels / 3, metrics.widthPixels / 3))
            grdImages.adapter = adapter
            grdImages.setOnItemClickListener { _, _, idx, _ -> click(idx) }
        }
    }

    fun click(idx : Int){
        val intent = Intent(this, ImageFullView::class.java)
        intent.putExtra("imgID", images[idx].img)
        intent.putExtra("imgName", images[idx].name)
        intent.putExtra("imgDesc", images[idx].desc)
        intent.putExtra("imgRating", images[idx].rating)
        this.idx = idx
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            var imgRating = data?.getDoubleExtra("imgRating", 0.0)
            images[this.idx].rating = imgRating!!
        }

        images.sortByDescending{it.rating}
        adapter.notifyDataSetChanged()
        grdImages.invalidateViews()
        grdImages.adapter = adapter
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("indexMax", images.size)
        var index = 1
        for (img in images) {
            outState?.putString("imgName$index", img.name)
            outState?.putString("imgDesc$index", img.desc)
            outState?.putInt("imgImg$index", img.img)
            outState?.putDouble("imgRating$index", img.rating)
            index++
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        val indexMax = savedInstanceState?.getInt("indexMax")
        var name: String
        var img: Int
        var rating : Double
        var desc: String
        for (i in 1..indexMax!!) {
            name = savedInstanceState.getString("imgName$i")!!
            img = savedInstanceState.getInt("imgImg$i")
            desc = savedInstanceState.getString("imgDesc$i")!!
            rating = savedInstanceState.getDouble("imgRating$i")
            images.add(ImageObject(name, desc, img, rating))
        }
        setContentView(R.layout.activity_main)
        val metrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(metrics)
        adapter = ImageAdapter(this, images, min(metrics.heightPixels / 3, metrics.widthPixels / 3))
        grdImages.adapter = adapter
        grdImages.setOnItemClickListener { _, _, idx, _ -> click(idx) }
        adapter.notifyDataSetChanged()
    }
//
//    private fun deleteTask(index: Int): Boolean {
//        val alert = AlertDialog.Builder(this)
//        alert.setTitle("Delete task")
//        alert.setMessage("Are you sure you want to delete this task?")
//        alert.setPositiveButton("Yes") { _, _ ->
//            tasks.removeAt(index)
//            adapter.notifyDataSetChanged()
//        }
//        alert.setNegativeButton("No") { _, _ -> }
//        alert.show()
//        return true
//    }
//
//    fun sortByPriority(view: View) {
//        tasks.sortBy { it.taskPriority }
//        adapter.notifyDataSetChanged()
//    }
//
//    fun sortByDate(view: View) {
//        tasks.sortBy { it.taskDate }
//        adapter.notifyDataSetChanged()
//    }
}

