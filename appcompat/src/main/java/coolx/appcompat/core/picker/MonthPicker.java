package coolx.appcompat.core.picker;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import coolx.appcompat.core.WheelPicker;

/**
 * Description: Month Picker
 * Author: Marco
 * Last Updated: 2023.07.14
 */
public class MonthPicker extends WheelPicker<Integer> {

    private static final int MAX_MONTH = 12;
    private static final int MIN_MONTH = 1;

    private int mSelectedMonth;

    private OnMonthSelectedListener mOnMonthSelectedListener;

    private int mYear;
    private long mMaxDate, mMinDate;
    private int mMaxYear, mMinYear;
    private int mMinMonth = MIN_MONTH;
    private int mMaxMonth = MAX_MONTH;

    public MonthPicker(Context context) {
        this(context, null);
    }

    public MonthPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemMaximumWidthText("00");
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);    // 个位数补 0 显示
        setDataFormat(numberFormat);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSelectedMonth = LocalDate.now().getMonthValue();
        } else {
            Calendar.getInstance().clear();
            mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        }
        updateMonth();
        setSelectedMonth(mSelectedMonth, false);
        setOnWheelChangeListener((item, position) -> {
            mSelectedMonth = item;
            if (mOnMonthSelectedListener != null) {
                mOnMonthSelectedListener.onMonthSelected(item);
            }
        });
    }

    /**
     * Updates the month range.
     * This method generates a list of months from the start month to the end month and sets it as the data list.
     */
    public void updateMonth() {
        List<Integer> list = new ArrayList<>();
        for (int i = mMinMonth; i <= mMaxMonth; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    private void setMaxDate(long date) {
        mMaxDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        mMaxYear = calendar.get(Calendar.YEAR);
    }

    private void setMinDate(long date) {
        mMinDate = date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        mMinYear = calendar.get(Calendar.YEAR);
    }

    public void setYear(int year) {
        mYear = year;
        mMinMonth = MIN_MONTH;
        mMaxMonth = MAX_MONTH;
        if (mMaxDate != 0 && mMaxYear == year) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mMaxDate);
            mMaxMonth = calendar.get(Calendar.MONTH) + 1;

        }
        if (mMinDate != 0 && mMinYear == year) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mMinDate);
            mMinMonth = calendar.get(Calendar.MONTH) + 1;

        }
        updateMonth();
        if (mSelectedMonth > mMaxMonth) {
            setSelectedMonth(mMaxMonth, false);
        } else if (mSelectedMonth < mMinMonth) {
            setSelectedMonth(mMinMonth, false);
        } else {
            setSelectedMonth(mSelectedMonth, false);
        }
    }

    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    public void setMonthYear(int startMonth) {
        if (mMinMonth > mSelectedMonth) {
            setSelectedMonth(mMinMonth, false);
        } else {
            setSelectedMonth(mSelectedMonth, false);
        }
    }

    public void setEndMonth(int endMonth) {
        if (mSelectedMonth > endMonth) {
            setSelectedMonth(mMaxMonth, false);
        } else {
            setSelectedMonth(mSelectedMonth, false);
        }
    }

    /**
     * Sets the start and end months for a time period.
     *
     * @param startMonth The starting month
     * @param endMonth   The ending month
     */
    public void setIntervalMonth(int startMonth, int endMonth) {
        this.mMinMonth = startMonth;
        this.mMaxMonth = endMonth;
        updateMonth();
        setMonthYear(startMonth);
        setEndMonth(endMonth);
    }

    /**
     * Sets the selected month and updates the current position.
     *
     * @param selectedMonth The year to be selected
     * @param smoothScroll  Flag indicating whether to enable smooth scrolling
     */
    public void setSelectedMonth(int selectedMonth, boolean smoothScroll) {
        setCurrentPosition(selectedMonth - mMinMonth, smoothScroll);
        mSelectedMonth = selectedMonth;
    }

    /**
     * Sets the listener to be notified when a month is selected.
     *
     * @param onMonthSelectedListener The listener to be set
     */
    public void setOnMonthSelectedListener(OnMonthSelectedListener onMonthSelectedListener) {
        mOnMonthSelectedListener = onMonthSelectedListener;
    }

    /**
     * Interface definition for a callback to be invoked when a month is selected.
     */
    public interface OnMonthSelectedListener {
        void onMonthSelected(int month);
    }

}
