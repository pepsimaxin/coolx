package coolx.appcompat.core.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import coolx.appcompat.app.AlertDialog;

class AlertCenterAnimator {
    private static final String DISMISSING = "dismissing";
    private static final String SHOWING = "showing";

    static void showPanel(View parentPanel) {
        ObjectAnimator mAnimation1, mAnimation2, mAnimation3;
        Interpolator interpolator = new PathInterpolator(0.2f, 0.1f,0.0f, 1.0f);
        mAnimation1 = ObjectAnimator.ofFloat(parentPanel, "scaleX", 0.7f, 1.0f);
        mAnimation2 = ObjectAnimator.ofFloat(parentPanel, "scaleY", 0.7f, 1.0f);
        mAnimation3 = ObjectAnimator.ofFloat(parentPanel, "alpha", 0f, 1.0f);
        mAnimation1.setDuration(300);
        mAnimation2.setDuration(300);
        mAnimation3.setDuration(150);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(mAnimation1, mAnimation2, mAnimation3);
        animatorSet.setInterpolator(interpolator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                parentPanel.setTag(SHOWING);
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                parentPanel.clearAnimation();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
            }
        });
        animatorSet.start();
    }

    static void dismissPanel(View parentPanel, AppCompatDialog mDialog) {
        ObjectAnimator mAnimation1, mAnimation2, mAnimation3;
        Interpolator interpolator = new PathInterpolator(0.0f, 0.0f,0.1f, 1.0f);
        mAnimation1 = ObjectAnimator.ofFloat(parentPanel, "scaleX", 1.0f, 0.7f);
        mAnimation2 = ObjectAnimator.ofFloat(parentPanel, "scaleY", 1.0f, 0.7f);
        mAnimation3 = ObjectAnimator.ofFloat(parentPanel, "alpha", 1.0f, 0.0f);
        mAnimation1.setDuration(250);
        mAnimation2.setDuration(250);
        mAnimation3.setDuration(100);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(mAnimation1, mAnimation2, mAnimation3);
        animatorSet.setInterpolator(interpolator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                parentPanel.setTag(DISMISSING);
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                ((AlertDialog) mDialog).realDismiss();
                parentPanel.clearAnimation();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
                ((AlertDialog) mDialog).realDismiss();
            }
        });
        animatorSet.start();
    }

    static void showDim(View dimView) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dimView, "alpha", 0, 1.0f);
        objectAnimator.setInterpolator(new DecelerateInterpolator(1.0f));
        objectAnimator.setDuration(250);
        objectAnimator.start();
    }

    static void dismissDim(View dimView) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dimView, "alpha", 1.0f, 0);
        Interpolator interpolator = new PathInterpolator(0.0f, 0.0f,0.1f, 1.0f);
        objectAnimator.setInterpolator(interpolator);
        objectAnimator.setDuration(200);
        objectAnimator.start();
    }
}
