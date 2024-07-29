package coolx.appcompat.app;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.res.ResourcesCompat;

import coolx.appcompat.R;
import coolx.appcompat.core.anim.AlertAnimHelper;
import coolx.appcompat.utils.BarUtils;

class AlertDesign {
    static final String BOTTOM_STACK    = "bottom_stack";
    static final String BOTTOM_SPECIAL  = "bottom_special";
    static final String BOTTOM_PROGRESS = "bottom_progress";
    static final String CENTER          = "center_default";
    static final String CENTER_1        = "center_red";
    static final String CENTER_2        = "center_black";
    static final String CENTER_STACK    = "center_stack";
    static final String FLOAT_DONE      = "float_done";
    static final String FLOAT_ALERT     = "float_alert";
    static final String FLOAT_ERROR     = "float_error";
    static final String DONE            = "done";
    static final String LOADING         = "loading";
    static final String INPUT           = "input";
    static final String DATE_PICKER     = "date_picker";
    static final String TIME_PICKER_1   = "time_picker_hms";
    static final String TIME_PICKER_2   = "time_picker_hm";

    final int overrideContentLayout;
    final int alertType;
    final int buttonTheme;
    final int buttonStyle;
    final long autoDismissDelay;

    AlertDesign(int overrideContentLayout, int alertType, int buttonTheme, int buttonStyle, long autoDismissDelay) {
        this.overrideContentLayout = overrideContentLayout;
        this.alertType = alertType;
        this.buttonTheme = buttonTheme;
        this.buttonStyle = buttonStyle;
        this.autoDismissDelay = autoDismissDelay;
    }

    static AlertDesign fromType(String dialogType) {
        AlertDesign mDesign;
        switch (dialogType) {
            case FLOAT_DONE:
                mDesign = FLOAT_ALERT_DESIGN_DONE;
                break;
            case FLOAT_ALERT:
                mDesign = FLOAT_ALERT_DESIGN_ALERT;
                break;
            case FLOAT_ERROR:
                mDesign = FLOAT_ALERT_DESIGN_ERROR;
                break;
            case CENTER:
                mDesign = CENTER_ALERT_DESIGN_CENTER;
                break;
            case CENTER_1:
                mDesign = CENTER_ALERT_DESIGN_CENTER_1;
                break;
            case CENTER_2:
                mDesign = CENTER_ALERT_DESIGN_CENTER_2;
                break;
            case CENTER_STACK:
                mDesign = CENTER_ALERT_DESIGN_CENTER_STACK;
                break;
            case BOTTOM_STACK:
                mDesign = BOTTOM_STACK_DESIGN;
                break;
            case BOTTOM_SPECIAL:
                mDesign = BOTTOM_SPECIAL_DESIGN;
                break;
            case BOTTOM_PROGRESS:
                mDesign = BOTTOM_PROGRESS_DESIGN;
                break;
            case LOADING:
                mDesign = BOTTOM_LOADING_DESIGN;
                break;
            case DATE_PICKER:
                mDesign = BOTTOM_DATE_PICKER_DESIGN;
                break;
            case TIME_PICKER_1:
                mDesign = BOTTOM_ALERT_DESIGN_TIME_PICKER_HMS;
                break;
            case TIME_PICKER_2:
                mDesign = BOTTOM_ALERT_DESIGN_TIME_PICKER_HM;
                break;
            case INPUT:
                mDesign = BOTTOM_ALERT_DESIGN_INPUT;
                break;
            case DONE:
                mDesign = BOTTOM_ALERT_DESIGN_DONE;
                break;
            default:
                mDesign = BOTTOM_DEFAULT_DESIGN;
        }
        return mDesign;
    }

    /**
     * Alert design params for bottom type of alertdialog.
     */
    static final AlertDesign BOTTOM_DEFAULT_DESIGN = new AlertDesign(
            0, 0, 0, 0, -1
    );

    static final AlertDesign BOTTOM_STACK_DESIGN = new AlertDesign(
            0, 0, 0, 1, -1
    );

    static final AlertDesign BOTTOM_SPECIAL_DESIGN = new AlertDesign(
            0, 0, 0, 2, -1
    );

    static final AlertDesign BOTTOM_PROGRESS_DESIGN = new AlertDesign(
            R.layout.coolx_alert_dialog_root_progress, 0, 0, 0, -1
    );

    static final AlertDesign BOTTOM_LOADING_DESIGN = new AlertDesign(
            R.layout.coolx_alert_dialog_root_loading, 0, 0, 0, -1
    );

