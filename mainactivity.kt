package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DatabaseHelper.Companion

class MainActivity : AppCompatActivity() {

    private lateinit var rollNumberEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var marksEditText: EditText
    private lateinit var addButton: Button
    private lateinit var db: DatabaseHelper
    private lateinit var displayButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var displayText: TextView
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rollNumberEditText = findViewById(R.id.roll_number_edit_text)
        nameEditText = findViewById(R.id.name_edit_text)
        marksEditText = findViewById(R.id.marks_edit_text)
        addButton = findViewById(R.id.add_button)
        displayButton = findViewById(R.id.display_button)
        displayText = findViewById(R.id.display_text)
        updateButton = findViewById(R.id.update_button)
        listView = findViewById(R.id.listview)
        deleteButton = findViewById(R.id.delete_button)
        db = DatabaseHelper(this)

        addButton.setOnClickListener {
            val rollNumber = rollNumberEditText.text.toString().toIntOrNull()
            val name = nameEditText.text.toString()
            val marks = marksEditText.text.toString().toIntOrNull()

            if (rollNumber == null || name.isEmpty() || marks == null) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val success = db.insertStudent(rollNumber,name,marks)
                if(success){
                    Toast.makeText(this,"Inserted",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"Not inserted",Toast.LENGTH_SHORT).show()
                }
                rollNumberEditText.setText("")
                nameEditText.setText("")
                marksEditText.setText("")
            }
        }

        displayButton.setOnClickListener {
            val stud_names = ArrayList<String>()
            val cur = db.display()
            if(cur.count==0){
                Toast.makeText(this,"No students",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                var sb = StringBuilder()
                while (cur.moveToNext()){
                    var rollno = cur.getInt(0).toString()
                    var name = cur.getString(1)
                    var marks = cur.getInt(2).toString()
                    stud_names.add(name)
                    sb.append("Roll no : $rollno\n")
                    sb.append("Name : $name\n")
                    sb.append("Marks : $marks\n")
                }
                displayText.text = sb.toString()
                val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,stud_names)
                listView.adapter = adapter
            }
            updateButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
        }
        updateButton.setOnClickListener {
            val rollNumber = rollNumberEditText.text.toString().toIntOrNull()
            val name = nameEditText.text.toString()
            val marks = marksEditText.text.toString().toIntOrNull()

            if (rollNumber==null || name.isEmpty() || marks==null) {
                displayText.text = "Please fill all fields"
            } else {
                val u = db.update(rollNumber,name,marks)
                if(u){
                    Toast.makeText(this,"updated",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"not updated",Toast.LENGTH_SHORT).show()
                }
            }
        }
        listView.setOnItemClickListener{parent,_,pos,id->
            val selected_item = parent.getItemAtPosition(pos) as String
            Toast.makeText(this,selected_item,Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("item",selected_item)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            val rollNumber = rollNumberEditText.text.toString().toIntOrNull()
            if(rollNumber == null){
                Toast.makeText(this,"enter roll no",Toast.LENGTH_SHORT).show()
            }
            else {
                val d = db.delete(rollNumber)
                if(d==0){
                    Toast.makeText(this,"No row was deleted",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"Row was deleted",Toast.LENGTH_SHORT).show()
                }
            }

        }
//        deleteButton.setOnClickListener {
//            val rollNumber = rollNumberEditText.text.toString()
//            val rowsAffected = dbHelper.deleteStudent(rollNumber)
//            if (rowsAffected > 0) {
//                displayText.text = "Student deleted successfully"
//            } else {
//                displayText.text = "Failed to delete student"
//            }
//        }

    }

//    inner class DatabaseHelper(context: Context) :
//        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//        override fun onCreate(db: SQLiteDatabase) {
//            val CREATE_TABLE_QUERY =
//                ("CREATE TABLE $TABLE_NAME ($COLUMN_ROLL_NUMBER TEXT PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_MARKS TEXT)")
//            db.execSQL(CREATE_TABLE_QUERY)
//        }
//
//        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
//            onCreate(db)
//        }
//
//        fun insertStudent(rollNumber: String, name: String, marks: String): Boolean {
//            val db = this.writableDatabase
//            val contentValues = ContentValues()
//            contentValues.put(COLUMN_ROLL_NUMBER, rollNumber)
//            contentValues.put(COLUMN_NAME, name)
//            contentValues.put(COLUMN_MARKS, marks)
//            val result = db.insert(TABLE_NAME, null, contentValues)
//            db.close()
//            return result != -1L
//        }
//
//        fun getAllStudents(): Cursor {
//            val db = this.readableDatabase
//            return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
//        }
//
//        fun deleteStudent(rollNumber: String): Int {
//            val db = this.writableDatabase
//            return db.delete(TABLE_NAME, "$COLUMN_ROLL_NUMBER = ?", arrayOf(rollNumber))
//        }
//
//        fun updateStudent(rollNumber: String, name: String, marks: String): Int {
//            val db = this.writableDatabase
//            val contentValues = ContentValues()
//            contentValues.put(COLUMN_NAME, name)
//            contentValues.put(COLUMN_MARKS, marks)
//            return db.update(
//                TABLE_NAME,
//                contentValues,
//                "$COLUMN_ROLL_NUMBER = ?",
//                arrayOf(rollNumber)
//            )
//        }
//    }
//
//    companion object {
//        private const val DATABASE_VERSION = 1
//        private const val DATABASE_NAME = "StudentDatabase.db"
//        private const val TABLE_NAME = "Student"
//        private const val COLUMN_ROLL_NUMBER = "roll_number"
//        private const val COLUMN_NAME = "name"
//        private const val COLUMN_MARKS = "marks"
//    }
}
