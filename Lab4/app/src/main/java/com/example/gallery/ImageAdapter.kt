package com.example.gallery

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class ImageAdapter(private val context: Context, private val imgValues: ArrayList<ImageObject>, private val size : Int) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var gridView: View
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.image, null)
            val imageView = gridView.findViewById(R.id.grid_item_image) as ImageView
            imageView.layoutParams.width = size
            imageView.layoutParams.height = size

            imageView.setImageDrawable(imgValues[position].getDraw(this.context))
        } else {
            gridView = convertView
        }
        return gridView
    }

    override fun getCount(): Int {
        return imgValues.size
    }

    override fun getItem(position: Int): Any? {
        return imgValues[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
