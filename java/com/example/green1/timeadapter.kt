package com.example.green1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class timeadapter(context: Context, val resource:Int, var list:ArrayList<timeclass>)
    : ArrayAdapter<timeclass>(context,resource, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)
        var v: View? = convertView
        if (v == null) {
            //처음, view가 생성안되어있음
            val vi = context.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = vi.inflate(R.layout.timelayout, null)
        }
        val p = list.get(position)
        v!!.findViewById<TextView>(R.id.musename).text = p.name
        v!!.findViewById<TextView>(R.id.musestart1).text = "평일관람시작 : "+p.start1
        v!!.findViewById<TextView>(R.id.museend1).text = "평일관람종료 : "+ p.end1
        v!!.findViewById<TextView>(R.id.musestart2).text = "공휴일관람시작 : "+p.start2
        v!!.findViewById<TextView>(R.id.museend2).text = "공휴일관람종료 : "+ p.end2

        return v
    }
}

