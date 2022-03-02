package com.adeeva.idn.remindyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.adeeva.idn.remindyou.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_sleep_schedule.*
import java.text.SimpleDateFormat
import java.util.*
import com.adeeva.idn.myalarmapp.fragment.TimePickerFragment as TimePickerFragment

class SleepScheduleActivity : AppCompatActivity(), View.OnClickListener, TimePickerFragment.DialogTimeListener {

    private var binding: ActivityMainBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver

    companion object{
        private const val TIME_PICKER_SLEEP_TAG ="TimePickerSleepWake"
        private const val TIME_PICKER_WAKE_TAG ="TimePickerSleepWake"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_sleep_schedule)

        btn_set_time_sleep.setOnClickListener(this)
        btn_set_time_wake.setOnClickListener(this)
        btn_check.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_set_time_sleep -> {
                val timePickerFragmentSleep = TimePickerFragment()
                timePickerFragmentSleep.show(supportFragmentManager, TIME_PICKER_SLEEP_TAG)
            }
            R.id.btn_set_time_wake -> {
                val timePickerFragmentWake = TimePickerFragment()
                timePickerFragmentWake.show(supportFragmentManager, TIME_PICKER_WAKE_TAG)
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val timeFormatSleep = SimpleDateFormat ("HH:mm " , Locale.getDefault())

        when(tag) {
            TIME_PICKER_SLEEP_TAG -> tv_time.text =
                timeFormatSleep.format(calendar.time)
            else -> {

            }
        }
        val timeFormatWake = SimpleDateFormat ("HH:mm " , Locale.getDefault())
        when(tag) {
            TIME_PICKER_WAKE_TAG -> tv_time_wake.text =
                timeFormatWake.format(calendar.time)
            else -> {

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}