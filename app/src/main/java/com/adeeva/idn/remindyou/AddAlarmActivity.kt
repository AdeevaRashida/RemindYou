package com.adeeva.idn.remindyou


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.adeeva.idn.myalarmapp.fragment.DatePickerFragment
import com.adeeva.idn.myalarmapp.fragment.TimePickerFragment
import com.adeeva.idn.myalarmapp.room.Alarm
import com.adeeva.idn.myalarmapp.room.AlarmDB
import com.adeeva.idn.remindyou.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_add_alarm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddAlarmActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener{

    private var binding: ActivityMainBinding? = null

    private lateinit var alarmReceiver: AlarmReceiver

    val db by lazy { AlarmDB(this) }

    companion object{
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePicker"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add_alarm)

        btn_set_date_other.setOnClickListener(this)
        btn_set_time_other.setOnClickListener(this)

        btn_add_alarm.setOnClickListener (this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_set_date_other -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.btn_set_time_other -> {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.btn_add_alarm -> {
                val onceDate = tv_date.text.toString()
                val onceTime = tv_time.text.toString()
                val onceMessage = et_note.text.toString()

                alarmReceiver.setOther(
                    this, AlarmReceiver.TYPE_OTHER,
                    onceDate,
                    onceTime,
                    onceMessage
                )

                CoroutineScope(Dispatchers.IO).launch {
                    db.alarmDao().addAlarm(
                        Alarm(0, onceTime, onceDate, onceMessage, AlarmReceiver.TYPE_OTHER)
                    )

                    finish()
                }
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormatOneTime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        tv_date.text = dateFormatOneTime.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val timeFormatOneTime = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag){
            TIME_PICKER_ONCE_TAG -> tv_time.text = timeFormatOneTime.format(calendar.time)
            else ->{

            }
        }
    }
    override fun onDestroy(){
        super.onDestroy()
        binding = null
    }

}