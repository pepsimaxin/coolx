package coolx.appcompat.core.anim;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatDialog;

public class AlertAnim implements IAlertAnim {
    private static final String DISMISSING = "dismissing";

    @Override
    public void cancelAnimator() {
    }

    @Override
    public void executeCenterShowAnim(View parentPanel, View dimView) {
        AlertCenterAnimator.showPanel(parentPanel);
        AlertCenterAnimator.showDim(dimView);
    }

    @Override
    public void executeBottomShowAnim(Context context, View parentPanel, View dimView) {
        AlertBottomAnimator.showPanel(context, parentPanel);
        AlertBottomAnimator.showDim(dimView);
    }

    @Override
    public void executeCenterDismissAnim(View parentPanel, View dimView, AppCompatDialog mDialog) {
        if (!DISMISSING.equals(parentPanel.getTag())) {
            AlertCenterAnimator.dismissPanel(parentPanel, mDialog);
            AlertCenterAnimator.dismissDim(dimView);
        }
    }

    @Override
    public void executeBottomDismissAnim(Context mContext, View parentPanel, View dimView, AppCompatDialog mDialog) {
        if (!DISMISSING.equals(parentPanel.getTag())) {
            AlertBottomAnimator.dismissPanel(mContext, parentPanel, mDialog);
            AlertBottomAnimator.dismissDim(dimView);
        }
    }
}
