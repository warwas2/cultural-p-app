package com.example.green1
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import java.io.Serializable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class Home : AppCompatActivity(), Serializable {
    var flag = -1
    var data : MyClass? = null
    val SELECT_IMAGE = 100
    var imgUri : Uri? = null
    lateinit var uri :Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        flag = intent.getIntExtra("flag",0)

        initPermission()
        instaImage.setOnClickListener {
            var type = "image/*"
            var share = Intent(Intent.ACTION_SEND)
            share.type = type
            uri = imgUri!!
            share.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(share,"Share to"))
        }

        if(intent.hasExtra("immg")){
            data = intent?.getSerializableExtra("immg") as MyClass?
            //  Log.e("Noa","${data?.title}")
            var tt = data?.title
            Dtitle.setText(tt)
            Ddate.text = data?.date
            mainText.setText(data?.text)
            imgUri = Uri.parse(data?.address as String)
            if(data?.address == null) {
                Log.e("Noa","Empty")
            }
            else {
                Log.e("Noa","Not Empty")
                Log.e("Noa","${data?.address}")
                instaimageView2.setImageURI(Uri.parse(data?.address))
            }
        }

        save.setOnClickListener {
            var temp = MyClass(Dtitle!!.text.toString(),imgUri.toString(),getDate(),mainText.text.toString())
            if(flag == 2){
                var result = Intent()
                result.putExtra("update",temp)
                setResult(Activity.RESULT_OK, result)
                finish()
            }
            else if(flag == 1){
                var result = Intent()
                result.putExtra("result", temp)
                Log.e("Noa", "put Extra")
                setResult(Activity.RESULT_OK, result)
                finish()
            }
            else if(flag == 0){
                Toast.makeText(applicationContext,"fxck",Toast.LENGTH_SHORT).show()
            }else{
                var ss="nothing"
                var result = Intent()
                result.putExtra("update",temp)
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }


    }

    fun btnClick(view: View) {
        // TODO
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        imgUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        //.data = imgUri
        startActivityForResult(intent,SELECT_IMAGE)
    } //이미지 불러오기

    fun getDate():String{
        val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.KOREA)
        return simpleDateFormat.format(Date())
    }

    fun initPermission(){
        if(!checkAppPermission (arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("반드시 이미지 데이터에 대한 권한이 허용되어야 합니다.")
                .setTitle("권한 허용")
                .setIcon(R.drawable.abc_ic_star_black_48dp)
            builder.setPositiveButton("OK") { _, _ ->
                askPermission (arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), SELECT_IMAGE );
            }
            val dialog = builder.create()
            dialog.show()
        }else{
            Toast . makeText ( getApplicationContext (),
                "권한이 승인되었습니다." , Toast . LENGTH_SHORT ). show ();
        }
    }
    fun checkAppPermission(requestPermission: Array<String>): Boolean {
        val requestResult = BooleanArray(requestPermission.size)
        for (i in requestResult.indices) {
            requestResult[i] = ContextCompat.checkSelfPermission(
                this,
                requestPermission[i]
            ) == PackageManager.PERMISSION_GRANTED
            if (!requestResult[i]) {
                return false
            }
        }
        return true
    } // checkAppPermission
    fun askPermission(requestPermission: Array<String>, REQ_PERMISSION: Int) {
        ActivityCompat.requestPermissions(
            this, requestPermission,
            REQ_PERMISSION
        )
    } // askPermission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SELECT_IMAGE -> if (checkAppPermission(permissions)) { //퍼미션 동의했을 때 할 일
                Toast.makeText(this, "권한이 승인됨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거절됨", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO
                instaimageView2.setImageURI(data!!.data)
                imgUri = data!!.data
            }
        }
    }

}
