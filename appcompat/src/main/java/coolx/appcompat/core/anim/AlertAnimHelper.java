package coolx.appcompat.core.anim;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatDialog;

public class AlertAnimHelper {

    private static IAlertAnim sAlertAnim;

    private AlertAnimHelper() {
        sAlertAnim = new AlertAnim();
    }

    public static void executeCenterShowAnim(View parentPanel, View dimView) {
        if (sAlertAnim == null) {
            sAlertAnim = new AlertAnim();
        }
        sAlertAnim.executeCenterShowAnim(parentPanel, dimView);
    }

    /**
     * Execute bottom show animation.
     */
    public static void executeBottomShowAnim(Context context, View parentPanel, View dimView) {
        if (sAlertAnim == null) {
            sAlertAnim = new AlertAnim();
        }
        sAlertAnim.executeBottomShowAnim(context, parentPanel, dimView);
    }

    public static void executeCenterDismissAnim(View parentPanel, View dimView, AppCompatDialog appCompatDialog) {
        if (sAlertAnim == null) {
            sAlertAnim = new AlertAnim();
        }
        sAlertAnim.executeCenterDismissAnim(parentPanel, dimView, appCompatDialog);
    }

    public static void executeBottomDismissAnim(Context mContext, View parentPanel, View dimView, AppCompatDialog appCompatDialog) {
        if (sAlertAnim == null) {
            sAlertAnim = new AlertAnim();
        }
        sAlertAnim.executeBottomDismissAnim(mContext, parentPanel, dimView, appCompatDialog);
    }

    public static void cancelAnimator() {
        if (sAlertAnim != null) {
            sAlertAnim.cancelAnimator();
        }
    }
}
