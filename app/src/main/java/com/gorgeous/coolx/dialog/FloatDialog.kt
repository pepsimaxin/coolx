package com.gorgeous.coolx.dialog;

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gorgeous.coolx.R
import com.gorgeous.coolx.databinding.DialogFloatBinding
import coolx.appcompat.app.AlertDialog
import coolx.appcompat.interfaces.XClickIntention

class FloatDialog : AppCompatActivity(), XClickIntention {

    private lateinit var binding: DialogFloatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogFloatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListener(binding.floatDialogDone,
                           binding.floatDialogAlert,
                           binding.floatDialogError)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.floatDialogDone ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.FLOAT_DONE)
                    .setFloatMessage(R.string.x_float_dialog_message_done)
                    .show()

            R.id.floatDialogAlert ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.FLOAT_ALERT)
                    .setFloatMessage(R.string.x_float_dialog_message_alert)
                    .show()

            R.id.floatDialogError ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.FLOAT_ERROR)
                    .setFloatMessage(R.string.x_float_dialog_message_error)
                    .show()
        }
    }
}