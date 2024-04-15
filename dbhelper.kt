package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE_QUERY =
            ("CREATE TABLE $TABLE_STUDENTS (" +
                    "$COL_ROLL_NO INT PRIMARY KEY, " +
                    "$COL_NAME TEXT, " +
                    "$COL_MARKS INT)")
        db.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun insertStudent(rollno: Int,name: String,marks:Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ROLL_NO,rollno)
        contentValues.put(COL_NAME,name)
        contentValues.put(COL_MARKS,marks)
        return db.insert(TABLE_STUDENTS,null,contentValues) != -1L
    }

    fun display(): Cursor{
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_STUDENTS ",null)
    }

    fun update(rollno: Int,name: String,marks: Int): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.apply {
            put(COL_NAME,name)
            put(COL_MARKS,marks)
        }
        val u = db.update(TABLE_STUDENTS,contentValues,"$COL_ROLL_NO = ?", arrayOf(rollno.toString()))
        return u>0
    }

    fun delete(rollno: Int): Int{
        val db = this.writableDatabase
        return db.delete(TABLE_STUDENTS,"$COL_ROLL_NO = ?", arrayOf(rollno.toString()))
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "student.db"
        const val TABLE_STUDENTS = "Students"
        const val COL_ROLL_NO = "roll_no"
        const val COL_NAME = "name"
        const val COL_MARKS = "marks"
    }
}