    static final AlertDesign BOTTOM_DATE_PICKER_DESIGN = new AlertDesign(
            R.layout.coolx_alert_dialog_root_date_picker, 0, 0, 0, -1
    );

    static final AlertDesign BOTTOM_ALERT_DESIGN_TIME_PICKER_HMS = new AlertDesign(
            R.layout.coolx_alert_dialog_root_time_picker_hms, 0, 0, 0, -1
    );

    static final AlertDesign BOTTOM_ALERT_DESIGN_TIME_PICKER_HM = new AlertDesign(
            R.layout.coolx_alert_dialog_root_time_picker_hm, 0, 0, 0, -1
    );

    // INPUT Type Dialog
    static final AlertDesign BOTTOM_ALERT_DESIGN_INPUT = new AlertDesign(
            R.layout.coolx_alert_dialog_root_input, 0, 0, 0, -1
    );

    static final AlertDesign BOTTOM_ALERT_DESIGN_DONE= new AlertDesign(
            R.layout.coolx_alert_dialog_root_done, 0, 0, 0, -1
    );

    static final AlertDesign FLOAT_ALERT_DESIGN_DONE = new AlertDesign(
            R.layout.coolx_float_dialog_root_done, -1, 0, -1, 3000
    );

    static final AlertDesign FLOAT_ALERT_DESIGN_ALERT = new AlertDesign(
            R.layout.coolx_float_dialog_root_alert, -1, 0, -1, 3000
    );

    static final AlertDesign FLOAT_ALERT_DESIGN_ERROR = new AlertDesign(
            R.layout.coolx_float_dialog_root_error, -1, 0, -1, 3000
    );

    static final AlertDesign CENTER_ALERT_DESIGN_CENTER = new AlertDesign(
            R.layout.coolx_center_dialog_root, 1, 1, 0, -1
    );

    static final AlertDesign CENTER_ALERT_DESIGN_CENTER_1 = new AlertDesign(
            R.layout.coolx_center_dialog_root, 1, 1, 0, -1
    );

    static final AlertDesign CENTER_ALERT_DESIGN_CENTER_2 = new AlertDesign(
            R.layout.coolx_center_dialog_root, 1, 0, 0, -1
    );

    static final AlertDesign CENTER_ALERT_DESIGN_CENTER_STACK = new AlertDesign(
            R.layout.coolx_center_dialog_root, 1, 0, 1, -1
    );

    /**
     * Start executing dialog show animation.
     *
     * @param mContext Context
     * @param parentPanel Dialog
     * @param dimView Dim
     */
    void executeShowAnimation(Context mContext, View parentPanel, View dimView) {
        if (alertType != 0) {
            executeCenterShowAnim(parentPanel, dimView);
        } else {
            executeBottomShowAnim(mContext, parentPanel, dimView);
        }
    }

    void executeDismissAnimation(Context mContext, View parentPanel, View dimView, AppCompatDialog appCompatDialog) {
        if (alertType != 0) {
            executeCenterDismissAnim(parentPanel, dimView, appCompatDialog);
        } else {
            executeBottomDismissAnim(mContext, parentPanel, dimView, appCompatDialog);
        }
    }

    /**
     * Start executing center dialog show animation.
     *
     * @param parentPanel Dialog
     * @param dimView Dim
     */
    void executeCenterShowAnim(View parentPanel, View dimView) {
        AlertAnimHelper.executeCenterShowAnim(parentPanel, dimView);
    }

    /**
     * Start executing bottom dialog show animation.
     *
     * @param parentPanel Dialog
     * @param dimView Dim
     */
    void executeBottomShowAnim(Context mContext, View parentPanel, View dimView) {
        AlertAnimHelper.executeBottomShowAnim(mContext, parentPanel, dimView);
    }

    void executeCenterDismissAnim(View parentPanel, View dimView, AppCompatDialog appCompatDialog) {
        AlertAnimHelper.executeCenterDismissAnim(parentPanel, dimView, appCompatDialog);
    }

    void executeBottomDismissAnim(Context mContext, View parentPanel, View dimView, AppCompatDialog appCompatDialog) {
        AlertAnimHelper.executeBottomDismissAnim(mContext, parentPanel, dimView, appCompatDialog);
    }

