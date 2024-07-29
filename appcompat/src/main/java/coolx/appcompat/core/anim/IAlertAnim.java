package coolx.appcompat.core.anim;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatDialog;

interface IAlertAnim {

    void cancelAnimator();

    /**
     * Execute center dialog show animation.
     */
    void executeCenterShowAnim(View parentPanel, View dimView);

    /**
     * Execute bottom type dialog show animation.
     */
    void executeBottomShowAnim(Context context, View parentPanel, View dimView);

    /**
     * Execute center dialog show animation.
     */
    void executeCenterDismissAnim(View parentPanel, View dimView, AppCompatDialog appCompatDialog);

    /**
     * Execute bottom dialog show animation.
     */
    void executeBottomDismissAnim(Context context, View parentPanel, View dimView, AppCompatDialog appCompatDialog);
}
