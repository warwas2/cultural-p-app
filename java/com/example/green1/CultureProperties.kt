package com.example.green1

import java.io.Serializable
import java.util.ArrayList

data class CultureProperties (var ctype:String, var name:String, var ccbaKdcd:String, var ccbaAsno:String
                              , var ccbaCtcd:String, var latitude:String, var longitude:String, var location:String, var time:String
                              , var imgUrl:String, var instruction:String, var voiceUrl:String, var visit:Boolean,var prefer:Boolean,var isEmpty:Boolean): Serializable
{
    constructor() : this("","","","","","","","","","","", "",false,false,false)
}
//문화재 종류,문화재 이름 ,종목코드, 지정번호, 시도코드, 위도 경도
// 위치(한글) ,시대, 이미지 (배열로) ,설명, 나레이션
//즐찾여부
