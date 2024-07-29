package com.gorgeous.coolx.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gorgeous.coolx.R
import com.gorgeous.coolx.databinding.DialogSpecialBinding
import coolx.appcompat.app.AlertDialog
import coolx.appcompat.interfaces.XClickIntention

class SpecialDialog : AppCompatActivity(), XClickIntention {
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0

    private var mHour = 0
    private var mMinute = 0
    private var mSecond = 0

    private lateinit var binding: DialogSpecialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogSpecialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListener(binding.timePickerDialogHMS,
                           binding.timePickerDialogHM,
                           binding.datePickerDialog)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.timePickerDialogHMS -> {
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.TIME_PICKER_1)
                    .setTitle(R.string.x_special_dialog_time_picker_title)
                    .setOnTimeSelectedListener { hour, minute, second ->
                        mHour = hour
                        mMinute = minute
                        mSecond = second
                    }
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm) { _: DialogInterface?, _: Int ->
                        Toast.makeText(this, "Time: $mHour" + "时" + "$mMinute" + "分" + "$mSecond" + "秒", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            }

            R.id.timePickerDialogHM -> {
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.TIME_PICKER_2)
                    .setTitle(R.string.x_special_dialog_time_picker_title)
                    .setOnTimeSelectedListener { hour, minute, _ ->
                        mHour = hour
                        mMinute = minute
                    }
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm) { _: DialogInterface?, _: Int ->
                        Toast.makeText(this, "Time: $mHour" + "时" + "$mMinute" + "分", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            }

            R.id.datePickerDialog -> {
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.DATE_PICKER)
                    .setTitle(R.string.x_special_dialog_date_picker_title)
                    .setOnDateSelectedListener { year, month, day ->
                        mYear = year
                        mMonth = month
                        mDay = day
                    }
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm) { _: DialogInterface?, _: Int ->
                        Toast.makeText(this, "Date: $mYear-$mMonth-$mDay", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            }
        }
    }
}