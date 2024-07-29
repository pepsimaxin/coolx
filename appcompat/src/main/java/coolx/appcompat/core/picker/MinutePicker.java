package coolx.appcompat.core.picker;

import android.content.Context;
import android.util.AttributeSet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import coolx.appcompat.core.WheelPicker;

/**
 * Description: MinutePicker
 */
public class MinutePicker extends WheelPicker<Integer> {
    private int mMinMinute, mMaxMinute;
    private int mSelectedMinute;

    private OnMinuteSelectedListener mOnMinuteSelectedListener;

    public MinutePicker(Context context) {
        this(context, null);
    }

    public MinutePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MinutePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
        setItemMaximumWidthText("00");
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);
        setDataFormat(numberFormat);
        updateMinute();
        setSelectedMinute(mSelectedMinute, false);
        setOnWheelChangeListener(new OnWheelChangeListener<Integer>() {
            @Override
            public void onWheelSelected(Integer item, int position) {
                mSelectedMinute = item;
                if (mOnMinuteSelectedListener != null) {
                    mOnMinuteSelectedListener.onMinuteSelected(item);
                }
            }
        });
    }

    private void initAttrs() {
        mMinMinute = 0;
        mMaxMinute = 60;
    }

    private void updateMinute() {
        List<Integer> list = new ArrayList<>();
        for (int i = mMinMinute; i < mMaxMinute; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    public void setSelectedMinute(int minute) {
        setSelectedMinute(minute, true);
    }

    public void setSelectedMinute(int minute, boolean smoothScroll) {
        setCurrentPosition(minute - mMinMinute, smoothScroll);
    }

    public void setIntervalMinute(int minMinute, int maxMinute) {
        this.mMinMinute = minMinute;
        this.mMaxMinute = maxMinute;
        updateMinute();
        setStartMinute(minMinute);
        setEndMinute(maxMinute);
    }

    public void setStartMinute(int minMinute) {
        if (mMinMinute > mSelectedMinute) {
            setSelectedMinute(mMinMinute, false);
        } else {
            setSelectedMinute(mSelectedMinute, false);
        }
    }

    public void setEndMinute(int maxMinute) {
        if (mSelectedMinute > mMaxMinute) {
            setSelectedMinute(mMaxMinute, false);
        } else {
            setSelectedMinute(mSelectedMinute, false);
        }
    }

    public int getSelectedMinute() {
        return mSelectedMinute;
    }

    public void setOnMinuteSelectedListener(OnMinuteSelectedListener onMinuteSelectedListener) {
        mOnMinuteSelectedListener = onMinuteSelectedListener;
    }

    public interface OnMinuteSelectedListener {
        void onMinuteSelected(int minute);
    }
}
