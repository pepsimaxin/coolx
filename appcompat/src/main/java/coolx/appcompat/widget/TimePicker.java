package coolx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import coolx.appcompat.R;
import coolx.appcompat.core.picker.HourPicker;
import coolx.appcompat.core.picker.MinutePicker;

/**
 * Description: TimePicker
 */
public class TimePicker extends LinearLayout implements
        HourPicker.OnHourSelectedListener, MinutePicker.OnMinuteSelectedListener {

    private HourPicker mHourPicker;
    private MinutePicker mMinutePicker;
    private MinutePicker mSecondPicker;
    private OnTimeSelectedListener mOnTimeSelectedListener;

    private String timePickerType;

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.coolx_special_time_picker, this);
        initChildPickers();
        initAttrs(context, attrs);
        mHourPicker.setBackground(getBackground());
        mMinutePicker.setBackground(getBackground());
        mSecondPicker.setBackground(getBackground());
        setIndicatorText(
                getResources().getString(R.string.coolx_time_picker_hour),
                getResources().getString(R.string.coolx_time_picker_minute),
                getResources().getString(R.string.coolx_time_picker_second)

        );
        if ("hs".equals(timePickerType)) {
            mSecondPicker.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHourSelected(int hour) {
        onTimeSelected();
    }

    @Override
    public void onMinuteSelected(int minute) {
        onTimeSelected();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimePicker);
        boolean isCyclic = a.getBoolean(R.styleable.TimePicker_wheelCyclic, true);
        switch (a.getInt(R.styleable.TimePicker_timePickerType, 0)) {
            case 0x10:
                timePickerType = "hms";
                break;
            case 0x20:
                timePickerType = "hs";
                break;
            default:
                break;
        }
        a.recycle();

        setCyclic(isCyclic);
    }

    private void initChildPickers() {
        mHourPicker = findViewById(R.id.hourPicker_layout_time);
        mHourPicker.setOnHourSelectedListener(this);
        mMinutePicker = findViewById(R.id.minutePicker_layout_time);
        mMinutePicker.setOnMinuteSelectedListener(this);
        mSecondPicker = findViewById(R.id.secondPicker_layout_time);
        mSecondPicker.setOnMinuteSelectedListener(this);
    }

    private void onTimeSelected() {
        if (mOnTimeSelectedListener != null) {
            mOnTimeSelectedListener.onTimeSelected(getHour(), getMinute(), getSecond());
        }
    }

    public void setTime(int hour, int minute) {
        setTime(hour, minute, 0, true);
    }

    public void setTime(int hour, int minute, int second) {
        setTime(hour, minute, second, true);
    }

    public void setTime(int hour, int minute, int second, boolean smoothScroll) {
        mHourPicker.setSelectedHour(hour, smoothScroll);
        mMinutePicker.setSelectedMinute(minute, smoothScroll);
        mSecondPicker.setSelectedMinute(second, smoothScroll);
    }

    public void setIntervalHour(int minHour, int maxHour) {
        mHourPicker.setIntervalHour(minHour, maxHour);
    }

    public void setIntervalMinute(int minMinute, int maxMinute) {
        mMinutePicker.setIntervalMinute(minMinute, maxMinute);
    }

    private int getHour() {
        return mHourPicker.getSelectedHour();
    }

    private int getMinute() {
        return mMinutePicker.getSelectedMinute();
    }

    private int getSecond() {
        return mSecondPicker.getSelectedMinute();
    }

    public void setCyclic(boolean cyclic) {
        mHourPicker.setCyclic(cyclic);
        mMinutePicker.setCyclic(cyclic);
        mSecondPicker.setCyclic(cyclic);
    }

    private void setIndicatorText(String hourText, String minuteText, String secondText) {
        mHourPicker.setIndicatorText(hourText);
        mMinutePicker.setIndicatorText(minuteText);
        mSecondPicker.setIndicatorText(secondText);
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
        mOnTimeSelectedListener = onTimeSelectedListener;
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int hour, int minute, int second);
    }
}
