package com.example.green1

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    lateinit var cultureProperties: CultureProperties
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        init()
    }
    fun init(){
        textView5.visibility=INVISIBLE
        page1.visibility= VISIBLE
        lateinit var mp: MediaPlayer
        cultureProperties=intent.getSerializableExtra("culture")as CultureProperties
        textView.append( cultureProperties.ctype)
        textView2.text = cultureProperties.name
        if(cultureProperties.isEmpty){
            textView3.text=""
            textView4.text=""
            textView5.text=""
            //이미지 없음
        }else{
            textView3.append( cultureProperties.location)
            textView4.append( cultureProperties.time)
            textView5.append("\n\n"+ cultureProperties.instruction)
            button.setOnClickListener {
                mp = MediaPlayer()
                mp.setDataSource(cultureProperties.voiceUrl)
                mp.prepare();
                mp.start();
            }
            button2.setOnClickListener {
              if(mp!!.isPlaying){
                  mp.pause()
              }

            }
            Ion.with(imageView).load(cultureProperties.imgUrl)
        }

        button3.setOnClickListener {
            textView5.visibility=INVISIBLE
            page1.visibility= VISIBLE
        }
        button4.setOnClickListener {
            textView5.visibility=VISIBLE
            page1.visibility= INVISIBLE
        }

    }
}