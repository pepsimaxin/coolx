package coolx.appcompat.app;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;

import java.lang.ref.WeakReference;
import java.util.List;

import coolx.appcompat.R;
import coolx.appcompat.annotation.CAD;
import coolx.appcompat.core.SingleClickListener;
import coolx.appcompat.core.anim.AlertAnimHelper;
import coolx.appcompat.utils.KeyboardUtils;
import coolx.appcompat.widget.CircularLoadingView;
import coolx.appcompat.widget.DatePicker;
import coolx.appcompat.widget.TimePicker;

public class AlertController {
    private final Context mContext;
    final AppCompatDialog mDialog;
    final Window mWindow;

    private CharSequence mTitle;
    private CharSequence mMessage;
    private CharSequence mProgressPrompt;

    ListView mListView;
    private View mView;

    private int mViewLayoutResId;

    private int mViewSpacingLeft;
    private int mViewSpacingTop;
    private int mViewSpacingRight;
    private int mViewSpacingBottom;
    private boolean mViewSpacingSpecified = false;

    Button mButtonPositive;
    private CharSequence mButtonPositiveText;
    Message mButtonPositiveMessage;

    Button mButtonNegative;
    private CharSequence mButtonNegativeText;
    Message mButtonNegativeMessage;

    Button mButtonNeutral;
    private CharSequence mButtonNeutralText;
    Message mButtonNeutralMessage;

    NestedScrollView mScrollView;

    private int mIconId = 0;
    private Drawable mIcon;

    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mMessageView;
    private TextView mProgressPromptView;

    private ProgressBar mProgressBar;
    private TextView mProgressPercent;
    private Handler mProgressUpdateHandler;

    private View mCustomTitleView;

    ListAdapter mAdapter;

    int mCheckedItem = -1;

    private final int mAlertDialogLayout;
    private final int mButtonPanelSideLayout;
    int mListLayout;
    int mMultiChoiceItemLayout;
    int mSingleChoiceItemLayout;
    int mListItemLayout;

    private final boolean mShowTitle;

    private int mButtonPanelLayoutHint = AlertDialog.LAYOUT_HINT_NONE;

    private final AlertDesign mDesign;

    private CharSequence mLoadingTitle;
    private CharSequence mFloatMessage;

    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;

    private final WindowManager mWindowManager;
    private final Point mScreenRealSize;    // 存储屏幕实际尺寸

    private View mParentPanel;
    private View mDimBg;
    private TextView mFloatMessageView;

    private CircularLoadingView mCircularLoadingView;

    private int mPanelAndImeMargin;
    private boolean mInsetsAnimationPlayed = false;

    private DatePicker.OnDateSelectedListener mOnDateSelectedListener;
    private TimePicker.OnTimeSelectedListener mHMSOnTimeSelectedListener;
    private TimePicker.OnTimeSelectedListener mHMOnTimeSelectedListener;

    Handler mHandler;

    private final View.OnClickListener mButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Message m;
            if (v == mButtonPositive && mButtonPositiveMessage != null) {
                m = Message.obtain(mButtonPositiveMessage);
            } else if (v == mButtonNegative && mButtonNegativeMessage != null) {
                m = Message.obtain(mButtonNegativeMessage);
            } else if (v == mButtonNeutral && mButtonNeutralMessage != null) {
                m = Message.obtain(mButtonNeutralMessage);
            } else {
                m = null;
            }

            if (m != null) {
                m.sendToTarget();
            }

