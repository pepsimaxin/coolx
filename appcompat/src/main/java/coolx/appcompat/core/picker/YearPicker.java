package coolx.appcompat.core.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import coolx.appcompat.R;
import coolx.appcompat.core.WheelPicker;

/**
 * Description: Year Picker
 * Author: Marco
 * Last Updated: 2023.07.14
 */
public class YearPicker extends WheelPicker<Integer> {

    private int mStartYear, mEndYear;
    private int mSelectedYear;

    private OnYearSelectedListener mOnYearSelectedListener;

    public YearPicker(Context context) {
        this(context, null);
    }

    public YearPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        setItemMaximumWidthText("0000");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSelectedYear = LocalDate.now().getYear();
        } else {
            mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
        }
        updateYear();
        setSelectedYear(mSelectedYear, false);
        setOnWheelChangeListener((item, position) -> {
            mSelectedYear = item;
            if (mOnYearSelectedListener != null) {
                mOnYearSelectedListener.onYearSelected(item);
            }
        });
    }

    /**
     * Initializes the attributes of the YearPicker based on the provided context and attribute set.
     *
     * @param context The context
     * @param attrs   The attribute set
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YearPicker);
        mStartYear = a.getInteger(R.styleable.YearPicker_coolStartYear, 1900);
        mEndYear = a.getInteger(R.styleable.YearPicker_coolEndYear, 2100);
        a.recycle();
    }

    /**
     * Updates the year range.
     * This method generates a list of years from the start year to the end year and sets it as the data list.
     */
    private void updateYear() {
        List<Integer> list = new ArrayList<>();
        for (int i = mStartYear; i <= mEndYear; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    /**
     * Sets the starting year of the range.
     * If the starting year is greater than the currently selected year, the selected year is updated to the starting year.
     * Otherwise, the selected year remains unchanged.
     */
    public void setStartYear() {
        setSelectedYear(Math.max(mStartYear, mSelectedYear), false);
    }

    /**
     * Sets the ending year of the range.
     * If the ending year is smaller than the currently selected year, the selected year is updated to the ending year.
     * Otherwise, the selected year remains unchanged.
     */
    public void setEndYear() {
        setSelectedYear(Math.min(mSelectedYear, mEndYear), false);
    }

    /**
     * Sets the start and end years for a time period.
     *
     * @param startYear The starting year
     * @param endYear   The ending year
     */
    public void setYear(int startYear, int endYear) {
        this.mStartYear = startYear;
        this.mEndYear = endYear;
        updateYear();
        setStartYear();
        setEndYear();
    }

    /**
     * Sets the selected year and updates the current position.
     *
     * @param selectedYear The year to be selected
     * @param smoothScroll Flag indicating whether to enable smooth scrolling
     */
    public void setSelectedYear(int selectedYear, boolean smoothScroll) {
        setCurrentPosition(selectedYear - mStartYear, smoothScroll);
    }

    /**
     * Returns the selected year.
     *
     * @return The selected year
     */
    public int getSelectedYear() {
        return mSelectedYear;
    }

    /**
     * Sets the listener to be notified when a year is selected.
     *
     * @param onYearSelectedListener The listener to be set
     */
    public void setOnYearSelectedListener(OnYearSelectedListener onYearSelectedListener) {
        mOnYearSelectedListener = onYearSelectedListener;
    }

    /**
     * Interface definition for a callback to be invoked when a year is selected.
     */
    public interface OnYearSelectedListener {
        void onYearSelected(int year);
    }
}
