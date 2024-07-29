package coolx.appcompat.core.picker;

import android.content.Context;
import android.util.AttributeSet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import coolx.appcompat.core.WheelPicker;

/**
 * Description: HourPicker
 */
public class HourPicker extends WheelPicker<Integer> {
    private int mMinHour, mMaxHour;
    private int mSelectedHour;

    private OnHourSelectedListener mOnHourSelectedListener;

    public HourPicker(Context context) {
        this(context, null);
    }

    public HourPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HourPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
        setItemMaximumWidthText("00");
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);
        setDataFormat(numberFormat);
        updateHour();
        setSelectedHour(mSelectedHour, false);
        setOnWheelChangeListener(new OnWheelChangeListener<Integer>() {
            @Override
            public void onWheelSelected(Integer item, int position) {
                mSelectedHour = item;
                if (mOnHourSelectedListener != null) {
                    mOnHourSelectedListener.onHourSelected(item);
                }
            }
        });
    }

    private void initAttrs() {
        mMinHour = 0;
        mMaxHour = 24;
    }

    private void updateHour() {
        List<Integer> list = new ArrayList<>();
        for (int i = mMinHour; i < mMaxHour; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    public void setSelectedHour(int hour) {
        setSelectedHour(hour, true);
    }

    public void setSelectedHour(int hour, boolean smoothScroll) {
        setCurrentPosition(hour - mMinHour, smoothScroll);
    }

    public void setIntervalHour(int minHour, int maxHour) {
        this.mMinHour = minHour;
        this.mMaxHour = maxHour;
        updateHour();
        setStartHour(minHour);
        setEndHour(maxHour);
    }

    public void setStartHour(int minHour) {
        if (mMinHour > mSelectedHour) {
            setSelectedHour(mMinHour, false);
        } else {
            setSelectedHour(mSelectedHour, false);
        }
    }

    public void setEndHour(int maxHour) {
        if (mSelectedHour > mMaxHour) {
            setSelectedHour(mMaxHour, false);
        } else {
            setSelectedHour(mSelectedHour, false);
        }
    }

    public int getSelectedHour() {
        return mSelectedHour;
    }

    public void setOnHourSelectedListener(OnHourSelectedListener onHourSelectedListener) {
        mOnHourSelectedListener = onHourSelectedListener;
    }

    public interface OnHourSelectedListener {
        void onHourSelected(int hour);
    }
}
