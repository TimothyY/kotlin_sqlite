package com.example.kotlin_sqlite

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var etEmail: EditText? = null
    var etPassword: EditText? = null
    val dbHelper = MyDBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etEmail = findViewById<EditText>(R.id.etEmail)
        etPassword = findViewById<EditText>(R.id.etPassword)
        var btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener(this)
        var btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        if(p0?.id==R.id.btnLogin){

            val db = dbHelper.readableDatabase

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(BaseColumns._ID, MsUserContract.MsUser.MS_USER_EMAIL, MsUserContract.MsUser.MS_USER_PASSWORD)

            // Filter results WHERE "title" = 'My Title'
            val selection = "${MsUserContract.MsUser.MS_USER_EMAIL} = ?"
            val selectionArgs = arrayOf(etEmail?.text.toString())

            // How you want the results sorted in the resulting Cursor
            val sortOrder = "${MsUserContract.MsUser.MS_USER_PASSWORD} DESC"

            val cursor = db.query(
                MsUserContract.MsUser.TABLE_MS_USER,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
            )

            var loginResult= false
            with(cursor) {
                while (moveToNext()) {
                    val storedPassword = getString(getColumnIndexOrThrow(MsUserContract.MsUser.MS_USER_PASSWORD))
                    if(etPassword?.text.toString().compareTo(storedPassword)==0){
                        loginResult=true
                    }
                }
            }
            cursor.close()

            if(loginResult==false){
                Toast.makeText(this, "Login Fail",Toast.LENGTH_LONG).show()
            }else if(loginResult){
                Toast.makeText(this, "Login Success",Toast.LENGTH_LONG).show()
            }

        }else if(p0?.id==R.id.btnRegister){

            // Gets the data repository in write mode
            val db = dbHelper.writableDatabase

            // Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(MsUserContract.MsUser.MS_USER_EMAIL, etEmail?.text.toString())
                put(MsUserContract.MsUser.MS_USER_PASSWORD, etPassword?.text.toString())
            }

            // Insert the new row, returning the primary key value of the new row
            val newRowId = db?.insert(MsUserContract.MsUser.TABLE_MS_USER, null, values)

            Toast.makeText(this, "Register Success",Toast.LENGTH_LONG).show()
        }
    }
}