    void setupWindow(Window window, AppCompatDialog appCompatDialog) {
        window.setLayout(MATCH_PARENT, MATCH_PARENT);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setDimAmount(0);
        /*
         * TODO: need to check
         *     FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS: Window 负责系统 bar 的 background 绘制
         */
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS |
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        );
        /* Set the content will be always presented in the bang area */
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            Activity activity = ((AlertDialog) appCompatDialog).getAssociatedActivity();
            if (activity != null) {
                window.getAttributes().layoutInDisplayCutoutMode =
                        getCutoutMode(Configuration.ORIENTATION_PORTRAIT, activity.getWindow().getAttributes().layoutInDisplayCutoutMode);
            } else {
                window.getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
        }
        /* TODO: need ? */
        clearFitSystemWindow(window.getDecorView());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.getAttributes().setFitInsetsSides(0);
            Activity associatedActivity = ((AlertDialog) appCompatDialog).getAssociatedActivity();
            if (associatedActivity != null && (associatedActivity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    }

    /**
     * Bang area adaptation
     */
    int getCutoutMode(int orientation, int activityCutoutMode) {
        int cutoutMode;
        if (activityCutoutMode == WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                cutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
            } else {
                cutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
        } else {
            cutoutMode = activityCutoutMode;
        }
        return cutoutMode;
    }

    void clearFitSystemWindow(View view) {
        if (view != null) {
            view.setFitsSystemWindows(false);
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    clearFitSystemWindow(((ViewGroup) view).getChildAt(i));
                }
            }
        }
    }

    /**
     * 设置弹窗展示位置：底部或者居中
     */
    public void setupDialogPanel(Context context, View parentPanel) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) parentPanel.getLayoutParams();
        if (alertType == 1) {
            layoutParams.gravity = Gravity.CENTER;
            parentPanel.setClipToOutline(true);
        }
        if (alertType == 0) {
            layoutParams.gravity = Gravity.BOTTOM;
            if (BarUtils.hasNavBar(context)) {
                setupBottomLayoutMarginsWithNav(context, layoutParams);
            } else {
                setupBottomLayoutMarginsWithoutNav(context, layoutParams);
            }
            if (buttonStyle == 2) {
                setupBottomSpecialLayoutMarginsWithoutNav(context, layoutParams);
            }
        }
        layoutParams.width = MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        parentPanel.setLayoutParams(layoutParams);
    }

    /**
     * Set bottom dialog layout margin params when nav bar hide.
     * @param layoutParams the parentPanel layoutParams
     */
    private void setupBottomLayoutMarginsWithoutNav(Context context, FrameLayout.LayoutParams layoutParams) {
        layoutParams.setMargins(
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_start),
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_top),
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_end),
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_bottom_without_nav));
    }

    /**
     * A：特殊用途
     */
    private void setupBottomSpecialLayoutMarginsWithoutNav(Context context, FrameLayout.LayoutParams layoutParams) {
        layoutParams.setMargins(
                0,
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_top),
                0,
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_bottom_without_nav));
    }

    /**
     * Set bottom dialog layout margin params when nav bar show.
     * @param layoutParams the parentPanel layoutParams
     */
    private void setupBottomLayoutMarginsWithNav(Context context, FrameLayout.LayoutParams layoutParams) {
        layoutParams.setMargins(
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_start),
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_top),
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_end),
                context.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_container_margin_bottom_with_nav));
    }

    /**
     * Setup dialog button area, check button type: default or stack.
     */
    public void setupButtons(Context context, AlertController alertController, ViewGroup buttonPanel) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (alertType == 1) {
            if (buttonStyle != 0) {
                buttonPanel.addView(mInflater.inflate(R.layout.coolx_center_dialog_button_stack, null));
            } else {
                buttonPanel.addView(mInflater.inflate(R.layout.coolx_center_dialog_button_default, null));
                if (buttonTheme != 0) {
                    Button mButtonNegative = buttonPanel.findViewById(R.id.button2);
                    mButtonNegative.setTextColor(ResourcesCompat.getColorStateList(context.getResources(),
                            R.color.coolx_center_dialog_negative_button_text_color_red, null));
                }
            }
        } else {
            if (buttonStyle == 1) {
                buttonPanel.addView(mInflater.inflate(R.layout.coolx_alert_dialog_button_stack, null));
            } else if (buttonStyle == 2) {
                buttonPanel.addView(mInflater.inflate(R.layout.coolx_alert_dialog_button_special, null));
            } else {
                buttonPanel.addView(mInflater.inflate(R.layout.coolx_alert_dialog_button_default, null));
            }
        }
        alertController.setupButtons(buttonPanel);
    }

    void handleIMFlags(AlertController alertController) {
        View inputView = alertController.mWindow.findViewById(R.id.coolx_input_dialog_edit_text);
        if (inputView == null) {
            alertController.mWindow.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }
}
