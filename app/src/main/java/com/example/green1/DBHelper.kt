package com.example.green1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)  {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "DB.db"
        val SQL_CREATE_ENTRIES = "CREATE TABLE " + MyEntry.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY," +
                MyEntry.COLUMN_NAME_TITLE + " TEXT," +
                MyEntry.COLUMN_NAME_ADDRESS + "  TEXT," +
                MyEntry.COLUMN_NAME_DATE + " TEXT," +
                MyEntry.COLUMN_NAME_TEXT + " TEXT )"
    }

    class MyEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "entry"
            val COLUMN_NAME_TITLE = "title"
            val COLUMN_NAME_ADDRESS = "address"
            val COLUMN_NAME_DATE = "date"
            val COLUMN_NAME_TEXT = "text"
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun saveDiary(file: MyClass) {

        val db = writableDatabase
        val values = ContentValues()

        values.put(MyEntry.COLUMN_NAME_TITLE, file.title)
        values.put(MyEntry.COLUMN_NAME_ADDRESS, file.address)
        values.put(MyEntry.COLUMN_NAME_DATE, file.date)
        values.put(MyEntry.COLUMN_NAME_TEXT,file.text)

        //같은 정보가 TABLE에 있으면 UPDATE
        db.insert(MyEntry.TABLE_NAME, null, values)
    }

    fun updateDiary(file: MyClass){

        val db = writableDatabase

        val values = ContentValues()
        values.put(MyEntry.COLUMN_NAME_TITLE, file.title)
        values.put(MyEntry.COLUMN_NAME_ADDRESS, file.address)
        values.put(MyEntry.COLUMN_NAME_DATE, file.date)
        values.put(MyEntry.COLUMN_NAME_TEXT,file.text)

        db.update(MyEntry.TABLE_NAME,values, MyEntry.COLUMN_NAME_TITLE+"=?", arrayOf(file.title))
        db.update(MyEntry.TABLE_NAME,values, MyEntry.COLUMN_NAME_ADDRESS+"=?", arrayOf(file.address))
        db.update(MyEntry.TABLE_NAME,values, MyEntry.COLUMN_NAME_DATE+"=?", arrayOf(file.date))
        db.update(MyEntry.TABLE_NAME,values, MyEntry.COLUMN_NAME_TEXT+"=?", arrayOf(file.text))
    }

    fun loadDiaryList(): ArrayList<MyClass> {
        val myArrayList = ArrayList<MyClass>()
        val db = readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            MyEntry.COLUMN_NAME_TITLE,
            MyEntry.COLUMN_NAME_ADDRESS,
            MyEntry.COLUMN_NAME_DATE,
            MyEntry.COLUMN_NAME_TEXT
        )

        val cursor = db.query(MyEntry.TABLE_NAME,
            projection, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndex(MyEntry.COLUMN_NAME_TITLE))
            val address = cursor.getString(cursor.getColumnIndex(MyEntry.COLUMN_NAME_ADDRESS))
            val date = cursor.getString(cursor.getColumnIndex(MyEntry.COLUMN_NAME_DATE))
            val text = cursor.getString(cursor.getColumnIndex(MyEntry.COLUMN_NAME_TEXT))
            val my = MyClass(title,address,date,text)
            myArrayList.add(my)
        }
        return myArrayList
    }

    fun removeMemo(file: MyClass) {
        val db = readableDatabase
        val selection = MyEntry.COLUMN_NAME_TITLE + " LIKE ?"
        val selectionArgs = arrayOf(file.title)
        db.delete(MyEntry.TABLE_NAME, selection, selectionArgs)
    }
}