package coolx.appcompat.core.picker;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import coolx.appcompat.core.WheelPicker;

/**
 * Description: Day Picker
 * Author: Marco
 * Last Updated: 2023.07.14
 */
public class DayPicker extends WheelPicker<Integer> {

    private int mMinDay, mMaxDay;

    private int mSelectedDay;

    private int mYear, mMonth;
    private long mMaxDate, mMinDate;
    private boolean mIsSetMaxDate;

    private OnDaySelectedListener mOnDaySelectedListener;

    public DayPicker(Context context) {
        this(context, null);
    }

    public DayPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemMaximumWidthText("00");
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);    // 个位数补 0 显示
        setDataFormat(numberFormat);
        mMinDay = 1;
        mMaxDay = Calendar.getInstance().getActualMaximum(Calendar.DATE);
        updateDay();
        mSelectedDay = Calendar.getInstance().get(Calendar.DATE);
        setSelectedDay(mSelectedDay, false);
        setOnWheelChangeListener((item, position) -> {
            mSelectedDay = item;
            if (mOnDaySelectedListener != null) {
                mOnDaySelectedListener.onDaySelected(item);
            }
        });
    }

    public void setMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mMaxDate);    // TODO: Optimization
        int maxYear = calendar.get(Calendar.YEAR);
        int maxMonth = calendar.get(Calendar.MONTH) + 1;
        int maxDay = calendar.get(Calendar.DAY_OF_MONTH);
        // 如果不判断 mIsSetMaxDate，则long 为0，则选择 1970-01-01 时会有问题
        if (mIsSetMaxDate && maxYear == year && maxMonth == month) {
            mMaxDay = maxDay;
        } else {
            calendar.set(year, month - 1, 1);
            mMaxDay = calendar.getActualMaximum(Calendar.DATE);
        }
        Log.d(TAG, "setMonth: year:" + year + " month: " + month + " day:" + mMaxDay);
        calendar.setTimeInMillis(mMinDate);
        int minYear = calendar.get(Calendar.YEAR);
        int minMonth = calendar.get(Calendar.MONTH) + 1;
        int minDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (minYear == year && minMonth == month) {
            mMinDay = minDay;
        } else {
            mMinDay = 1;
        }
        updateDay();
        if (mSelectedDay < mMinDay) {
            setSelectedDay(mMinDay, false);
        } else if (mSelectedDay > mMaxDay) {
            setSelectedDay(mMaxDay, false);
        } else {
            setSelectedDay(mSelectedDay, false);
        }
    }

    public int getSelectedDay() {
        return mSelectedDay;
    }

    public void setSelectedDay(int selectedDay) {
        setSelectedDay(selectedDay, true);
    }

    public void setSelectedDay(int selectedDay, boolean smoothScroll) {
        setCurrentPosition(selectedDay - mMinDay, smoothScroll);
        mSelectedDay = selectedDay;
    }

    private void setMaxDate(long date) {
        mMaxDate = date;
        mIsSetMaxDate = true;
    }

    private void setMinDate(long date) {
        mMinDate = date;
    }

    public void setStartDay(int startDay) {
        if (mMinDay > mSelectedDay) {
            setSelectedDay(mMinDay, false);
        } else {
            setSelectedDay(mSelectedDay, false);
        }
    }

    public void setEndDay(int endDay) {
        if (mSelectedDay > endDay) {
            setSelectedDay(mMaxDay, false);
        } else {
            setSelectedDay(mSelectedDay, false);
        }
    }

    /**
     * Sets the start and end days for a time period.
     *
     * @param startDay The starting day
     * @param endDay   The ending day
     */
    public void setIntervalDay(int startDay, int endDay) {
        this.mMinDay = startDay;
        this.mMaxDay = endDay;
        updateDay();
        setStartDay(startDay);
        setEndDay(endDay);
    }

    /**
     * Updates the day range.
     * This method generates a list of days from the start day to the end day and sets it as the data list.
     */
    private void updateDay() {
        List<Integer> list = new ArrayList<>();
        for (int i = mMinDay; i <= mMaxDay; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    /**
     * Sets the listener to be notified when a day is selected.
     *
     * @param onDaySelectedListener The listener to be set
     */
    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        mOnDaySelectedListener = onDaySelectedListener;
    }

    /**
     * Interface definition for a callback to be invoked when a day is selected.
     */
    public interface OnDaySelectedListener {
        void onDaySelected(int day);
    }
}
