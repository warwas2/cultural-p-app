package com.example.green1

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class row3adapter(val items:ArrayList<String>,val imgs:ArrayList<Int>)
    : RecyclerView.Adapter<row3adapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? =null
    interface OnItemClickListener{
        fun OnItemClick(holder:ViewHolder,view: View,data:String,position: Int)//나를 터치하면 이 함수 이용한
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v= LayoutInflater.from(p0.context).inflate(R.layout.row3,p0,false)
        return ViewHolder(v) //row객체의 인스턴스를 viewholder에 전달.
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return items.size //저장된 배열의 item개수 반환
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.textView.text=items.get(p1).toString()
        p0.imageView.setImageResource(imgs.get(p1))
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var textView: TextView
        var imageView:ImageView
        init{ //textview객체 초기화
            textView=itemView.findViewById(R.id.textView)
            imageView=itemView.findViewById(R.id.image3)
            itemView.setOnClickListener{
                val position=adapterPosition
                itemClickListener?.OnItemClick(this,it,items[position],position)
            }

        }
    }

}
