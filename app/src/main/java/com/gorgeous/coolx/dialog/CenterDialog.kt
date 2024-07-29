package com.gorgeous.coolx.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gorgeous.coolx.R
import com.gorgeous.coolx.databinding.DialogCenterBinding
import coolx.appcompat.app.AlertDialog
import coolx.appcompat.interfaces.XClickIntention

class CenterDialog : AppCompatActivity(), XClickIntention {

    private lateinit var binding: DialogCenterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListener(binding.centerDialog,
                           binding.centerDialogRed,
                           binding.centerDialogBlack,
                           binding.centerDialogSimple,
                           binding.centerDialogSingleButton,
                           binding.centerDialogStack)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.centerDialog ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.CENTER)
                    .setTitle(R.string.x_center_dialog_title)
                    .setMessage(R.string.x_center_dialog_message_1)
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm) { _: DialogInterface?, _: Int ->
                        Toast.makeText(this, "居中确定按钮", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.centerDialogRed ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.CENTER_1)
                    .setTitle(R.string.x_center_dialog_title)
                    .setMessage(R.string.x_center_dialog_message_1)
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm) { _: DialogInterface?, _: Int ->
                        Toast.makeText(this, "居中确定按钮", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.centerDialogBlack ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.CENTER_2)
                    .setTitle(R.string.x_center_dialog_title)
                    .setMessage(R.string.x_center_dialog_message_2)
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.centerDialogSimple ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.CENTER_1)
                    .setMessage(R.string.x_center_dialog_message_3)
                    .setPositiveButton(R.string.x_center_dialog_positive_button_delete, null)
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.centerDialogSingleButton ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.CENTER_2)
                    .setTitle(R.string.x_center_dialog_title)
                    .setMessage(R.string.x_center_dialog_message_4)
                    .setNegativeButton(coolx.appcompat.R.string.common_confirm, null)
                    .show()
            R.id.centerDialogStack ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.CENTER_STACK)
                    .setTitle(R.string.x_center_dialog_title)
                    .setMessage(R.string.x_center_dialog_message_5)
                    .setPositiveButton("土豪直接下载", null)
                    .setNegativeButton("WiFi智能下载", null)
                    .show()
        }
    }
}