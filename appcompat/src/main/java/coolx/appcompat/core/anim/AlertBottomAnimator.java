package coolx.appcompat.core.anim;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.app.AppCompatDialog;

import coolx.appcompat.app.AlertDialog;
import coolx.appcompat.utils.ScreenUtils;

class AlertBottomAnimator {
    private static final String DISMISSING = "dismissing";
    private static final String SHOWING = "showing";

    static void showPanel(Context context, View parentPanel) {
        TranslateAnimation mTranslateAnimation = new TranslateAnimation(0, 0, ScreenUtils.getScreenHeight(context), 0);
        Interpolator interpolator = new PathInterpolator(0.2f, 0.1f,0.0f, 1.0f);
        mTranslateAnimation.setDuration(300);
        mTranslateAnimation.setInterpolator(interpolator);
        mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                parentPanel.setTag(SHOWING);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                parentPanel.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        parentPanel.startAnimation(mTranslateAnimation);
    }

    static void dismissPanel(Context mContext, View parentPanel, AppCompatDialog mDialog) {
        TranslateAnimation mAnimation = new TranslateAnimation(0, 0, 0, ScreenUtils.getScreenHeight(mContext));
        Interpolator interpolator = new PathInterpolator(0.2f, 0.1f,0.0f, 1.0f);
        mAnimation.setDuration(250);
        mAnimation.setInterpolator(interpolator);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                parentPanel.setTag(DISMISSING);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((AlertDialog) mDialog).realDismiss();
                parentPanel.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                ((AlertDialog) mDialog).realDismiss();
            }
        });
        parentPanel.startAnimation(mAnimation);
    }

    static void showDim(View dimView) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dimView, "alpha", 0, 1.0f);
        objectAnimator.setInterpolator(new DecelerateInterpolator(1.0f));
        objectAnimator.setDuration(100);
        objectAnimator.start();
    }
    
    static void dismissDim(View dimView) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dimView, "alpha", 1.0f, 0);
        Interpolator interpolator = new PathInterpolator(0.2f, 0.1f,0.0f, 1.0f);
        objectAnimator.setInterpolator(interpolator);
        objectAnimator.setDuration(350);
        objectAnimator.start();
    }
}
