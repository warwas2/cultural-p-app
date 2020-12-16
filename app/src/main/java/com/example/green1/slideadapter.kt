package com.example.green1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class slideadapter(context: Context,var resources:Int, var items:ArrayList<CultureProperties>,val cultureDbHelper: CultureDbHelper):
    ArrayAdapter<CultureProperties>(context,resources,items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)
        var v: View? = convertView
        if (v == null) {
            //처음, view가 생성안되어있음
            val vi = context.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = vi.inflate(R.layout.slidelayout, null)
        }
        val p = items.get(position)
        v!!.findViewById<TextView>(R.id.s_museum).text = p.name

        var str1 = p.location.split("/")
        v!!.findViewById<TextView>(R.id.s_address).text =str1[0].toString()

        v!!.findViewById<TextView>(R.id.s_address).textSize= 11F
        if(p.visit==true){

            v!!.findViewById<Button>(R.id.setvisited).text = "방문 완료"
             v!!.findViewById<TextView>(R.id.setvisited).isClickable=false

            // v!!.findViewById<TextView>(R.id.setvisited).setBackgroundResource(R.drawable.visit_stamp)
           // v!!.findViewById<TextView>(R.id.setvisited).text=""

        }else{

           // v!!.findViewById<TextView>(R.id.s_dis).text = "방문전"
            v!!.findViewById<Button>(R.id.setvisited).text ="방문 확인"
            v!!.findViewById<TextView>(R.id.setvisited).setOnClickListener {
                cultureDbHelper.updateVisit(p.name)
                //items=cultureDbHelper.loadCultureList()



                v!!.findViewById<Button>(R.id.setvisited).text = "방문 완료"
                v!!.findViewById<TextView>(R.id.setvisited).isClickable=false
                Toast.makeText(this.context," 방문 확인 !",Toast.LENGTH_SHORT).show()

            }
        }


        return v
    }
}