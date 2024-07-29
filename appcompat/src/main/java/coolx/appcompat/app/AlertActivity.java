package coolx.appcompat.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.Nullable;

import coolx.appcompat.R;

/**
 * Description: AlertActivity
 * @Author    : Marco
 * @Update    : 2023.09.14
 */
public abstract class AlertActivity extends Activity implements DialogInterface, DialogInterface.OnDismissListener,
        DialogInterface.OnCancelListener {

    public AlertActivity() {}

    protected AlertDialog.Builder mBuilder;
    protected AlertDialog mAlertDialog;

    protected AlertController mAlert;
    protected AlertController.AlertParams mAlertParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBuilder = new AlertDialog.Builder(this, R.style.coolxTransparentActivityStyle);
        mBuilder.setOnCancelListener(this);
        mAlertDialog = mBuilder.create();
        mAlert = mAlertDialog.mAlert;
        mAlertParams = mAlertDialog.mAlertParams;
    }

    public void dismiss() {
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    public void cancel() {
        finish();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return dispatchPopulateAccessibilityEvent(event);
    }

    public static boolean dispatchPopulateAccessibilityEvent(Activity act,
        AccessibilityEvent event) {
        event.setClassName(Dialog.class.getName());
        event.setPackageName(act.getPackageName());

        ViewGroup.LayoutParams params = act.getWindow().getAttributes();
        boolean isFullScreen = (params.width == ViewGroup.LayoutParams.MATCH_PARENT) &&
                (params.height == ViewGroup.LayoutParams.MATCH_PARENT);
        event.setFullScreen(isFullScreen);

        return false;
    }

    protected void setupAlert() {
        mAlert.installContent(mAlertParams);
        mAlertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAlert.onKeyDown(keyCode, event)) return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mAlert.onKeyUp(keyCode, event)) return true;
        return super.onKeyUp(keyCode, event);
    }
}
