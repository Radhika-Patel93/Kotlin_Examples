package com.demo.kotlinexamples.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.demo.kotlinexamples.R
import kotlinx.android.synthetic.main.activity_date_picker.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import java.util.*

class DatePickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)
        setSupportActionBar(toolbar_include.toolbar)
        toolbar_include.toolbar.setTitle("")
        toolbar_include.txtviewTitle.text = "Date Picker"

        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)) { view, year, month, day ->
            val month = month + 1
            val msg = "You Selected: $day/$month/$year"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

        }


    }
}