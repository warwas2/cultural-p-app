package com.example.green1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.example.insta.GridAdapter
import kotlinx.android.synthetic.main.activity_main.*
import net.daum.mf.map.api.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.io.Serializable
import java.io.StringReader
import java.util.*

class MainActivity : AppCompatActivity(), MapView.CurrentLocationEventListener,
    MapReverseGeoCoder.ReverseGeoCodingResultListener, MapView.POIItemEventListener, CalloutBalloonAdapter,MapView.MapViewEventListener, Serializable {
    lateinit var timemap: MutableMap<String, Int>
    lateinit var timelist: ArrayList<timeclass>
    lateinit var adapter: timeadapter
    lateinit var row3Adapter: row3adapter
    lateinit var searchAdapter: searchadapter
    var searchArray: ArrayList<CultureProperties> = ArrayList()
    val cultureDbHelper: CultureDbHelper = CultureDbHelper(this)
    var culture: ArrayList<CultureProperties> = ArrayList()
    var saveArray: ArrayList<CultureProperties> = ArrayList()
    var checkType: Int = 0
    var intarray :IntArray = intArrayOf()
    var mMapView: MapView? = null
    var mycurrentloc: MapPoint? =MapPoint.mapPointWithGeoCoord( 37.53498077392578,127.07890319824219)
    var m_cul: ArrayList<CultureProperties> = ArrayList()



    private val ACCESS_REQUEST =100

    var array: MutableList<String> = mutableListOf("고건준", "천승범", "유종우", "김재원")

    internal var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    companion object {

        private val LOG_TAG = "MainActivity"


        private val GPS_ENABLE_REQUEST_CODE = 2001
        private val PERMISSIONS_REQUEST_CODE = 100
    }


    val data31 = arrayListOf(
        "서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "전국일원"
    )
    val data32 = arrayListOf(
        "국보",
        "보물",
        "사적",
        "사적및 명승",
        "천연기념물",
        "국가무형문화재",
        "국가민속문화재",
        "시도유형문화재",
        "시도무형문화재",
        "시도기념물",
        "시도민속문화재",
        "문화재자료",
        "등록문화재",
        "이북5도 무형문화재"
    )

    val imgData31:ArrayList<Int> = arrayListOf(R.drawable.seoul, R.drawable.busan,R.drawable.daegu,R.drawable.incheon,R.drawable.gwangju,R.drawable.daejeon,R.drawable.ulsan,R.drawable.sejong,
        R.drawable.gyeonggi, R.drawable.gangwon, R.drawable.chungbuk, R.drawable.chungnam, R.drawable.jeonbuk, R.drawable.jeonnam, R.drawable.gyeongbuk, R.drawable.gyeongnam,
        R.drawable.jeju,R.drawable.others)
    val imgData32:ArrayList<Int> = arrayListOf(R.drawable.type1, R.drawable.type2,R.drawable.type3,R.drawable.type4,R.drawable.type5,R.drawable.type6,R.drawable.type7,R.drawable.type8,
        R.drawable.type9,R.drawable.type10,R.drawable.type11,R.drawable.type12,R.drawable.type13,R.drawable.type14)
    val myDbHelper: DBHelper = DBHelper(this)
    lateinit var layoutManager : GridLayoutManager
    lateinit var instaadapter0: GridAdapter
    lateinit var instaadapter1: GridAdapter
    var imgArry: ArrayList<MyClass> = ArrayList()
    var selected=0
    var gridNum = 0
    var visited = 0;
    val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                visited=0
                main1.visibility = VISIBLE
                main2.visibility = INVISIBLE
                main3.visibility = INVISIBLE
                main4.visibility = INVISIBLE
                for(i in culture){
                    if(i.visit)
                        visited++
                }
                visit.text=visited.toString()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                culture = cultureDbHelper.loadCultureList()
                main1.visibility = INVISIBLE
                main2.visibility = VISIBLE
                main3.visibility = INVISIBLE
                main4.visibility = INVISIBLE
                linear311.visibility=VISIBLE

                //천승범

                mMapView = findViewById<MapView>(com.example.green1.R.id.map_view)
                pinpoint(culture)
                var sadapter = slideadapter(this,R.layout.slidelayout,m_cul,cultureDbHelper)
                listView.adapter = sadapter

                mMapView!!.setMapViewEventListener(this)
                mMapView!!.setPOIItemEventListener(this)
                var mylocbtn = findViewById<Button>(R.id.myloc)

                mylocbtn.setOnClickListener{
                    mMapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
                    mMapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                    mMapView!!.setZoomLevel(2,true)

                    var dist:FloatArray= FloatArray(1)
                   m_cul.clear()

                    var minium :DoubleArray = DoubleArray(10)
                    var min : Double = 50000.toDouble()
                    var i = 0
                    culture.forEach {
                            i++
                        if (it.longitude.isNotEmpty() && it.latitude.isNotEmpty()) {

                            var dist:FloatArray= FloatArray(1)
                            Location.distanceBetween(it.latitude.toDouble(), it.longitude.toDouble(),mycurrentloc!!.mapPointGeoCoord.latitude,mycurrentloc!!.mapPointGeoCoord.longitude,dist)

                            if(min>dist[0]){
                                m_cul.add(it)
                                Log.e("cul_array",it.name)
                            }
                        }
                    }

                    pinpoint(culture)

                }







                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_culture -> {
                culture = cultureDbHelper.loadCultureList()
                main1.visibility = INVISIBLE
                main2.visibility = INVISIBLE
                main3.visibility = VISIBLE
                frame31.visibility = VISIBLE
                frame32.visibility = INVISIBLE
                main4.visibility = INVISIBLE
                linear311.visibility=VISIBLE
                val layoutManager =GridLayoutManager(this, 2)
                recycler31.layoutManager = layoutManager
                row3Adapter = row3adapter(data31,imgData31)

                recycler31.adapter = row3Adapter

                row3Adapter.itemClickListener = object : row3adapter.OnItemClickListener {
                    override fun OnItemClick(holder: row3adapter.ViewHolder, view: View, data: String, position: Int) {

                        val location: String = data31.get(position)
                        searchArray.clear()
                        when (location) {
                            "전국일원" -> {
                                var isContain: Boolean
                                for (cul in culture) {
                                    isContain = false
                                    for (loc in data31) {
                                        if (cul.location.contains(loc)) {
                                            isContain = true
                                            break
                                        }

                                    }
                                    if (cul.location.contains("충청남도") || cul.location.contains("충청북도") ||
                                        cul.location.contains("전라북도") || cul.location.contains("전라남도") ||
                                        cul.location.contains("경상남도") || cul.location.contains("경상북도")
                                    )
                                        isContain = true
                                    if (!isContain) searchArray.add(cul)
                                }
                            }
                            "충남" -> {
                                for (cul in culture) {
                                    if (cul.location.contains(location) || cul.location.contains("충청남도")) {
                                        searchArray.add(cul)
                                    }
                                }
                            }
                            "충북" -> {
                                for (cul in culture) {
                                    if (cul.location.contains(location) || cul.location.contains("충청북도")) {
                                        searchArray.add(cul)
                                    }
                                }
                            }
                            "전북" -> {
                                for (cul in culture) {
                                    if (cul.location.contains(location) || cul.location.contains("전라북도")) {
                                        searchArray.add(cul)
                                    }
                                }
                            }
                            "전남" -> {
                                for (cul in culture) {
                                    if (cul.location.contains(location) || cul.location.contains("전라남도")) {
                                        searchArray.add(cul)
                                    }
                                }
                            }
                            "경북" -> {
                                for (cul in culture) {
                                    if (cul.location.contains(location) || cul.location.contains("경상북도")) {
                                        searchArray.add(cul)
                                    }
                                }
                            }
                            "경남" -> {
                                for (cul in culture) {
                                    if (cul.location.contains(location) || cul.location.contains("경상남도")) {
                                        searchArray.add(cul)
                                    }
                                }
                            }
                            "세종" -> {
                                for (cul in culture) {
                                    if (cul.location.contains(location + "시")) {
                                        searchArray.add(cul)
                                    }
                                }
                            }
                            else -> {
                                for (cul in culture) {
                                    if (cul.location.contains(location)) {
                                        searchArray.add(cul)
                                    }
                                }
                            }
                        }

                        searchAdapter = searchadapter(searchArray, cultureDbHelper)
                        searchAdapter.itemClickListener = object : searchadapter.OnItemClickListener {
                            override fun OnItemClick(
                                holder: searchadapter.ViewHolder,
                                view: View,
                                data: CultureProperties,
                                position: Int
                            ) {
                                val intent = Intent(applicationContext, IntroActivity::class.java)
                                intent.putExtra("culture", data)
                                startActivity(intent)
                                // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }


                        }
                        val layoutManager = LinearLayoutManager(applicationContext,LinearLayout.VERTICAL,false)
                        recycler31.layoutManager=layoutManager
                        recycler31.adapter = searchAdapter

                        //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                }

                button31.setOnClickListener {
                    frame31.visibility = VISIBLE
                    frame32.visibility = INVISIBLE
                    row3Adapter = row3adapter(data31,imgData31)
                    val layoutManager =GridLayoutManager(this, 2)
                    recycler31.layoutManager = layoutManager
                    recycler31.adapter = row3Adapter
                    row3Adapter.itemClickListener = object : row3adapter.OnItemClickListener {
                        override fun OnItemClick(
                            holder: row3adapter.ViewHolder,
                            view: View,
                            data: String,
                            position: Int
                        ) {
                            val location: String = data31.get(position)
                            searchArray.clear()
                            when (location) {
                                "전국일원" -> {
                                    var isContain: Boolean
                                    for (cul in culture) {
                                        isContain = false
                                        for (loc in data31) {
                                            if (cul.location.contains(loc)) {
                                                isContain = true
                                                break
                                            }

                                        }
                                        if (cul.location.contains("충청남도") || cul.location.contains("충청북도") ||
                                            cul.location.contains("전라북도") || cul.location.contains("전라남도") ||
                                            cul.location.contains("경상남도") || cul.location.contains("경상북도")
                                        )
                                            isContain = true
                                        if (!isContain) searchArray.add(cul)
                                    }
                                }
                                "충남" -> {
                                    for (cul in culture) {
                                        if (cul.location.contains(location) || cul.location.contains("충청남도")) {
                                            searchArray.add(cul)
                                        }
                                    }
                                }
                                "충북" -> {
                                    for (cul in culture) {
                                        if (cul.location.contains(location) || cul.location.contains("충청북도")) {
                                            searchArray.add(cul)
                                        }
                                    }
                                }
                                "전북" -> {
                                    for (cul in culture) {
                                        if (cul.location.contains(location) || cul.location.contains("전라북도")) {
                                            searchArray.add(cul)
                                        }
                                    }
                                }
                                "전남" -> {
                                    for (cul in culture) {
                                        if (cul.location.contains(location) || cul.location.contains("전라남도")) {
                                            searchArray.add(cul)
                                        }
                                    }
                                }
                                "경북" -> {
                                    for (cul in culture) {
                                        if (cul.location.contains(location) || cul.location.contains("경상북도")) {
                                            searchArray.add(cul)
                                        }
                                    }
                                }
                                "경남" -> {
                                    for (cul in culture) {
                                        if (cul.location.contains(location) || cul.location.contains("경상남도")) {
                                            searchArray.add(cul)
                                        }
                                    }
                                }
                                "세종" -> {
                                    for (cul in culture) {
                                        if (cul.location.contains(location + "시")) {
                                            searchArray.add(cul)
                                        }
                                    }
                                }
                                else -> {
                                    for (cul in culture) {
                                        if (cul.location.contains(location)) {
                                            searchArray.add(cul)
                                        }
                                    }
                                }
                            }
                            searchAdapter = searchadapter(searchArray, cultureDbHelper)
                            searchAdapter.itemClickListener = object : searchadapter.OnItemClickListener {
                                override fun OnItemClick(
                                    holder: searchadapter.ViewHolder,
                                    view: View,
                                    data: CultureProperties,
                                    position: Int
                                ) {
                                    val intent = Intent(applicationContext, IntroActivity::class.java)
                                    intent.putExtra("culture", data)
                                    startActivity(intent)
                                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }


                            }
                            val layoutManager = LinearLayoutManager(applicationContext,LinearLayout.VERTICAL,false)
                            recycler31.layoutManager=layoutManager
                            recycler31.adapter = searchAdapter
                            //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    }

                }
                button32.setOnClickListener {
                    frame31.visibility = VISIBLE
                    frame32.visibility = INVISIBLE
                    row3Adapter = row3adapter(data32,imgData32)//이거 바꿔야함
                    recycler31.adapter = row3Adapter
                    val layoutManager =GridLayoutManager(this, 2)
                    recycler31.layoutManager = layoutManager
                    row3Adapter.itemClickListener = object : row3adapter.OnItemClickListener {
                        override fun OnItemClick(
                            holder: row3adapter.ViewHolder,
                            view: View,
                            data: String,
                            position: Int
                        ) {
                            val type: String = data32.get(position)
                            searchArray.clear()
                            for (cul in culture) {
                                if (cul.ctype.contains(type)) {
                                    searchArray.add(cul)
                                }
                            }
                            searchAdapter = searchadapter(searchArray, cultureDbHelper)
                            searchAdapter.itemClickListener = object : searchadapter.OnItemClickListener {
                                override fun OnItemClick(
                                    holder: searchadapter.ViewHolder,
                                    view: View,
                                    data: CultureProperties,
                                    position: Int
                                ) {
                                    val intent = Intent(applicationContext, IntroActivity::class.java)
                                    intent.putExtra("culture", data)
                                    startActivity(intent)
                                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }


                            }
                            val layoutManager = LinearLayoutManager(applicationContext,LinearLayout.VERTICAL,false)
                            recycler31.layoutManager=layoutManager
                            recycler31.adapter = searchAdapter
                            //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    }


                }
                button33.setOnClickListener {
                    frame32.visibility = VISIBLE
                    frame31.visibility = INVISIBLE
                    val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recycler32.layoutManager = layoutManager
                    searchArray.clear()
                    saveArray.clear()
                    for (cul in culture) {
                        if (cul.prefer)
                            searchArray.add(cul)
                        else
                            saveArray.add(cul)
                    }
                    searchArray.addAll(saveArray)
                    //searchArray.addAll(culture)
                    searchAdapter = searchadapter(searchArray, cultureDbHelper)
                    saveArray.clear()
                    saveArray.addAll(culture)
                    searchAdapter.itemClickListener = object : searchadapter.OnItemClickListener {
                        override fun OnItemClick(
                            holder: searchadapter.ViewHolder,
                            view: View,
                            data: CultureProperties,
                            position: Int
                        ) {
                            val intent = Intent(applicationContext, IntroActivity::class.java)
                            intent.putExtra("culture", data)
                            startActivity(intent)
                            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    }
                    recycler32.adapter = searchAdapter
                    radioGroup1.setOnCheckedChangeListener { radioGroup, i ->
                        when (i) {
                            R.id.radioButton1_1 -> {
                                checkType = 0
                                filter(editText.text.toString())
                                adapter.notifyDataSetChanged()
                            }
                            R.id.radioButton1_2 -> {
                                checkType = 1
                                filter(editText.text.toString())
                                adapter.notifyDataSetChanged()
                            }

                        }
                    }
                    editText.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(edit: Editable) {
                            // TODO : item filtering
                            val text = editText.getText().toString()
                            filter(text)

                        }

                        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                        }

                        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                        }
                    })
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_time -> {
                main1.visibility = INVISIBLE
                main2.visibility = INVISIBLE
                main3.visibility = INVISIBLE
                main4.visibility = VISIBLE
                main41.visibility = VISIBLE
                main42.visibility = INVISIBLE
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLocation()
        changeGrid.setOnClickListener {
            initAdapter()
            /*if(gridNum ==0){
                gridNum = 1
            }
            else{
                gridNum = 0
            }*/
        }
        // textView.movementMethod = ScrollingMovementMethod()

        button3.setOnClickListener {
            first.visibility = INVISIBLE
                        second.visibility = VISIBLE
                main1.visibility=VISIBLE
                val pref = getSharedPreferences("isFirst2", Activity.MODE_PRIVATE)
                val first = pref.getBoolean("isFirst2", false)
                if (first == false) {
                    val editor = pref.edit()
                    editor.putBoolean("isFirst2", true)
                    editor.commit()
                    Log.e("최초", first.toString())
                    cultureDbHelper.loading()
                    cultureDbHelper.parsingXml()
                initLocation();
                //앱 최초 실행시 하고 싶은 작업
            }
            visited=0
            for(i in culture){
                if(i.visit)
                    visited++
            }
            visit.text=visited.toString()
        }
        //////////////////////////////////////


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId=R.id.navigation_home



        /////////////////////////////////////////


        init4() //처음 앱 킬때 문화재 정보 받아오기.

        visited = imgArry.size
        loadProfile()

        if (intent.hasExtra("sss")) {
            var k = intent.getSerializableExtra("sss") as MyClass
            updateMyData(k)
        }
        loadMyData()


        layoutManager = GridLayoutManager(this, 2)
        layoutManager.isSmoothScrollbarEnabled = true
        rView.layoutManager = layoutManager
        //rView.adapter = instaadapter
        initAdapter()
        //Toast.makeText(applicationContext, "${imgArry[0].address}", Toast.LENGTH_SHORT).show()


        testBtn.setOnClickListener {
            var sendIntent = Intent(this, Home::class.java)
            sendIntent.putExtra("flag", 1)
            startActivityForResult(sendIntent, 123)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO
                Log.e("Noa","save")
                var result = data!!.getSerializableExtra("result") as MyClass
                Log.e("Noa","${result.title}")
                saveMyData(result)
                Log.e("Noa","save completed")
                if(gridNum==0){
                    gridNum = 1
                }
                else{
                    gridNum = 0
                }
                initAdapter()
            }
        }
        else if(requestCode == 456){
            if (resultCode == Activity.RESULT_OK) {
                // TODO
                Log.e("Noa","update")
                var update = data!!.getSerializableExtra("update") as MyClass
                updateMyData(update)

                if(gridNum==0){
                    gridNum = 1
                }
                else{
                    gridNum = 0
                }
                initAdapter()
            }
        }
    }

    fun loadProfile(){
        if(visited < 1){
            //level1
            profileIMG.setImageResource(R.drawable.bronze)
        }
        else if(visited >= 1 && visited < 2){
            //level2
            profileIMG.setImageResource(R.drawable.silver)
        }
        else if(visited >= 2 && visited < 3){
            //level3
            profileIMG.setImageResource(R.drawable.gold)
        }
        else if(visited >= 3 && visited < 4){
            //level4
            profileIMG.setImageResource(R.drawable.master)
        }
        else{
            //level5
            profileIMG.setImageResource(R.drawable.challenger)
        }
    }

    fun updateMyData(data: MyClass) {
        imgArry[selected] = data
        myDbHelper.updateDiary(data)
        visited = imgArry.size
        loadProfile()
    }

    fun loadMyData() {
        imgArry = myDbHelper.loadDiaryList()
        visited = imgArry.size
        loadProfile()
        post.text = imgArry.size.toString()
    }

    fun addMyData(data: MyClass) {
        imgArry.add(data)
        visited = imgArry.size
        loadProfile()
        post.text = imgArry.size.toString()
    }

    fun saveMyData(data: MyClass) {
        addMyData(data)
        myDbHelper.saveDiary(data)
        //adapter.notifyDataSetChanged()
        visited = imgArry.size
        loadProfile()
    }

    fun initAdapter(){
        instaadapter0 = GridAdapter(imgArry,0)
        instaadapter1 = GridAdapter(imgArry,1)
        if(gridNum == 0) {
            changeGrid.setImageResource(R.drawable.feed)
            layoutManager.spanCount = 2
            rView.adapter = instaadapter0
            instaadapter0.itemClickListener = object : GridAdapter.OnItemClickListener {
                override fun OnItemClick(holder: GridAdapter.ViewHolder, view: View, data: MyClass, position: Int) {
                    //TODO("not implemented")
                    // To change body of created functions use File | Settings | File Templates.
                    selected = position
                    var intent = Intent(holder.iView.context, Home::class.java)
                    intent.putExtra("flag", 2)
                    intent.putExtra("immg", data)
                    startActivityForResult(intent, 456)
                }
            }
            gridNum = 1
        }
        else{
            changeGrid.setImageResource(R.drawable.cross)
            layoutManager.spanCount = 1
            rView.adapter = instaadapter1
            instaadapter1.itemClickListener = object : GridAdapter.OnItemClickListener {
                override fun OnItemClick(holder: GridAdapter.ViewHolder, view: View, data: MyClass, position: Int) {
                    //TODO("not implemented")
                    // To change body of created functions use File | Settings | File Templates.
                    selected = position
                    var intent = Intent(holder.iView.context, Home::class.java)
                    intent.putExtra("flag", 2)
                    intent.putExtra("immg", data)
                    startActivityForResult(intent, 456)
                }
            }
            gridNum = 0
        }
    }

    fun filter(text: String) {
        searchArray.clear()
        if (text.length == 0) {
            //searchArray.addAll(saveArray)
            searchArray.clear()
            saveArray.clear()
            for (cul in culture) {
                if (cul.prefer)
                    searchArray.add(cul)
                else
                    saveArray.add(cul)
            }
            searchArray.addAll(saveArray)
        } else {
            if (checkType == 0) {
                for (cul in saveArray) {
                    if (cul.name.contains(text)) {
                        searchArray.add(cul)
                    }
                }
            } else {
                for (cul in saveArray) {
                    if (cul.location.contains(text)) {
                        searchArray.add(cul)
                    }
                }
            }
        }
        searchAdapter.notifyDataSetChanged()
    }

    fun init4() {
        val pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE)
        val first = pref.getBoolean("isFirst", false)
        if (first == false) {
            val editor = pref.edit()
            editor.putBoolean("isFirst", true)
            editor.commit()
            Log.e("최초", first.toString())
            cultureDbHelper.readFile()
            cultureDbHelper.loading()

            //앱 최초 실행시 하고 싶은 작업
        } else {
            culture = cultureDbHelper.loadCultureList()
        }
        timelist = arrayListOf()
        timemap = mutableMapOf()
        var am = resources.assets
        var iss: InputStream? = null
        try {
            iss = am.open("file.txt")
            val inputstr = iss.bufferedReader().use {
                it.readText()
            }
            //textView.append(inputstr)
            parsing4(inputstr)
            adapter = timeadapter(this, R.layout.timelayout, timelist)
            list41.adapter = adapter
            button4.setOnClickListener {
                val input = edittext4.text.toString()
                val inputtimeclass = timemap[input]
                if (inputtimeclass == null)
                    Toast.makeText(this, "없음", Toast.LENGTH_SHORT).show()
                else
                    showing4(timelist[inputtimeclass!!.toInt()])
            }
            list41.setOnItemClickListener { parent, view, position, id ->
                val w = parent.getItemAtPosition(position) as timeclass
                showing4(w)
            }
        } catch (e: Exception) {
            //textView.text=e.toString()
        }
        if (iss != null) {
            try {
                iss.close()
            } catch (e: Exception) {
                //  textView.append(e.toString())
            }
        }
//        val i = Intent(applicationContext,FirstActivity::class.java)
//        startActivity(i)
    }


    fun parsing4(result: String) {
        var index = 0
        var factory: XmlPullParserFactory
        try {
            factory = XmlPullParserFactory.newInstance()
            // factory.isNamespaceAware=true
            var xpp = factory.newPullParser()
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            xpp.setInput(StringReader(result))
            var eventype = xpp.eventType
            var bset = false
            var tagname = ""

            var name: String = ""
            var start1: String = ""
            var end1: String = ""
            var start2: String = ""
            var end2: String = ""
            var tel = ""
            var fee1 = ""
            var fee2 = ""
            var fee3 = ""
            var info = ""
            var homepage = ""

            while (eventype != XmlPullParser.END_DOCUMENT) {


                if (eventype == XmlPullParser.START_TAG) {
                    tagname = xpp.name
                    if (tagname == "평일관람시작시각" || tagname == "시설명" || tagname == "평일관람종료시각" || tagname == "공휴일관람시작시각" || tagname == "운영기관전화번호" || tagname == "공휴일관람종료시각" || tagname == "운영홈페이지" || tagname == "어른관람료" || tagname == "청소년관람료" || tagname == "어린이관람료" || tagname == "박물관미술관소개")
                        bset = true
                } else if (eventype == XmlPullParser.TEXT) {
                    if (bset) {
                        var data = xpp.text

                        if (tagname == "시설명")
                            name = data
                        else if (tagname == "운영기관전화번호")
                            tel = data
                        else if (tagname == "운영홈페이지")
                            homepage = data
                        else if (tagname == "평일관람시작시각")
                            start1 = data
                        else if (tagname == "평일관람종료시각")
                            end1 = data
                        else if (tagname == "공휴일관람시작시각")
                            start2 = data
                        else if (tagname == "공휴일관람종료시각")
                            end2 = data
                        else if (tagname == "어른관람료")
                            fee1 = data
                        else if (tagname == "청소년관람료")
                            fee2 = data
                        else if (tagname == "어린이관람료")
                            fee3 = data
                        else if (tagname == "박물관미술관소개") {
                            info = data
                            timemap.put(name, index)
                            index++
                            timelist.add(
                                timeclass(
                                    name,
                                    tel,
                                    homepage,
                                    start1,
                                    end1,
                                    start2,
                                    end2,
                                    fee1,
                                    fee2,
                                    fee3,
                                    info
                                )
                            )
                        }
                        bset = false
                    }
                } else if (eventype == XmlPullParser.END_TAG) {

                }
                eventype = xpp.next()
            }
        } catch (e: Exception) {
            // textView.text = e.message
        }
    }

    fun showing4(input: timeclass) {
        main42.visibility = VISIBLE
        main41.visibility = INVISIBLE
        textView421.text = input.name
        // textView42.,text="홈페이지 : ${input.homepage}"
        textView422.text =
            "전화번호 : " + input.tel + "\n" + "홈페이지 : ${input.homepage} \n평일관람시작시각 : ${input.start1}\n평일관람종료시각 : ${input.end1}\n공휴일관람시작시각 : ${input.start2}\n공휴일관람종료시각 : ${input.end2}\n"
        textView422.append("\n *관람료\n어른 - ${input.fee1}\n청소년 - ${input.fee2}\n어린이 - ${input.fee3}\n\n소개 : ${input.info}")


        button42.setOnClickListener {
            main41.visibility = VISIBLE
            main42.visibility = INVISIBLE
        }
    }


    /*
     override fun onDestroy() {
         super.onDestroy()
         mMapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
         mMapView!!.setShowCurrentLocationMarker(false)

     }*/

    override fun onCurrentLocationUpdate(mapView: MapView, currentLocation: MapPoint, accuracyInMeters: Float) {
        val mapPointGeo = currentLocation.mapPointGeoCoord
        mycurrentloc = currentLocation
        var dist:FloatArray= FloatArray(1)
        m_cul.clear()

        // Location.distanceBetween(p1!!.mapPoint.mapPointGeoCoord.latitude,p1!!.mapPoint.mapPointGeoCoord.longitude,mycurrentloc!!.mapPointGeoCoord.latitude,mycurrentloc!!.mapPointGeoCoord.longitude,dist)

        Log.e("sd",dist.toString())
        var minium :DoubleArray = DoubleArray(10)
        var min : Double = 100000.toDouble()
        var cul:ArrayList<CultureProperties> = ArrayList()
        culture.forEach {

            if (it.longitude.isNotEmpty() && it.latitude.isNotEmpty()) {

                var dist:FloatArray= FloatArray(1)
                Location.distanceBetween(it.latitude.toDouble(), it.longitude.toDouble(),mycurrentloc!!.mapPointGeoCoord.latitude,mycurrentloc!!.mapPointGeoCoord.longitude,dist)

                if(min>dist[0]){
                    cul.add(it)
                    //cul_array.add(it.name)
                }
            }
        }

        cul.forEach {
            Log.e("dd", it.name)
        }


    }


    override fun getPressedCalloutBalloon(p0: MapPOIItem?): View? {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return null;
    }

    override fun getCalloutBalloon(p0: MapPOIItem?): View? {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return null
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {



    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {


        var round :Double = 9000.toDouble()
        var dist:FloatArray= FloatArray(1)
        //  cul_array.clear()
        Location.distanceBetween(p1!!.mapPoint.mapPointGeoCoord.latitude,p1!!.mapPoint.mapPointGeoCoord.longitude,mycurrentloc!!.mapPointGeoCoord.latitude,mycurrentloc!!.mapPointGeoCoord.longitude,dist)

        if(dist[0]<9000){
            p1!!.markerType = MapPOIItem.MarkerType.YellowPin
            mMapView!!.addPOIItem(p1!!)
            //culture 값을 change 하고 maeker set
            Toast.makeText(this,"방문 완료!",Toast.LENGTH_SHORT).show()
            cultureDbHelper.updateVisit(p1!!.itemName)
            culture=cultureDbHelper.loadCultureList()
            culture.forEach {
                if(it.name == p1!!.itemName)
                    Log.e("as",it.visit.toString())
            }

        }else{
            Toast.makeText(this,"거리가 너무 멉니다! 문화재와의 거리" +(dist[0].toInt()/1000.0).toString()+"km ",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
        ///  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.


    }

    override fun onMapViewInitialized(p0: MapView?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        mMapView!!.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }


    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        culture=cultureDbHelper.loadCultureList()
     /*   var dist:FloatArray= FloatArray(1)
        m_cul.clear()

        var minium :DoubleArray = DoubleArray(10)
        var min : Double = 50000.toDouble()
        culture.forEach {

            if (it.longitude.isNotEmpty() && it.latitude.isNotEmpty()) {

                var dist:FloatArray= FloatArray(1)
                Location.distanceBetween(it.latitude.toDouble(), it.longitude.toDouble(),mycurrentloc!!.mapPointGeoCoord.latitude,mycurrentloc!!.mapPointGeoCoord.longitude,dist)

                if(min>dist[0]){
                    m_cul.add(it)
                    Log.e("cul_array",it.name)
                }
            }
        }

        pinpoint(culture)
*/
        /*   var adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cul_array)
           listView.adapter = adapter

   */
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

        var mylocbtn = findViewById<Button>(R.id.myloc)
        var layer = findViewById<LinearLayout>(R.id.drawer)
        if(mylocbtn.visibility.equals(View.VISIBLE)){
            mylocbtn.visibility = View.INVISIBLE
            layer.visibility = View.INVISIBLE
            //  main2.vi
        }
        else{
            mylocbtn.visibility = View.VISIBLE
            layer.visibility = View.VISIBLE
        }

        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun pinpoint(culture: ArrayList<CultureProperties>): Unit {
        var num: Int = 0

        culture.forEach {

            if (it.longitude.isNotEmpty() && it.latitude.isNotEmpty()) {
                var visited: Boolean = false
                var marker: MapPOIItem = MapPOIItem()
                var museumloc: MapPoint = MapPoint.mapPointWithGeoCoord(it.latitude.toDouble(), it.longitude.toDouble())
                marker.tag = num++


                Log.e("marker",it.name)
                marker.mapPoint = museumloc
                Log.e("add", it.longitude + it.latitude)
                Log.e("bool",it.visit.toString())
                if (it.visit==false) {
                    marker.markerType = MapPOIItem.MarkerType.BluePin
                    marker.selectedMarkerType = MapPOIItem.MarkerType.BluePin

                    marker.itemName = it.name.toString()
                } else {

                    marker.itemName = it.name.toString()+" 방문 완료! "
                    marker.markerType = MapPOIItem.MarkerType.RedPin
                    marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                }
                mMapView!!.addPOIItem(marker)

            }
        }
    }

    private fun checkAppPermission(requestPermission: Array<String>): Boolean {
        val requestResult = BooleanArray(requestPermission.size)
        for (i in requestResult.indices) {
            requestResult[i] =
                ContextCompat.checkSelfPermission(this, requestPermission[i]) == PackageManager.PERMISSION_GRANTED
            if (!requestResult[i]) { // 허가안될경우
                return false
            }
        }
        return true
    }
    fun askPermission(requestPermission: Array<String>, REQ_PERMISSION: Int) {
        ActivityCompat.requestPermissions(
            this, requestPermission, REQ_PERMISSION
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode){
            ACCESS_REQUEST ->{
                if(checkAppPermission(permissions)){
                    Toast.makeText(this,"권한 승인", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"승인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun initLocation() {
        if (checkAppPermission(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        ) {
            Toast.makeText(this, "권한승인됨", Toast.LENGTH_LONG).show()
        } else {
            askPermission(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                    // android,Manifest.permission.
                ), ACCESS_REQUEST
            )

        }
    }


    override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView, v: Float) {
    }
    override fun onCurrentLocationUpdateFailed(mapView: MapView) {
    }
    override fun onCurrentLocationUpdateCancelled(mapView: MapView) {
    }
    override fun onReverseGeoCoderFoundAddress(mapReverseGeoCoder: MapReverseGeoCoder, s: String) {
        mapReverseGeoCoder.toString()
        onFinishReverseGeoCoding(s)
    }
    override fun onReverseGeoCoderFailedToFindAddress(mapReverseGeoCoder: MapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail")
    }
    private fun onFinishReverseGeoCoding(result: String) {
    }





}