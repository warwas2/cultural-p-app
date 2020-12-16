package com.example.insta

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.green1.MyClass
import com.example.green1.R
import java.io.Serializable
class GridAdapter(var items:ArrayList<MyClass>,var num:Int)
    : RecyclerView.Adapter<GridAdapter.ViewHolder>(), Serializable {

    var number = num

    //(2)
    inner class ViewHolder(itemView:View)
        : RecyclerView.ViewHolder(itemView){
        var iView: ImageView
        var tView: TextView
        init{
            //row에서 textView를 찾아서 초기화.
            iView = itemView.findViewById(R.id.myImg)
            tView = itemView.findViewById(R.id.myText)

            itemView.setOnClickListener {
                val position = adapterPosition
                itemClickListener?.OnItemClick(this,it,items[position],position)
            }
        }
    }

    //  viewHolder 생성(1)
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        lateinit var v :View
        if(num==0) {
            v = LayoutInflater.from(p0.context)
                .inflate(R.layout.image2, p0, false)
        }
        else{
            v = LayoutInflater.from(p0.context)
                .inflate(R.layout.image, p0, false)
        }

        return ViewHolder(v)
    }

    //item 갯수 세는 함수.
    override fun getItemCount(): Int {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return items.size
    }

    //  item과 viewHolder 연결
    //  data 연결할 때 호출되는 함수.(3)
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.e("This","여기가 이미지 로딩")
        if(number == 0) {
            var u = Uri.parse(items.get(p1).address!!)
            p0.iView.setImageURI(u)
        }
        else{
            var u = Uri.parse(items.get(p1).address!!)
            p0.iView.setImageURI(u)
            p0.tView.text = items.get(p1).date
        }
    }

    interface OnItemClickListener{
        fun OnItemClick(holder:ViewHolder, view: View, data: MyClass, position:Int)
    }

    var itemClickListener : OnItemClickListener? = null
}