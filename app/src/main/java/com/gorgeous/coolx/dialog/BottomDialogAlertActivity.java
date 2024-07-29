package com.gorgeous.coolx.dialog;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;

import coolx.appcompat.app.AlertActivity;
import coolx.appcompat.app.AlertController;
import coolx.appcompat.app.AlertDialog;

public class BottomDialogAlertActivity extends AlertActivity implements DialogInterface.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialog();
    }

    void createDialog() {
        final AlertController.AlertParams p = mAlertParams;
        p.mMessage = "电话请求开启蓝牙权限";
        p.mPositiveButtonText = "确定";
        p.mPositiveButtonListener = this;
        p.mNegativeButtonText = "取消";

        setupAlert();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE) {
            finish();
        }
    }
}
