package coolx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import coolx.appcompat.R;
import coolx.appcompat.core.picker.DayPicker;
import coolx.appcompat.core.picker.MonthPicker;
import coolx.appcompat.core.picker.YearPicker;

/**
 * Description: Date Picker
 * Created by marco, at 2024/07/25
 */
public class DatePicker extends LinearLayout implements YearPicker.OnYearSelectedListener,
        MonthPicker.OnMonthSelectedListener, DayPicker.OnDaySelectedListener {

    private YearPicker mYearPicker;
    private MonthPicker mMonthPicker;
    private DayPicker mDayPicker;

    private OnDateSelectedListener mOnDateSelectedListener;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.coolx_special_date_picker, this);
        initChildPickers();
        initAttrs(context, attrs);
        mYearPicker.setBackground(getBackground());
        mMonthPicker.setBackground(getBackground());
        mDayPicker.setBackground(getBackground());
        setIndicatorText(
                getResources().getString(R.string.coolx_date_picker_year),
                getResources().getString(R.string.coolx_date_picker_month),
                getResources().getString(R.string.coolx_date_picker_day)
        );
    }

    /**
     * Initializes the attributes of the DatePicker based on the provided context and attribute set.
     *
     * @param context The context
     * @param attrs   The attribute set
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker);
        boolean isCyclic = a.getBoolean(R.styleable.DatePicker_wheelCyclic, true);
        a.recycle();

        setCyclic(isCyclic);
    }

    /**
     * Initializes the child pickers and sets the appropriate listener for each picker.
     */
    private void initChildPickers() {
        mYearPicker = findViewById(R.id.yearPicker_layout_date);
        mYearPicker.setOnYearSelectedListener(this);
        mMonthPicker = findViewById(R.id.monthPicker_layout_date);
        mMonthPicker.setOnMonthSelectedListener(this);
        mDayPicker = findViewById(R.id.dayPicker_layout_date);
        mDayPicker.setOnDaySelectedListener(this);
    }

    /**
     * Notifies the listener that a date has been selected.
     */
    private void onDateSelected() {
        if (mOnDateSelectedListener != null) {
            mOnDateSelectedListener.onDateSelected(getYear(), getMonth(), getDay());
        }
    }

    /**
     * Callback method invoked when a year is selected in the year picker.
     *
     * @param year The selected year
     */
    @Override
    public void onYearSelected(int year) {
        mMonthPicker.setYear(year);
        int month = getMonth();
        mDayPicker.setMonth(year, month);
        onDateSelected();
    }

    /**
     * Callback method invoked when a month is selected in the month picker.
     *
     * @param month The selected month
     */
    @Override
    public void onMonthSelected(int month) {
        mDayPicker.setMonth(getYear(), month);
        onDateSelected();
    }

    /**
     * Callback method invoked when a day is selected in the day picker.
     *
     * @param day The selected day
     */
    @Override
    public void onDaySelected(int day) {
        onDateSelected();
    }

    /**
     * Sets the interval of selectable years in the year picker.
     *
     * @param startYear The starting year of the interval
     * @param endYear   The ending year of the interval
     */
    public void setIntervalYear(int startYear, int endYear) {
        mYearPicker.setYear(startYear, endYear);
    }

    /**
     * Sets the interval of selectable months in the month picker.
     *
     * @param startMonth The starting month of the interval
     * @param endMonth   The ending month of the interval
     */
    public void setIntervalMonth(int startMonth, int endMonth) {
        mMonthPicker.setIntervalMonth(startMonth, endMonth);
    }

    /**
     * Sets the interval of selectable days in the day picker.
     *
     * @param startDay The starting day of the interval
     * @param EndDay   The ending day of the interval
     */
    public void setIntervalDay(int startDay, int EndDay) {
        mDayPicker.setIntervalDay(startDay, EndDay);
    }

    /**
     * Sets the date and updates the display of the picker.
     *
     * @param year  The year
     * @param month The month (1-12)
     * @param day   The day
     */
    public void setDate(int year, int month, int day) {
        setDate(year, month, day, true);
    }

    /**
     * Sets the date and updates the display of the picker.
     *
     * @param year         The year
     * @param month        The month (1-12)
     * @param day          The day
     * @param smoothScroll Whether to smoothly scroll to the specified date
     */
    public void setDate(int year, int month, int day, boolean smoothScroll) {
        mYearPicker.setSelectedYear(year, smoothScroll);
        mMonthPicker.setSelectedMonth(month, smoothScroll);
        mDayPicker.setSelectedDay(day, smoothScroll);
    }

    /**
     * Returns the selected year from the year picker.
     */
    private int getYear() {
        return mYearPicker.getSelectedYear();
    }

    /**
     * Returns the selected month from the month picker.
     */
    private int getMonth() {
        return mMonthPicker.getSelectedMonth();
    }

    /**
     * Returns the selected day from the day picker.
     */
    private int getDay() {
        return mDayPicker.getSelectedDay();
    }

    /**
     * Sets whether the day, month, and year pickers should wrap around and become cyclic.
     *
     * @param cyclic {@code true} to enable cyclic behavior, {@code false} to disable it
     */
    public void setCyclic(boolean cyclic) {
        mDayPicker.setCyclic(cyclic);
        mMonthPicker.setCyclic(cyclic);
        mYearPicker.setCyclic(cyclic);
    }

    /**
     * Sets the indicator text for the year, month, and day pickers.
     *
     * @param yearText  The indicator text for the year picker
     * @param monthText The indicator text for the month picker
     * @param dayText   The indicator text for the day picker
     */
    private void setIndicatorText(String yearText, String monthText, String dayText) {
        mYearPicker.setIndicatorText(yearText);
        mMonthPicker.setIndicatorText(monthText);
        mDayPicker.setIndicatorText(dayText);
    }

    /**
     * Sets the listener to be notified when a date is selected.
     *
     * @param onDateSelectedListener The listener to be set
     */
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        mOnDateSelectedListener = onDateSelectedListener;
    }

    /**
     * Interface definition for a callback to be invoked when a date is selected.
     */
    public interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int day);
    }
}
