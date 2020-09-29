package com.demo.kotlinexamples.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.demo.kotlinexamples.R
import kotlinx.android.synthetic.main.activity_time_picker.*
import kotlinx.android.synthetic.main.activity_time_picker.toolbar_include
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class TimePickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_picker)
        setSupportActionBar(toolbar_include.toolbar)
        toolbar_include.toolbar.setTitle("")
        toolbar_include.txtviewTitle.text = "Time Picker"

        timePicker.setOnTimeChangedListener { _, hour, minute -> var hour = hour
            var am_pm = ""
            // AM_PM decider logic
            when {hour == 0 -> { hour += 12
                am_pm = "AM"
            }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> { hour -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            if (textView != null) {
                val hour = if (hour < 10) "0" + hour else hour
                val min = if (minute < 10) "0" + minute else minute
                // display format of time
                val msg = "Time is: $hour : $min $am_pm"
                textView.text = msg
                textView.visibility = ViewGroup.VISIBLE
            }
        }
    }
}