            // Post a message so we dismiss after the above handlers are executed
            mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG, mDialog)
                    .sendToTarget();
        }
    };

    private static final class ButtonHandler extends Handler {
        // Button clicks have Message.what as the BUTTON{1,2,3} constant
        private static final int MSG_DISMISS_DIALOG = 1;

        private final WeakReference<DialogInterface> mDialog;

        public ButtonHandler(Looper looper, DialogInterface dialog) {
            super(looper);
            mDialog = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                    break;

                case MSG_DISMISS_DIALOG:
                    ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    private static boolean shouldCenterSingleButton(Context context) {
        final TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(androidx.appcompat.R.attr.alertDialogCenterButtons, outValue, true);
        return outValue.data != 0;
    }

    @CAD
    public AlertController(Context context, AppCompatDialog di, Window window, String alertType) {
        // Special design for coolx
        mScreenRealSize = new Point();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDesign = AlertDesign.fromType(alertType);

        mContext = context;
        mDialog = di;
        mWindow = window;
        mHandler = new ButtonHandler(Looper.getMainLooper(), di);

        final TypedArray a = context.obtainStyledAttributes(null, R.styleable.AlertDialog,
                androidx.appcompat.R.attr.alertDialogStyle, 0);

        mAlertDialogLayout = a.getResourceId(R.styleable.AlertDialog_layout, 0);
        mButtonPanelSideLayout = a.getResourceId(R.styleable.AlertDialog_buttonPanelSideLayout, 0);

        mListLayout = a.getResourceId(R.styleable.AlertDialog_listLayout, 0);
        mMultiChoiceItemLayout = a.getResourceId(R.styleable.AlertDialog_multiChoiceItemLayout, 0);
        mSingleChoiceItemLayout = a
                .getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
        mListItemLayout = a.getResourceId(R.styleable.AlertDialog_listItemLayout, 0);
        mShowTitle = a.getBoolean(R.styleable.AlertDialog_showTitle, true);

        a.recycle();

        /* We use a custom title so never request a window title */
        di.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    static boolean canTextInput(View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }

        if (!(v instanceof ViewGroup)) {
            return false;
        }

        ViewGroup vg = (ViewGroup) v;
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            v = vg.getChildAt(i);
            if (canTextInput(v)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Special added from com.android.internal.app.AlertController
     * <p>
     * Use to transfer parameter from outside.
     */
    public void installContent(AlertParams params) {
        params.apply(this);
        installContent();
    }

    public void installContent() {
        mDialog.setContentView(selectContentView());
        mDesign.setupWindow(mWindow, mDialog);
        setupView();
    }

    private int selectContentView() {
        if (mDesign.overrideContentLayout != 0) {
            return mDesign.overrideContentLayout;
        }
        if (mButtonPanelSideLayout == 0) {
            return mAlertDialogLayout;
        }
        if (mButtonPanelLayoutHint == AlertDialog.LAYOUT_HINT_SIDE) {
            return mButtonPanelSideLayout;
        }
        return mAlertDialogLayout;
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    /**
     * @see AlertDialog.Builder#setCustomTitle(View)
     */
    public void setCustomTitle(View customTitleView) {
        mCustomTitleView = customTitleView;
    }

    public void setLoadingTitle(CharSequence title) {
        mLoadingTitle = title;
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    public void setProgressPrompt(CharSequence progressPrompt) {
        mProgressPrompt = progressPrompt;
        if (mProgressPromptView != null) {
            mProgressPromptView.setText(mProgressPrompt);
        }
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
        onProgressChanged();
    }

    private void onProgressChanged() {
        mProgressUpdateHandler.sendEmptyMessage(0);
    }


    public void setFloatMessage(CharSequence floatMessage) {
        mFloatMessage = floatMessage;
        if (mFloatMessageView != null) {
            mFloatMessageView.setText(mFloatMessage);
        }
    }

    /**
     * Set the view resource to display in the dialog.
     */
    public void setView(int layoutResId) {
        mView = null;
        mViewLayoutResId = layoutResId;
        mViewSpacingSpecified = false;
    }

    /**
     * Set the view to display in the dialog.
     */
    public void setView(View view) {
        mView = view;
        mViewLayoutResId = 0;
        mViewSpacingSpecified = false;
    }

    /**
     * Set the view to display in the dialog along with the spacing around that view
     */
    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight,
                        int viewSpacingBottom) {
        mView = view;
        mViewLayoutResId = 0;
        mViewSpacingSpecified = true;
        mViewSpacingLeft = viewSpacingLeft;
        mViewSpacingTop = viewSpacingTop;
        mViewSpacingRight = viewSpacingRight;
        mViewSpacingBottom = viewSpacingBottom;
    }

    private void setOnDateSelectedListener(DatePicker.OnDateSelectedListener onDateSelectedListener) {
        mOnDateSelectedListener = onDateSelectedListener;
    }

    private void setHMSOnTimeSelectedListener(TimePicker.OnTimeSelectedListener hmsOnTimeSelectedListener) {
        mHMSOnTimeSelectedListener = hmsOnTimeSelectedListener;
    }

    private void setHMOnTimeSelectedListener(TimePicker.OnTimeSelectedListener hmOnTimeSelectedListener) {
        mHMOnTimeSelectedListener = hmOnTimeSelectedListener;
    }

    /**
     * Sets a hint for the best button panel layout.
     */
    public void setButtonPanelLayoutHint(int layoutHint) {
        mButtonPanelLayoutHint = layoutHint;
    }

    /**
     * Sets an icon, a click listener or a message to be sent when the button is clicked.
     * You only need to pass one of {@code icon}, {@code listener} or {@code msg}.
     *
     * @param whichButton Which button, can be one of
     *                    {@link DialogInterface#BUTTON_POSITIVE},
     *                    {@link DialogInterface#BUTTON_NEGATIVE}, or
     *                    {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text        The text to display in positive button.
     * @param listener    The {@link DialogInterface.OnClickListener} to use.
     * @param msg         The {@link Message} to be sent when clicked.
     *
     */
    public void setButton(int whichButton, CharSequence text,
                          DialogInterface.OnClickListener listener, Message msg) {

        if (msg == null && listener != null) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }

        switch (whichButton) {

            case DialogInterface.BUTTON_POSITIVE:
                mButtonPositiveText = text;
                mButtonPositiveMessage = msg;
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                mButtonNegativeText = text;
                mButtonNegativeMessage = msg;
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                mButtonNeutralText = text;
                mButtonNeutralMessage = msg;
                break;

            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    /**
     * Specifies the icon to display next to the alert title.
     *
     * @param resId the resource identifier of the drawable to use as the icon,
     *              or 0 for no icon
     */
    public void setIcon(int resId) {
        mIcon = null;
        mIconId = resId;

        if (mIconView != null) {
            if (resId != 0) {
                mIconView.setVisibility(View.VISIBLE);
                mIconView.setImageResource(mIconId);
            } else {
                mIconView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Specifies the icon to display next to the alert title.
     *
     * @param icon the drawable to use as the icon or null for no icon
     */
    public void setIcon(Drawable icon) {
        mIcon = icon;
        mIconId = 0;

        if (mIconView != null) {
            if (icon != null) {
                mIconView.setVisibility(View.VISIBLE);
                mIconView.setImageDrawable(icon);
            } else {
                mIconView.setVisibility(View.GONE);
            }
        }
    }

    private void setCancelable(boolean cancelable) {
        this.mCancelable = cancelable;
    }

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.mCanceledOnTouchOutside = canceledOnTouchOutside;
    }

    private boolean isCancelable() {
        return mCancelable;
    }

    private boolean isCanceledOnTouchOutside() {
        return mCanceledOnTouchOutside;
    }

    /**
     * @param attrId the attributeId of the theme-specific drawable
     *               to resolve the resourceId for.
     *
     * @return resId the resourceId of the theme-specific drawable
     */
    public int getIconAttributeResId(int attrId) {
        TypedValue out = new TypedValue();
        mContext.getTheme().resolveAttribute(attrId, out, true);
        return out.resourceId;
    }

    public ListView getListView() {
        return mListView;
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    public Button getButton(int whichButton) {
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                return mButtonPositive;
            case DialogInterface.BUTTON_NEGATIVE:
                return mButtonNegative;
            case DialogInterface.BUTTON_NEUTRAL:
                return mButtonNeutral;
            default:
                return null;
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mScrollView != null && mScrollView.executeKeyEvent(event);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mScrollView != null && mScrollView.executeKeyEvent(event);
    }

    /**
     * Resolves whether a custom or default panel should be used. Removes the
     * default panel if a custom panel should be used. If the resolved panel is
     * a view stub, inflates before returning.
     *
     * @param customPanel the custom panel
     * @param defaultPanel the default panel
     * @return the panel to use
     */
    @Nullable
    private ViewGroup resolvePanel(@Nullable View customPanel, @Nullable View defaultPanel) {
        if (customPanel == null) {
            // Inflate the default panel, if needed.
            if (defaultPanel instanceof ViewStub) {
                defaultPanel = ((ViewStub) defaultPanel).inflate();
            }

            return (ViewGroup) defaultPanel;
        }

        // Remove the default panel entirely.
        if (defaultPanel != null) {
            final ViewParent parent = defaultPanel.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(defaultPanel);
            }
        }

        // Inflate the custom panel, if needed.
        if (customPanel instanceof ViewStub) {
            customPanel = ((ViewStub) customPanel).inflate();
        }

        return (ViewGroup) customPanel;
    }

    void setupView() {
        mParentPanel = mWindow.findViewById(R.id.parentPanel);
        // 弹窗三大核心区域：1. 头部标题区域，2. 中间内容区域；3. 底部 Button 区域
        final View defaultTopPanel = mParentPanel.findViewById(R.id.topPanel);
        final View defaultContentPanel = mParentPanel.findViewById(R.id.contentPanel);
        final View defaultButtonPanel = mParentPanel.findViewById(R.id.buttonPanel);

        mDesign.setupDialogPanel(mContext, mParentPanel);

        // Install custom content before setting up the title or buttons so
        // that we can handle panel overrides.
        // 4. 弹窗自定义区域
        final ViewGroup customPanel = (ViewGroup) mParentPanel.findViewById(R.id.customPanel);
        setupCustomContent(customPanel);

        final View customTopPanel = customPanel.findViewById(R.id.topPanel);
        final View customContentPanel = customPanel.findViewById(R.id.contentPanel);
        final View customButtonPanel = customPanel.findViewById(R.id.buttonPanel);

        // Resolve the correct panels and remove the defaults, if needed.
        final ViewGroup topPanel = resolvePanel(customTopPanel, defaultTopPanel);
        final ViewGroup contentPanel = resolvePanel(customContentPanel, defaultContentPanel);
        final ViewGroup buttonPanel = resolvePanel(customButtonPanel, defaultButtonPanel);

        assert contentPanel != null;
        setupContent(contentPanel);
        mDesign.setupButtons(mContext, this, buttonPanel);
        setupTitle(topPanel);

        final boolean hasCustomPanel = customPanel.getVisibility() != View.GONE;
        final boolean hasTopPanel = topPanel != null
                && topPanel.getVisibility() != View.GONE;
        assert buttonPanel != null;
        final boolean hasButtonPanel = buttonPanel.getVisibility() != View.GONE;

        if (hasTopPanel) {
            // Only clip scrolling content to padding if we have a title.
            if (mScrollView != null) {
                mScrollView.setClipToPadding(true);
            }

            // Setup ListView vertical padding.
            if (mListView != null) {
                contentPanel.setPadding(
                        contentPanel.getPaddingLeft(),
                        mContext.getResources().getDimensionPixelOffset(R.dimen.coolx_alert_dialog_listview_margin_top),
                        contentPanel.getPaddingRight(),
                        mContext.getResources().getDimensionPixelOffset(R.dimen.coolx_alert_dialog_listview_margin_bottom));
            }
        }

        // Update scroll indicators as needed.
        if (!hasCustomPanel) {
            final View content = mListView != null ? mListView : mScrollView;
            if (content != null) {
                final int indicators = (hasTopPanel ? View.SCROLL_INDICATOR_TOP : 0)
                        | (hasButtonPanel ? View.SCROLL_INDICATOR_BOTTOM : 0);
                content.setScrollIndicators(indicators,
                        View.SCROLL_INDICATOR_TOP | View.SCROLL_INDICATOR_BOTTOM);
            }
        }

        final ListView listView = mListView;
        if (listView != null && mAdapter != null) {
            listView.setAdapter(mAdapter);
            final int checkedItem = mCheckedItem;
            if (checkedItem > -1) {
                listView.setItemChecked(checkedItem, true);
                listView.setSelection(checkedItem);
            }
        }

        setupWindowInsetsAnimation();
        setupDim();
    }

    private void setupCustomContent(ViewGroup customPanel) {
        final View customView;
        if (mView != null) {
            customView = mView;
        } else if (mViewLayoutResId != 0) {
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            customView = inflater.inflate(mViewLayoutResId, customPanel, false);
        } else {
            customView = null;
        }

        final boolean hasCustomView = customView != null;
        if (!hasCustomView || !canTextInput(customView)) {
            mDesign.handleIMFlags(this);
        }

        if (hasCustomView) {
            final FrameLayout custom = (FrameLayout) mWindow.findViewById(R.id.custom);
            custom.addView(customView, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            if (mDesign == AlertDesign.BOTTOM_SPECIAL_DESIGN) {
                // special use for special dialog, so you don't care...
                custom.setPadding(0,
                        mContext.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_custom_layout_padding_top_special),
                        0,
                        mContext.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_custom_layout_padding_bottom));
            } else {
                custom.setPadding(0,
                        mContext.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_custom_layout_padding_top),
                        0,
                        mContext.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_custom_layout_padding_bottom));
            }
            if (mViewSpacingSpecified) {
                custom.setPadding(
                        mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
            }

            if (mListView != null) {
                ((LinearLayoutCompat.LayoutParams) customPanel.getLayoutParams()).weight = 0;
            }
        } else {
            customPanel.setVisibility(View.GONE);
        }
    }

    private void setupTitle(ViewGroup topPanel) {
        if (mCustomTitleView != null) {
            // Add the custom title view directly to the topPanel layout
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            topPanel.addView(mCustomTitleView, 0, lp);

            // Hide the title template
            View titleTemplate = mWindow.findViewById(R.id.title_template);
            titleTemplate.setVisibility(View.GONE);
        } else {
            mIconView = (ImageView) mWindow.findViewById(android.R.id.icon);

            final boolean hasTextTitle = !TextUtils.isEmpty(mTitle);
            if (hasTextTitle && mShowTitle) {
                // Display the title if a title is supplied, else hide it.
                mTitleView = (TextView) mWindow.findViewById(R.id.alertTitle);
                mTitleView.setText(mTitle);

                // Do this last so that if the user has supplied any icons we
                // use them instead of the default ones. If the user has
                // specified 0 then make it disappear.
                if (mIconId != 0) {
                    mIconView.setImageResource(mIconId);
                } else if (mIcon != null) {
                    mIconView.setImageDrawable(mIcon);
                } else {
                    // Apply the padding from the icon to ensure the title is
                    // aligned correctly.
                    mTitleView.setPadding(mIconView.getPaddingLeft(),
                            mIconView.getPaddingTop(),
                            mIconView.getPaddingRight(),
                            mIconView.getPaddingBottom());
                    mIconView.setVisibility(View.GONE);
                }
            } else {
                // Hide the title template
                final View titleTemplate = mWindow.findViewById(R.id.title_template);
                titleTemplate.setVisibility(View.GONE);
                mIconView.setVisibility(View.GONE);
                topPanel.setVisibility(View.GONE);
                mMessageView.setPadding(0,
                        mContext.getResources().getDimensionPixelOffset(R.dimen.coolx_center_dialog_message_padding_top_without_title),
                        0,
                        mContext.getResources().getDimensionPixelOffset(R.dimen.coolx_center_dialog_message_padding_bottom_without_title));
            }
        }
    }

    private void setupContent(ViewGroup contentPanel) {
        // 内容区域 ScrollView 处理
        mScrollView = mWindow.findViewById(R.id.scrollView);
        mScrollView.setFocusable(false);
        mScrollView.setNestedScrollingEnabled(false);

        // Special case for users that only want to display a String
        mMessageView = (TextView) contentPanel.findViewById(android.R.id.message);
        if (mMessageView == null) {
            return;
        }

        if (mMessage != null) {
            mMessageView.setText(mMessage);
        } else {
            mMessageView.setVisibility(View.GONE);
            mScrollView.removeView(mMessageView);

            if (mListView != null) {
                final ViewGroup scrollParent = (ViewGroup) mScrollView.getParent();
                final int childIndex = scrollParent.indexOfChild(mScrollView);
                scrollParent.removeViewAt(childIndex);
                scrollParent.addView(mListView, childIndex,
                        new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            } else {
                contentPanel.setVisibility(View.GONE);
            }
        }

        // ProgressBar
        mProgressBar = mWindow.findViewById(R.id.coolxProgressBar);
        if (mProgressBar != null) {
            mProgressUpdateHandler = new Handler(msg -> {
                updateProgressPercent();
                return true;    // 返回 true 表示消息已处理
            });
        }

        mProgressPromptView = mWindow.findViewById(R.id.progressPrompt);
        if (mProgressPromptView != null) {
            mProgressPromptView.setText(mProgressPrompt);
        }

        mProgressPercent = mWindow.findViewById(R.id.progressPercent);

        // DatePicker
        DatePicker datePickerView = mWindow.findViewById(R.id.coolx_date_picker_view);
        if (datePickerView != null) {
            datePickerView.setOnDateSelectedListener(mOnDateSelectedListener);
        }

        // TimePicker_HMS
        TimePicker hMSTimePickerView = mWindow.findViewById(R.id.coolx_hms_time_picker_view);
        if (hMSTimePickerView != null) {
            hMSTimePickerView.setOnTimeSelectedListener(mHMSOnTimeSelectedListener);
        }

        // TimePicker_HM
        TimePicker hMTimePickerView = mWindow.findViewById(R.id.coolx_hm_time_picker_view);
        if (hMTimePickerView != null) {
            hMTimePickerView.setOnTimeSelectedListener(mHMOnTimeSelectedListener);
        }

        // Loading View
        if (!TextUtils.isEmpty(mLoadingTitle)) {
            AppCompatTextView loadingTitleView = (AppCompatTextView) mWindow.findViewById(R.id.loadingMessage);
            loadingTitleView.setText(mLoadingTitle);
            mCircularLoadingView = (CircularLoadingView) mWindow.findViewById(R.id.loadingView);
            mCircularLoadingView.doAnimation();
        }

        // Float View
        mFloatMessageView = mWindow.findViewById(R.id.coolx_float_dialog_message);
        if (mFloatMessageView != null) {
            if (!TextUtils.isEmpty(mFloatMessage)) {
                mFloatMessageView.setText(mFloatMessage);
            }
            if(mDesign.autoDismissDelay > 0) {
                mDialog.setOnShowListener(floatDialog ->
                        new Handler().postDelayed(floatDialog::dismiss, mDesign.autoDismissDelay));
            }
        }

        // Input View
        View inputView = mWindow.findViewById(R.id.coolx_input_dialog_edit_text);
        if (inputView != null) {
            KeyboardUtils.showSoftInput(inputView);
        }
    }

    /**
     * Update ProgressBar Percent.
     */
    private void updateProgressPercent() {
        int progress = mProgressBar.getProgress();
        int max = mProgressBar.getMax();
        double percent = ((double) progress / (double) max) * 100;
        @SuppressLint("DefaultLocale") String percentText = String.format("%.0f%%", percent); // 不包含小数部分的格式
        mProgressPercent.setText(percentText);
    }

    /**
     * After AlertDesign determines the button layout, start the setting process here.
     * @param buttonPanel buttons area
     */
    void setupButtons(ViewGroup buttonPanel) {
        int BIT_BUTTON_POSITIVE = 1;
        int BIT_BUTTON_NEGATIVE = 2;
        int BIT_BUTTON_NEUTRAL = 4;
        int whichButtons = 0;

        View mVerticalDividerLine = buttonPanel.findViewById(R.id.vertical_divider_line);
        View mDivider = buttonPanel.findViewById(R.id.divider);

        mButtonPositive = (Button) buttonPanel.findViewById(R.id.button1);
        mButtonPositive.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mButtonPositiveText)) {
            mButtonPositive.setVisibility(View.GONE);
            if (mVerticalDividerLine != null) mVerticalDividerLine.setVisibility(View.GONE);
            if (mDivider != null) mDivider.setVisibility(View.GONE);
        } else {
            mButtonPositive.setText(mButtonPositiveText);
            mButtonPositive.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
        }

        mButtonNegative = buttonPanel.findViewById(R.id.button2);
        mButtonNegative.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mButtonNegativeText)) {
            mButtonNegative.setVisibility(View.GONE);
            if (mVerticalDividerLine != null) mVerticalDividerLine.setVisibility(View.GONE);
            if (mDivider != null) mDivider.setVisibility(View.GONE);
        } else {
            mButtonNegative.setText(mButtonNegativeText);
            mButtonNegative.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
        }

        mButtonNeutral = (Button) buttonPanel.findViewById(R.id.button3);
        mButtonNeutral.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mButtonNeutralText)) {
            mButtonNeutral.setVisibility(View.GONE);
        } else {
            mButtonNeutral.setText(mButtonNeutralText);
            mButtonNeutral.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEUTRAL;
        }

        if (shouldCenterSingleButton(mContext)) {
            /*
             * If we only have 1 button it should be centered on the layout and
             * expand to fill 50% of the available space.
             */
            if (whichButtons == BIT_BUTTON_POSITIVE) {
                centerButton(mButtonPositive);
            } else if (whichButtons == BIT_BUTTON_NEGATIVE) {
                centerButton(mButtonNegative);
            } else if (whichButtons == BIT_BUTTON_NEUTRAL) {
                centerButton(mButtonNeutral);
            }
        }

        final boolean hasButtons = whichButtons != 0;
        if (!hasButtons) {
            buttonPanel.setVisibility(View.GONE);
        }
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.weight = 0.5f;
        button.setLayoutParams(params);
    }

    private void setupDim() {
        mDimBg = mWindow.findViewById(R.id.dialog_dim_bg);
        // Dialog 蒙层交互操作
        mDimBg.setOnClickListener(new SingleClickListener() {
            @Override
            protected void singleClick(View v) {
                if (isCancelable() && isCanceledOnTouchOutside()) {
                    if (mParentPanel != null) KeyboardUtils.hideSoftInput(mParentPanel);
                    mDialog.cancel();
                }
            }
        });
    }

    public static class AlertParams {
        public final Context mContext;
        public final LayoutInflater mInflater;

        public String mDialogType = "";

        public int mIconAttrId = 0;
        public int mIconId = 0;
        public Drawable mIcon;
        public CharSequence mTitle;
        public CharSequence mMessage;
        public CharSequence mProgressPrompt;

        public CharSequence mLoadingTitle;
        public View mCustomTitleView;

        public CharSequence mPositiveButtonText;
        public CharSequence mNegativeButtonText;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public DialogInterface.OnClickListener mNeutralButtonListener;

        public boolean mCancelable;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public CharSequence[] mItems;
        public ListAdapter mAdapter;
        public DialogInterface.OnClickListener mOnClickListener;

        public DatePicker.OnDateSelectedListener mOnDateSelectedListener;
        public TimePicker.OnTimeSelectedListener mHMSOnTimeSelectedListener;
        public TimePicker.OnTimeSelectedListener mHMOnTimeSelectedListener;

        public int mViewLayoutResId;
        public View mView;
        public int mViewSpacingLeft;
        public int mViewSpacingTop;
        public int mViewSpacingRight;
        public int mViewSpacingBottom;
        public boolean mViewSpacingSpecified = false;
        public boolean[] mCheckedItems;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public int mCheckedItem = -1;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        public Cursor mCursor;
        public String mLabelColumn;
        public String mIsCheckedColumn;
        public boolean mForceInverseBackground;
        public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public boolean mRecycleOnMeasure = true;
        public CharSequence mFloatMessage;

        /**
         * Interface definition for a callback to be invoked before the ListView
         * will be bound to an adapter.
         */
        public interface OnPrepareListViewListener {

            /**
             * Called before the ListView is bound to an adapter.
             * @param listView The ListView that will be shown in the dialog.
             */
            void onPrepareListView(ListView listView);
        }

        public AlertParams(Context context) {
            mContext = context;
            mCancelable = true;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // 初始化数据
        public void apply(AlertController dialog) {
            if (mCustomTitleView != null) {
                dialog.setCustomTitle(mCustomTitleView);
            } else {
                if (mTitle != null) {
                    dialog.setTitle(mTitle);
                }
                if (mLoadingTitle != null) {
                    dialog.setLoadingTitle(mLoadingTitle);
                }
                if (mFloatMessage != null) {
                    dialog.setFloatMessage(mFloatMessage);
                }
                if (mIcon != null) {
                    dialog.setIcon(mIcon);
                }
                if (mIconId != 0) {
                    dialog.setIcon(mIconId);
                }
                if (mIconAttrId != 0) {
                    dialog.setIcon(dialog.getIconAttributeResId(mIconAttrId));
                }
            }
            if (mMessage != null) {
                dialog.setMessage(mMessage);
            }
            if (mProgressPrompt != null) {
                dialog.setProgressPrompt(mProgressPrompt);
            }
            if (mPositiveButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, mPositiveButtonText,
                        mPositiveButtonListener, null);
            }
            if (mNegativeButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, mNegativeButtonText,
                        mNegativeButtonListener, null);
            }
            if (mNeutralButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, mNeutralButtonText,
                        mNeutralButtonListener, null);
            }
            // 如果设置了 mItems 数据，表示是单选或者多选列表，则创建一个 ListView
            if ((mItems != null) || (mCursor != null) || (mAdapter != null)) {
                createListView(dialog);
            }
            // 将 mView 设置给 Dialog
            if (mView != null) {
                if (mViewSpacingSpecified) {
                    dialog.setView(mView, mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight,
                            mViewSpacingBottom);
                } else {
                    dialog.setView(mView);
                }
            } else if (mViewLayoutResId != 0) {
                dialog.setView(mViewLayoutResId);
            }

            if (mOnDateSelectedListener != null) {
                dialog.setOnDateSelectedListener(mOnDateSelectedListener);
            }
            if (mHMSOnTimeSelectedListener != null) {
                dialog.setHMSOnTimeSelectedListener(mHMSOnTimeSelectedListener);
            }
            if (mHMOnTimeSelectedListener != null) {
                dialog.setHMOnTimeSelectedListener(mHMOnTimeSelectedListener);
            }

            dialog.setCancelable(mCancelable);
        }

        private void createListView(final AlertController dialog) {
            final ListView listView =
                    (ListView) mInflater.inflate(dialog.mListLayout, null);
            final ListAdapter adapter;

            if (mIsMultiChoice) {
                if (mCursor == null) {
                    adapter = new ArrayAdapter<CharSequence>(
                            mContext, dialog.mMultiChoiceItemLayout, android.R.id.text1, mItems) {
                        @NonNull
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            if (mCheckedItems != null) {
                                boolean isItemChecked = mCheckedItems[position];
                                if (isItemChecked) {
                                    listView.setItemChecked(position, true);
                                }
                            }
                            return view;
                        }
                    };
                } else {
                    adapter = new CursorAdapter(mContext, mCursor, false) {
                        private final int mLabelIndex;
                        private final int mIsCheckedIndex;

                        {
                            final Cursor cursor = getCursor();
                            mLabelIndex = cursor.getColumnIndexOrThrow(mLabelColumn);
                            mIsCheckedIndex = cursor.getColumnIndexOrThrow(mIsCheckedColumn);
                        }

                        @Override
                        public void bindView(View view, Context context, Cursor cursor) {
                            CheckedTextView text = (CheckedTextView) view.findViewById(
                                    android.R.id.text1);
                            text.setText(cursor.getString(mLabelIndex));
                            listView.setItemChecked(cursor.getPosition(),
                                    cursor.getInt(mIsCheckedIndex) == 1);
                        }

                        @Override
                        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                            return mInflater.inflate(dialog.mMultiChoiceItemLayout,
                                    parent, false);
                        }

                    };
                }
            } else {
                final int layout;
                if (mIsSingleChoice) {
                    layout = dialog.mSingleChoiceItemLayout;
                } else {
                    layout = dialog.mListItemLayout;
                }

                if (mCursor != null) {
                    adapter = new SimpleCursorAdapter(mContext, layout, mCursor,
                            new String[] { mLabelColumn }, new int[] { android.R.id.text1 }, FLAG_REGISTER_CONTENT_OBSERVER);
                } else if (mAdapter != null) {
                    adapter = mAdapter;
                } else {
                    adapter = new CheckedItemAdapter(mContext, layout, android.R.id.text1, mItems);
                }
            }

            if (mOnPrepareListViewListener != null) {
                mOnPrepareListViewListener.onPrepareListView(listView);
            }

            /* Don't directly set the adapter on the ListView as we might
             * want to add a footer to the ListView later.
             */
            dialog.mAdapter = adapter;
            dialog.mCheckedItem = mCheckedItem;

            if (mOnClickListener != null) {
                listView.setOnItemClickListener((parent, v, position, id) -> {
                    mOnClickListener.onClick(dialog.mDialog, position);
                    if (!mIsSingleChoice) {
                        dialog.mDialog.dismiss();
                    }
                });
            } else if (mOnCheckboxClickListener != null) {
                listView.setOnItemClickListener((parent, v, position, id) -> {
                    if (mCheckedItems != null) {
                        mCheckedItems[position] = listView.isItemChecked(position);
                    }
                    mOnCheckboxClickListener.onClick(
                            dialog.mDialog, position, listView.isItemChecked(position));
                });
            }

            // Attach a given OnItemSelectedListener to the ListView
            if (mOnItemSelectedListener != null) {
                listView.setOnItemSelectedListener(mOnItemSelectedListener);
            }

            if (mIsSingleChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            } else if (mIsMultiChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
            dialog.mListView = listView;
        }
    }

    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
        public CheckedItemAdapter(Context context, int resource, int textViewResourceId,
                                  CharSequence[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public void onStart() {
        mDesign.executeShowAnimation(mContext, mParentPanel, mDimBg);
    }

    public void onStop() {
    }

    public void dismiss() {
        if (!TextUtils.isEmpty(mLoadingTitle)) {
            mCircularLoadingView.stopAnim();
        }

        if (mParentPanel == null) return;

        if (mParentPanel.isAttachedToWindow()) {
            checkAndClearFocus();
            mDesign.executeDismissAnimation(mContext, mParentPanel, mDimBg, mDialog);
        } else {
            // dismiss the dialog when dialog not attach to window
            try {
                ((AlertDialog) mDialog).realDismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupWindowInsetsAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (mWindow.getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) {
                mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            }

            mWindow.getDecorView().setWindowInsetsAnimationCallback(
                    new WindowInsetsAnimation.Callback(WindowInsetsAnimation.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE) {
                @NonNull
                @Override
                public WindowInsets onProgress(@NonNull WindowInsets windowInsets, @NonNull List<WindowInsetsAnimation> runningAnimations) {
                    if (windowInsets.isVisible(WindowInsets.Type.ime())) {
                        Insets typesInset = windowInsets.getInsets(WindowInsets.Type.ime());
                        int translationY = typesInset.bottom - mPanelAndImeMargin;
                        if (translationY < 0) {
                            translationY = 0;
                        }
                        translateDialogPosition(-translationY);
                    }
                    return windowInsets;
                }

                @NonNull
                @Override
                public WindowInsetsAnimation.Bounds onStart(@NonNull WindowInsetsAnimation animation, @NonNull WindowInsetsAnimation.Bounds bounds) {
                    mPanelAndImeMargin = (int) (spaceBetweenDialogAndIME() + mParentPanel.getTranslationY());
                    if (mPanelAndImeMargin <= 0) {
                        mPanelAndImeMargin = 0;
                    }
                    return super.onStart(animation, bounds);
                }

                @Override
                public void onEnd(@NonNull WindowInsetsAnimation animation) {
                    super.onEnd(animation);
                    mInsetsAnimationPlayed = true;
                    WindowInsets insets = mWindow.getDecorView().getRootWindowInsets();
                    if (insets != null) {
                        Insets imeInset = insets.getInsets(WindowInsets.Type.ime());
                        if (imeInset.bottom <= 0 && mParentPanel.getTranslationY() < 0) {
                            translateDialogPosition(0);
                        }
                    }
                }

                @Override
                public void onPrepare(@NonNull WindowInsetsAnimation animation) {
                    super.onPrepare(animation);
                    AlertAnimHelper.cancelAnimator();
                    mInsetsAnimationPlayed = false;
                }
            });

            mWindow.getDecorView().setOnApplyWindowInsetsListener((v, insets) -> {
                v.post(() -> checkTranslateDialogPanel(insets));
                return WindowInsets.CONSUMED;
            });
        }
    }

    private void checkTranslateDialogPanel(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && mInsetsAnimationPlayed) {
            Insets imeInset = insets.getInsets(WindowInsets.Type.ime());
            if (imeInset.bottom > 0) {
                mPanelAndImeMargin = (int) (spaceBetweenDialogAndIME() + mParentPanel.getTranslationY());
                if (mPanelAndImeMargin <= 0) {
                    mPanelAndImeMargin = 0;
                    translateDialogPosition(0);
                    return;
                }
                // 手机上横屏弹窗距离屏幕底部高度 < 键盘高度，将弹窗上移
                if (mPanelAndImeMargin < imeInset.bottom) {
                    translateDialogPosition(mPanelAndImeMargin - imeInset.bottom);
                } else {
                    translateDialogPosition(0);
                }
            } else {
                // 键盘未弹出时，translationY 置为 0
                if (mParentPanel.getTranslationY() < 0) {
                    translateDialogPosition(0);
                }
            }
        }
    }

    private int spaceBetweenDialogAndIME() {
        int[] location = new int[2];
        // 获取 mParentPanel 坐标
        mParentPanel.getLocationOnScreen(location);
        // 获取屏幕实际尺寸
        mWindowManager.getDefaultDisplay().getRealSize(mScreenRealSize);
        int extraImeMargin = mContext.getResources().getDimensionPixelSize(R.dimen.coolx_alert_dialog_ime_margin);
        if (mDesign == AlertDesign.BOTTOM_SPECIAL_DESIGN) {    // no space between dialog and IME
            return mScreenRealSize.y - (location[1] + mParentPanel.getHeight());
        }
        return mScreenRealSize.y - (location[1] + mParentPanel.getHeight()) - extraImeMargin;
    }

    /**
     * 执行 Dialog 移动操作
     */
    private void translateDialogPosition(int y) {
        mParentPanel.setTranslationY(y);
    }

    private void checkAndClearFocus() {
        View focusView = mWindow.getCurrentFocus();
        if (focusView != null) {
            focusView.clearFocus();
            KeyboardUtils.hideSoftInput(mParentPanel);
        }
    }
}
