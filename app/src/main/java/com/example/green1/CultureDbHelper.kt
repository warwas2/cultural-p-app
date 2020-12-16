package com.example.green1

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import com.koushikdutta.ion.Ion
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL


class CultureDbHelper(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    var cultureSet: ArrayList<CultureProperties> = ArrayList()
    lateinit var progressDialog: ProgressDialog

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "cultureDB.db"
        val SQL_CREATE_ENTRIES = "CREATE TABLE " + cultureEntry.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY," +
                cultureEntry.COLUMN_NAME_CTYPE + "  TEXT," +
                cultureEntry.COLUMN_NAME_NAME + "  TEXT," +
                cultureEntry.COLUMN_NAME_CCBAKDCD + "  TEXT," +
                cultureEntry.COLUMN_NAME_CCBAASNO + "  TEXT," +
                cultureEntry.COLUMN_NAME_CCBACTCD + "  TEXT," +
                cultureEntry.COLUMN_NAME_LATITUDE + "  TEXT," +
                cultureEntry.COLUMN_NAME_LONGITUDE + "  TEXT," +
                cultureEntry.COLUMN_NAME_LOCATION + "  TEXT," +
                cultureEntry.COLUMN_NAME_TIME + "  TEXT," +
                cultureEntry.COLUMN_NAME_IMGURL + "  TEXT," +
                cultureEntry.COLUMN_NAME_INSTRUCTION + "  TEXT," +
                cultureEntry.COLUMN_NAME_VOICEURL + "  TEXT," +
                cultureEntry.COLUMN_NAME_VISIT + "  INTEGER," +
                cultureEntry.COLUMN_NAME_PREFER + "  INTEGER," +
                cultureEntry.COLUMN_NAME_ISEMPTY + " INTEGER )"
    }
    class cultureEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "CultureProperties"
            val COLUMN_NAME_CTYPE = "ctype"
            val COLUMN_NAME_NAME = "name"
            val COLUMN_NAME_CCBAKDCD = "ccbakdcd"
            val COLUMN_NAME_CCBAASNO = "ccbaAsno"
            val COLUMN_NAME_CCBACTCD = "ccbaCtcd"
            val COLUMN_NAME_LATITUDE = "latitude"
            val COLUMN_NAME_LONGITUDE = "longitude"
            val COLUMN_NAME_LOCATION = "location"
            val COLUMN_NAME_TIME = "time"
            val COLUMN_NAME_IMGURL = "imgUrl"
            val COLUMN_NAME_INSTRUCTION = "instruction"
            val COLUMN_NAME_VOICEURL = "voiceUrl"
            val COLUMN_NAME_VISIT = "visit"
            val COLUMN_NAME_PREFER="prefer"
            val COLUMN_NAME_ISEMPTY="isempty"
        }
    }
    fun saveCulture() {
        val db = writableDatabase
        for(i in 0..(cultureSet.size-1)) {
            val values = ContentValues()
            values.put(cultureEntry.COLUMN_NAME_CTYPE, cultureSet.get(i).ctype)
            values.put(cultureEntry.COLUMN_NAME_NAME, cultureSet.get(i).name)
            values.put(cultureEntry.COLUMN_NAME_CCBAKDCD, cultureSet.get(i).ccbaKdcd)
            values.put(cultureEntry.COLUMN_NAME_CCBAASNO, cultureSet.get(i).ccbaAsno)
            values.put(cultureEntry.COLUMN_NAME_CCBACTCD, cultureSet.get(i).ccbaCtcd)
            values.put(cultureEntry.COLUMN_NAME_LATITUDE, cultureSet.get(i).latitude)
            values.put(cultureEntry.COLUMN_NAME_LONGITUDE, cultureSet.get(i).longitude)
            if(!cultureSet.get(i).isEmpty) {
                values.put(cultureEntry.COLUMN_NAME_LOCATION, cultureSet.get(i).location)
                values.put(cultureEntry.COLUMN_NAME_TIME, cultureSet.get(i).time)
                values.put(cultureEntry.COLUMN_NAME_IMGURL, cultureSet.get(i).imgUrl)
                values.put(cultureEntry.COLUMN_NAME_INSTRUCTION, cultureSet.get(i).instruction)
                values.put(cultureEntry.COLUMN_NAME_VOICEURL, cultureSet.get(i).voiceUrl)
                values.put(cultureEntry.COLUMN_NAME_VISIT, cultureSet.get(i).visit)
                values.put(cultureEntry.COLUMN_NAME_PREFER, cultureSet.get(i).prefer)
                //Log.e("문화재개수 ", i.toString())
            }else{
                values.put(cultureEntry.COLUMN_NAME_LOCATION, "")
                values.put(cultureEntry.COLUMN_NAME_TIME, "")
                values.put(cultureEntry.COLUMN_NAME_IMGURL,"")
                values.put(cultureEntry.COLUMN_NAME_INSTRUCTION, "")
                values.put(cultureEntry.COLUMN_NAME_VOICEURL, "")
                values.put(cultureEntry.COLUMN_NAME_VISIT,false)
                values.put(cultureEntry.COLUMN_NAME_PREFER,false)
            }
            values.put(cultureEntry.COLUMN_NAME_ISEMPTY, cultureSet.get(i).isEmpty)
            db.insert(cultureEntry.TABLE_NAME, null, values)
        }
        progressDialog.dismiss()

        //  loadingEnd()
    }
    fun loading() {
        //로딩
        android.os.Handler().postDelayed(
            {
                progressDialog = ProgressDialog(context)
                progressDialog.setIndeterminate(true)
                progressDialog.setMessage("잠시만 기다려 주세요")
                progressDialog.show()
                progressDialog.setCancelable(false)
                progressDialog.setTitle("문화재 정보 불러오기")
            },0
        )
        Log.e("로딩시작","ㅋ")
    }

    fun loadingEnd() {
        android.os.Handler().postDelayed(
            { progressDialog!!.dismiss() }, 0
        )

    }
    fun openXml(result: InputStream){
        var factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware=true
        val xpp = factory.newPullParser()
        xpp.setInput(result, "utf-8")
        var eventType = xpp.eventType

        var isItem = false
        var dataSet = false
        var status = 0
        var i=0
        var item= CultureProperties()
        while (eventType != XmlPullParser.END_DOCUMENT) {


            when(eventType){
                XmlPullParser.START_DOCUMENT->{}

                XmlPullParser.START_TAG -> {
                    val tagname = xpp.name
                    if(isItem){
                        if(tagname.equals("ccmaName")||tagname.equals("ccbaMnm1")||tagname.equals("ccbaKdcd")||
                            tagname.equals("ccbaAsno")||tagname.equals("ccbaCtcd") ||tagname.equals("latitude")
                            ||tagname.equals("longitude")) {
                            dataSet = true
                            when (tagname) {
                                "ccmaName"->{
                                    status=1
                                }
                                "ccbaMnm1"->{
                                    status=2
                                }
                                "ccbaKdcd"->{
                                    status=3
                                }
                                "ccbaAsno"->{
                                    status=4
                                }
                                "ccbaCtcd"->{
                                    status=5
                                }
                                "latitude"->{
                                    status=6
                                }
                                "longitude"->{
                                    status=7
                                }
                            }
                        }
                    }
                    if (tagname.equals("item")) {
                        isItem = true
                        status=0
                        item= CultureProperties()
                    }
                }

                XmlPullParser.TEXT->{
                    if ( dataSet ) {
                        when(status){
                            1->{
                                item!!.ctype =xpp.text
                            }
                            2->{
                                item!!.name =xpp.text
                            }
                            3->{
                                item!!.ccbaKdcd =xpp.text
                            }
                            4->{
                                item!!.ccbaAsno=xpp.text
                            }
                            5->{
                                item!!.ccbaCtcd=xpp.text
                            }
                            6->{
                                item!!.latitude=xpp.text.trim()
                            }
                            7->{
                                item!!.longitude=xpp.text.trim()
                            }

                        }

                        dataSet = false ;
                    }
                }
                XmlPullParser.END_TAG->if (xpp.name.equals("item") && item != null){
                    isItem=false
                    status=0
                    cultureSet.add(item)
                }


            }
            eventType = xpp.next()
        }
        loadingEnd()
        // saveCulture()
    }
    fun readFile(){
        Ion.with(context).load( "http://www.cha.go.kr/cha/SearchKindOpenapiList.do?pageUnit=500&pageIndex=1")//pageUnit->data개수
            .asInputStream()
            .setCallback { e, result ->
                if(result!=null) {
                    openXml(result)
                    /*     Log.e("링크","http://www.cha.go.kr/cha/SearchKindOpenapiDt.do?ccbaKdcd=" + cultureSet.get(1).ccbaKdcd + "&ccbaAsno=" +
                                 cultureSet.get(47).ccbaAsno + "&ccbaCtcd=" + cultureSet.get(47).ccbaCtcd)
     */

                }
                else
                    Toast.makeText(context, "문화재를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }


    }

    fun parsingXml(){
        Thread(Runnable {
            for(i in cultureSet.indices) {
                try{
                    var url = URL(
                        "http://www.cha.go.kr/cha/SearchKindOpenapiDt.do?ccbaKdcd=" + cultureSet.get(i).ccbaKdcd + "&ccbaAsno=" +
                                cultureSet.get(i).ccbaAsno + "&ccbaCtcd=" + cultureSet.get(i).ccbaCtcd
                    )
                    var url2 =
                        URL(
                            "http://www.cha.go.kr/cha/SearchVoiceOpenapi.do?ccbaKdcd=" + cultureSet.get(i).ccbaKdcd + "&ccbaAsno=" +
                                    cultureSet.get(i).ccbaAsno + "&ccbaCtcd=" + cultureSet.get(i).ccbaCtcd + "&ccbaGbn=kr"
                        )
                    val inputStream:InputStream = url.openStream()
                    var factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
                    var parser = factory.newPullParser()
                    parser.setInput(InputStreamReader(inputStream, "UTF-8"))
                    var eventType = parser.eventType

                    var value1 = false
                    var value2 = false
                    var value3 = false
                    var value4 = false


                    while (eventType !== XmlPullParser.END_DOCUMENT) {
                        when (eventType) {
                            XmlPullParser.START_DOCUMENT -> {

                            }
                            XmlPullParser.END_DOCUMENT -> {

                            }
                            XmlPullParser.START_TAG -> {
                                if (parser.name.equals("item")) {

                                }
                                if (parser.name.equals("ccbaLcad")) value1 = true
                                if (parser.name.equals("ccceName")) value2 = true
                                if (parser.name.equals("imageUrl")) value3 = true
                                if (parser.name.equals("content")) value4 = true


                            }
                            XmlPullParser.TEXT -> if (value1) {
                                val text=parser.text.split("//")
                                cultureSet.get(i)!!.location = text[0].trim()
                                value1 = false
                            } else if (value2) {
                                cultureSet.get(i)!!.time = parser.text
                                value2 = false
                            } else if (value3) {
                                cultureSet.get(i)!!.imgUrl = parser.text
                                value3 = false
                            } else if (value4) {
                                cultureSet.get(i)!!.instruction= parser.text
                                value4 = false
                            }

                        }
                        eventType = parser.next()
                    }
                    var imageUrl = URL(cultureSet.get(i).imgUrl)
                    val is1 = imageUrl.openStream()
                    // val bm = BitmapFactory.decodeStream(is1)
                    var audio: Boolean = false
                    val inputStream2:InputStream = url2.openStream()
                    factory = XmlPullParserFactory.newInstance()
                    parser = factory.newPullParser()
                    parser.setInput(InputStreamReader(inputStream2, "UTF-8"))
                    eventType = parser.eventType
                    while (eventType !== XmlPullParser.END_DOCUMENT) {
                        when (eventType) {
                            XmlPullParser.START_DOCUMENT -> {

                            }
                            XmlPullParser.END_DOCUMENT -> {

                            }
                            XmlPullParser.START_TAG -> {

                                if (parser.name.equals("voiceUrl")) audio = true

                                //위도 경도도 받을 수 있음
                            }
                            XmlPullParser.TEXT -> if (audio) {
                                cultureSet.get(i)!!.voiceUrl = parser.text.trim()
                                audio = false
                            }

                        }
                        eventType = parser.next()
                    }
                }catch (e:Exception){
                    cultureSet.get(i).isEmpty=true

                }




                Log.e("문화재개수2 ",  cultureSet.get(i).toString())
                Log.e("문화재개수3 ",  i.toString())
            }
            saveCulture()

        }).start()

    }
    fun loadCultureList(): ArrayList<CultureProperties> {
        val cultureArrayList = ArrayList<CultureProperties>()
        val db = readableDatabase

        val projection = arrayOf(BaseColumns._ID,
            cultureEntry.COLUMN_NAME_CTYPE,
            cultureEntry.COLUMN_NAME_NAME,
            cultureEntry.COLUMN_NAME_CCBAKDCD,
            cultureEntry.COLUMN_NAME_CCBAASNO,
            cultureEntry.COLUMN_NAME_CCBACTCD,
            cultureEntry.COLUMN_NAME_LATITUDE,
            cultureEntry.COLUMN_NAME_LONGITUDE,
            cultureEntry.COLUMN_NAME_LOCATION,
            cultureEntry.COLUMN_NAME_TIME,
            cultureEntry.COLUMN_NAME_IMGURL,
            cultureEntry.COLUMN_NAME_INSTRUCTION,
            cultureEntry.COLUMN_NAME_VOICEURL,
            cultureEntry.COLUMN_NAME_VISIT,
            cultureEntry.COLUMN_NAME_PREFER,
            cultureEntry.COLUMN_NAME_ISEMPTY
        )


        val cursor = db.query(cultureEntry.TABLE_NAME,
            projection, null, null, null, null, null)


        while (cursor.moveToNext()) {
            val culture = CultureProperties(
                cursor.getString(cursor.getColumnIndex(cultureEntry.COLUMN_NAME_CTYPE)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_NAME)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_CCBAKDCD)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_CCBAASNO)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_CCBACTCD)),
                cursor.getString(cursor.getColumnIndex(  cultureEntry.COLUMN_NAME_LATITUDE)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_LOCATION)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_TIME)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_IMGURL)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_INSTRUCTION)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_VOICEURL)),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_VISIT)).equals("1"),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_PREFER)).equals("1"),
                cursor.getString(cursor.getColumnIndex( cultureEntry.COLUMN_NAME_ISEMPTY)).equals("1")
            )
            cultureArrayList.add(culture)
            Log.e("불러오기",culture.toString())
        }
        Log.e("불러오기",cultureArrayList.size.toString())
        return cultureArrayList
    }
    fun updatePrefer(cul:CultureProperties){

        val db=writableDatabase

        val values=ContentValues()
        values.put(cultureEntry.COLUMN_NAME_PREFER,cul.prefer)

        db.update(cultureEntry.TABLE_NAME,values,cultureEntry.COLUMN_NAME_NAME+"=?", arrayOf(cul.name))

    }
    fun updateVisit(name:String){

        val db=writableDatabase

        val values=ContentValues()
        values.put(cultureEntry.COLUMN_NAME_VISIT,1)

        db.update(cultureEntry.TABLE_NAME,values,cultureEntry.COLUMN_NAME_NAME+"=?", arrayOf(name))

    }
    override fun onCreate(p0: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.e("시작","시작")
        p0!!.execSQL(SQL_CREATE_ENTRIES)
        //   readFile()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}