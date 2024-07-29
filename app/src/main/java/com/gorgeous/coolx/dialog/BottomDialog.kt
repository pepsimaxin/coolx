package com.gorgeous.coolx.dialog

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gorgeous.coolx.R
import com.gorgeous.coolx.databinding.DialogBottomBinding
import coolx.appcompat.app.AlertDialog
import coolx.appcompat.interfaces.XClickIntention
import coolx.appcompat.view.LoadingDoneView

class BottomDialog : AppCompatActivity(), XClickIntention {
    private val country = arrayOf<CharSequence>("中国", "美国", "意大利", "葡萄牙", "俄罗斯", "法国")
    private val province = arrayOf("北京", "上海", "南京", "深圳", "成都")

    private lateinit var binding: DialogBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListener(
            binding.bottomDialog,
            binding.bottomDialogNoIcon,
            binding.bottomDialogStack,
            binding.bottomDialogSingleSelect,
            binding.bottomDialogMultiSelect,
            binding.bottomDialogDone,
            binding.bottomDialogInput,
            binding.bottomDialogLoading,
            binding.loadingDone,
            binding.bottomDialogSimple,
            binding.bottomDialogTransparentActivity,
            binding.bottomDialogAlertActivity,
            binding.bottomDialogProgressBar,
            binding.bottomDialogProgressBarNoMessage,
            binding.bottomDialogProgressBarPercent
        )
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bottomDialog ->
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.x_logo)
                    .setTitle(R.string.x_bottom_dialog_title)
                    .setMessage(R.string.x_bottom_dialog_message)
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.bottomDialogProgressBar ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.BOTTOM_PROGRESS)
                    .setTitle("进度标题")
                    .setProgressPrompt("提示信息")
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.bottomDialogProgressBarNoMessage -> {
                val alertDialog = AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.BOTTOM_PROGRESS)
                    .setTitle("正在复制，请稍后")
                    .setProgressPrompt("已完成传输")
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .create()
                alertDialog.show()
                alertDialog.setProgress(30)
            }
            R.id.bottomDialogProgressBarPercent ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.BOTTOM_PROGRESS)
                    .setTitle("正在复制，请稍后")
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.bottomDialogNoIcon ->
                AlertDialog.Builder(this)
                    .setTitle(R.string.x_bottom_dialog_title)
                    .setMessage(R.string.x_bottom_dialog_message)
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.bottomDialogStack ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.BOTTOM_STACK)
                    .setIcon(R.drawable.x_logo)
                    .setTitle("默认应用")
                    .setMessage("将百度网盘设为您的默认短信应用吗？")
                    .setPositiveButton("设为默认应用") { _: DialogInterface?, _: Int ->
                        Toast.makeText(this, "堆叠确定按钮", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.bottomDialogSingleSelect ->
                AlertDialog.Builder(this)
                    .setTitle(R.string.x_bottom_dialog_single_select_title)
                    .setSingleChoiceItems(country, 2) {
                            _: DialogInterface?, which: Int -> Toast.makeText(this, "which = " + which + ", select:" + country[which], Toast.LENGTH_SHORT).show()
                    }
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .show()
            R.id.bottomDialogMultiSelect ->
                AlertDialog.Builder(this)
                    .setTitle(R.string.x_bottom_dialog_multi_select_title)
                    .setMultiChoiceItems(province, booleanArrayOf(false, false, true, false, true)) {
                            _: DialogInterface?, _: Int, _: Boolean -> }
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.bottomDialogDone ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.DONE)
                    .setIcon(coolx.appcompat.R.drawable.coolx_preset_icon_done)
                    .setTitle(R.string.x_bottom_dialog_done_title)
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .show()
            R.id.bottomDialogInput ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.INPUT)
                    .setTitle(R.string.x_bottom_dialog_input_title)
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm) { _: DialogInterface?, _: Int -> }
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.bottomDialogLoading ->
                AlertDialog.Builder(this)
                    .setDialogType(AlertDialog.LOADING)
                    .setLoadingTitle(R.string.x_bottom_dialog_loading_title)
                    .show()
            R.id.loadingDone ->
                AlertDialog.Builder(this)
                    .setView(LoadingDoneView(this, "操作需要一点时间", "操作已完成", 2000))
                    .show()
            R.id.bottomDialogSimple ->
                AlertDialog.Builder(this)
                    .setMessage(R.string.x_bottom_dialog_simple_title)
                    .setPositiveButton(coolx.appcompat.R.string.common_confirm, null)
                    .setNegativeButton(coolx.appcompat.R.string.common_cancel, null)
                    .show()
            R.id.bottomDialogTransparentActivity -> {
                val intent = Intent(this, TransparentActivity::class.java)
                startActivity(intent)
            }
            R.id.bottomDialogAlertActivity -> {
                val intent = Intent(this, BottomDialogAlertActivity::class.java)
                startActivity(intent)
            }
        }
    }
}