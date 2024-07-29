package com.gorgeous.coolx.dialog

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import coolx.appcompat.app.AlertDialog

class TransparentActivity : Activity(), DialogInterface.OnCancelListener{
    private lateinit var mDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDialog = AlertDialog.Builder(this)
            .setTitle("特殊场景")
            .setMessage("部分特殊场景，一个 Activity 内部只是拉起一个 Dialog，没有其他业务逻辑，需要实现 CoolX 效果。\n\n" +
                    "例如：MediaProjectionPermissionActivity\n\n" +
                    "则需指定 Activity 主题：@style/coolxTransparentActivityStyle")
            .setPositiveButton(coolx.appcompat.R.string.common_confirm) { _: DialogInterface?, _: Int ->
                finish()
            }
            .setOnCancelListener(this)
            .create();

        mDialog.show()
    }

    override fun onCancel(dialog: DialogInterface?) {
        finish()
    }
}