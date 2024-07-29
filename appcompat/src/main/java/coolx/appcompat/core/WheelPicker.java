package coolx.appcompat.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.text.Format;
import java.util.List;

import coolx.appcompat.R;

/**
 * Description: 滚动选择器
 */
@SuppressWarnings({"FieldCanBeLocal", "unused", "SameParameterValue"})
public class WheelPicker<T> extends View {

    /**
     * 数据集合
     */
    private List<T> mDataList;

    private Format mDataFormat;

    /**
     * Item 的 Text 的颜色
     */
    private int mTextColor;

    private int mTextSize;

    private Paint mTextPaint;

    /**
     * 字体渐变，开启后越靠近边缘，字体越模糊
     */
    private boolean mIsTextGradual;

    /**
     * 选中的 Item 的 Text 颜色
     */
    private int mSelectedItemTextColor;

    /**
     * 选中的 Item 的 Text 大小
     */
    private int mSelectedItemTextSize;

    private Paint mSelectedItemPaint;

    /**
     * 指示器文字，会在中心文字后边多绘制一个文字
     */
    private String mIndicatorText;

    /**
     * 指示器文字颜色（年月日）
     */
    private int mIndicatorTextColor;

    /**
     * 指示器文字大小（年月日）
     */
    private int mIndicatorTextSize;

    private Paint mIndicatorPaint;

    private Paint mPaint;

    /**
     * 最大的一个Item的文本的宽高
     */
    private int mTextMaxWidth, mTextMaxHeight;

    /**
     * 输入的一段文字，可以用来测量 mTextMaxWidth
     */
    private String mItemMaximumWidthText;

    /**
     * 中心 Item 上下两边分别的数量
     */
    private int mHalfVisibleItemCount = 1;

    /**
     * 两个Item之间的高度间隔
     */
    private int mItemHeightSpace, mItemWidthSpace;

    private int mItemHeight;

    /**
     * 当前的Item的位置
     */
    private int mCurrentPosition;

    /**
     * 是否将中间的Item放大
     */
    private boolean mIsZoomInSelectedItem;

    /**
     * 是否显示幕布，中央Item会遮盖一个颜色
     */
    private boolean mIsShowCurtain;

    /**
     * 幕布颜色
     */
    private int mCurtainColor;

    /**
     * 是否显示幕布的边框
     */
    private boolean mIsShowCurtainBorder;

    /**
     * 幕布边框的颜色
     */
    private int mCurtainBorderColor;

    /**
     * 整个控件的可绘制面积
     */
    private final Rect mDrawnRect;

    /**
     * 中心被选中的 Item 的坐标矩形
     */
    private final Rect mSelectedItemRect;

    /**
     * 第一个Item的绘制Text的坐标
     */
    private int mFirstItemDrawX, mFirstItemDrawY;

    /**
     * 中心的Item绘制text的Y轴坐标
     */
    private int mCenterItemDrawnY;

    private final Scroller mScroller;

    private final int mTouchSlop;

    /**
     * 该标记的作用是，令mTouchSlop仅在一个滑动过程中生效一次。
     */
    private boolean mTouchSlopFlag;

    private VelocityTracker mTracker;

    private float mTouchDownY;
    /**
     * Y轴Scroll滚动的位移
     */
    private int mScrollOffsetY;

    /**
     * 最后手指Down事件的Y轴坐标，用于计算拖动距离
     */
    private float mLastDownY;

    /**
     * 是否循环读取
     */
    private boolean mIsCyclic = true;

    /**
     * 最大可以Fling的距离
     */
    private int mMaxFlingY, mMinFlingY;

    /**
     * 滚轮滑动时的最小/最大速度
     */
    private int mMinimumVelocity = 50, mMaximumVelocity = 12000;

    /**
     * 是否是手动停止滚动
     */
    private boolean mIsAbortScroller;

    private final LinearGradient mLinearGradient;

    private final Handler mHandler = new Handler();

    private OnWheelChangeListener<T> mOnWheelChangeListener;

    private final Runnable mScrollerRunnable = new Runnable() {
        @Override
        public void run() {

            if (mScroller.computeScrollOffset()) {

                mScrollOffsetY = mScroller.getCurrY();
                postInvalidate();
                mHandler.postDelayed(this, 16);
            }
            if (mScroller.isFinished() || (mScroller.getFinalY() == mScroller.getCurrY()
                    && mScroller.getFinalX() == mScroller.getCurrX())) {

                if (mItemHeight == 0) {
                    return;
                }
                int position = -mScrollOffsetY / mItemHeight;
                position = fixItemPosition(position);
                if (mCurrentPosition != position) {
                    mCurrentPosition = position;
                    if (mOnWheelChangeListener == null) {
                        return;
                    }
                    mOnWheelChangeListener.onWheelSelected(mDataList.get(position),
                            position);
                }
            }
        }
    };

    public WheelPicker(Context context) {
        this(context, null);
    }

    public WheelPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
        mLinearGradient = new LinearGradient(mTextColor, mSelectedItemTextColor);
        mDrawnRect = new Rect();
        mSelectedItemRect = new Rect();
        mScroller = new Scroller(context);

        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelPicker);
        mTextSize = getResources().getDimensionPixelSize(R.dimen.coolx_wheel_item_text_size);
        mTextColor = getResources().getColor(R.color.coolx_wheelpicker_item_text_color, null);
        mIsTextGradual = true;
        mIsCyclic = a.getBoolean(R.styleable.WheelPicker_wheelCyclic, false);
        mItemMaximumWidthText = a.getString(R.styleable.WheelPicker_itemMaximumWidthText);
        mSelectedItemTextColor = getResources().getColor(R.color.coolx_wheelpicker_item_text_color_selected, null);
        mSelectedItemTextSize = getResources().getDimensionPixelSize(R.dimen.coolx_wheel_item_text_size_selected);
        mCurrentPosition = 0;
        mItemWidthSpace = getResources().getDimensionPixelOffset(R.dimen.coolx_wheel_item_width_space);
        mItemHeightSpace = getResources().getDimensionPixelOffset(R.dimen.coolx_wheel_item_height_space);
        mIsZoomInSelectedItem = true;
        mIsShowCurtain = true;
        mCurtainColor = Color.TRANSPARENT;
        mIsShowCurtainBorder = true;
        mCurtainBorderColor = getResources().getColor(R.color.coolx_wheelpicker_divider, null);
        mIndicatorText = "";
        mIndicatorTextColor = getResources().getColor(R.color.coolx_wheelpicker_indicator_text_color, null);
        mIndicatorTextSize = getResources().getDimensionPixelSize(R.dimen.coolx_wheel_indicator_text_size);
        a.recycle();
    }

    public void computeTextSize() {
        mTextMaxWidth = mTextMaxHeight = 0;
        if (mDataList.size() == 0) {
            return;
        }

        //这里使用最大的,防止文字大小超过布局大小。
        mPaint.setTextSize(Math.max(mSelectedItemTextSize, mTextSize));

        if (!TextUtils.isEmpty(mItemMaximumWidthText)) {
            mTextMaxWidth = (int) mPaint.measureText(mItemMaximumWidthText);
        } else {
            mTextMaxWidth = (int) mPaint.measureText(mDataList.get(0).toString());
        }
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        mTextMaxHeight = (int) (metrics.bottom - metrics.top);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mSelectedItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mSelectedItemPaint.setStyle(Paint.Style.FILL);
        mSelectedItemPaint.setTextAlign(Paint.Align.CENTER);
        mSelectedItemPaint.setColor(mSelectedItemTextColor);
        mSelectedItemPaint.setTextSize(mSelectedItemTextSize);
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mIndicatorPaint.setTextAlign(Paint.Align.LEFT);
        mIndicatorPaint.setColor(mIndicatorTextColor);
        mIndicatorPaint.setTextSize(mIndicatorTextSize);
    }

    /**
     * 计算实际的大小
     *
     * @param specMode 测量模式
     * @param specSize 测量的大小
     * @param size     需要的大小
     * @return 返回的数值
     */
    private int measureSize(int specMode, int specSize, int size) {
        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            return Math.min(specSize, size);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = mTextMaxWidth + mItemWidthSpace;
        int height = (mTextMaxHeight + mItemHeightSpace) * getVisibleItemCount();

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureSize(specWidthMode, specWidthSize, width),
                measureSize(specHeightMode, specHeightSize, height));
    }

    /**
     * 计算Fling极限
     * 如果为Cyclic模式则为Integer的极限值，如果正常模式，则为一整个数据集的上下限。
     */
    private void computeFlingLimitY() {
        mMinFlingY = mIsCyclic ? Integer.MIN_VALUE :
                -mItemHeight * (mDataList.size() - 1);
        mMaxFlingY = mIsCyclic ? Integer.MAX_VALUE : 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawnRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mItemHeight = mDrawnRect.height() / getVisibleItemCount();
        mFirstItemDrawX = mDrawnRect.centerX();
        mFirstItemDrawY = (int) ((mItemHeight - (mSelectedItemPaint.ascent() + mSelectedItemPaint.descent())) / 2);
        // 中间的 Item 边框
        mSelectedItemRect.set(getPaddingLeft(), mItemHeight * mHalfVisibleItemCount,
                getWidth() - getPaddingRight(), mItemHeight + mItemHeight * mHalfVisibleItemCount);
        computeFlingLimitY();
        mCenterItemDrawnY = mFirstItemDrawY + mItemHeight * mHalfVisibleItemCount;

        mScrollOffsetY = -mItemHeight * mCurrentPosition;
    }

    /**
     * 修正坐标值，让其回到dateList的范围内
     *
     * @param position 修正前的值
     * @return 修正后的值
     */
    private int fixItemPosition(int position) {
        if (position < 0) {
            //将数据集限定在0 ~ mDataList.size()-1之间
            position = mDataList.size() + (position % mDataList.size());

        }
        if (position >= mDataList.size()) {
            //将数据集限定在0 ~ mDataList.size()-1之间
            position = position % mDataList.size();
        }
        return position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 文本水平居中对齐
        mPaint.setTextAlign(Paint.Align.CENTER);
        if (mIsShowCurtain) {
            mPaint.setStyle(Paint.Style.FILL);             // 实心
            mPaint.setColor(mCurtainColor);                // 实心透明
            canvas.drawRect(mSelectedItemRect, mPaint);    // 绘制矩形
        }
        if (mIsShowCurtainBorder) {
            mPaint.setStyle(Paint.Style.STROKE);           // 边框
            mPaint.setColor(mCurtainBorderColor);          // 边框颜色
            canvas.drawLine(getPaddingLeft(), mItemHeight * mHalfVisibleItemCount,
                    getWidth() - getPaddingRight(), mItemHeight * mHalfVisibleItemCount, mPaint);
            canvas.drawLine(getPaddingLeft(), mItemHeight + mItemHeight * mHalfVisibleItemCount,
                    getWidth() - getPaddingRight(), mItemHeight + mItemHeight * mHalfVisibleItemCount, mPaint);
//            canvas.drawRect(mSelectedItemRect, mPaint);
//            canvas.drawRect(mDrawnRect, mPaint);
        }
        int drawnSelectedPos = -mScrollOffsetY / mItemHeight;
        mPaint.setStyle(Paint.Style.FILL);
        //首尾各多绘制一个用于缓冲
        for (int drawDataPos = drawnSelectedPos - mHalfVisibleItemCount - 1;
             drawDataPos <= drawnSelectedPos + mHalfVisibleItemCount + 1; drawDataPos++) {
            int position = drawDataPos;
            if (mIsCyclic) {
                position = fixItemPosition(position);
            } else {
                if (position < 0 || position > mDataList.size() - 1) {
                    continue;
                }
            }

            T data = mDataList.get(position);
            int itemDrawY = mFirstItemDrawY + (drawDataPos + mHalfVisibleItemCount) * mItemHeight + mScrollOffsetY;
            //距离中心的Y轴距离
            int distanceY = Math.abs(mCenterItemDrawnY - itemDrawY);

            if (mIsTextGradual) {
                //文字颜色渐变要在设置透明度上边，否则会被覆盖
                //计算文字颜色渐变
                if (distanceY < mItemHeight) {  //距离中心的高度小于一个ItemHeight才会开启渐变
                    float colorRatio = 1 - (distanceY / (float) mItemHeight);
                    mSelectedItemPaint.setColor(mLinearGradient.getColor(colorRatio));
                    mTextPaint.setColor(mLinearGradient.getColor(colorRatio));
                } else {
                    mSelectedItemPaint.setColor(mSelectedItemTextColor);
                    mTextPaint.setColor(mTextColor);
                }
                //计算透明度渐变
                float alphaRatio;
                if (itemDrawY > mCenterItemDrawnY) {
                    alphaRatio = (mDrawnRect.height() - itemDrawY) /
                            (float) (mDrawnRect.height() - (mCenterItemDrawnY));
                } else {
                    alphaRatio = itemDrawY / (float) mCenterItemDrawnY;
                }

                alphaRatio = alphaRatio < 0 ? 0 : alphaRatio;
                mSelectedItemPaint.setAlpha((int) (alphaRatio * 255));
                mTextPaint.setAlpha((int) (alphaRatio * 255));
            }

            //开启此选项,会将越靠近中心的Item字体放大
            if (mIsZoomInSelectedItem) {
                if (distanceY < mItemHeight) {
                    float addedSize = (mItemHeight - distanceY) / (float) mItemHeight * (mSelectedItemTextSize - mTextSize);
                    mSelectedItemPaint.setTextSize(mTextSize + addedSize);
                    mTextPaint.setTextSize(mTextSize + addedSize);
                } else {
                    mSelectedItemPaint.setTextSize(mTextSize);
                    mTextPaint.setTextSize(mTextSize);
                }
            } else {
                mSelectedItemPaint.setTextSize(mTextSize);
                mTextPaint.setTextSize(mTextSize);
            }
            String drawText = mDataFormat == null ? data.toString() : mDataFormat.format(data);
            //在中间位置的Item作为被选中的。
            if (distanceY < mItemHeight / 2) {
                canvas.drawText(drawText, mFirstItemDrawX, itemDrawY, mSelectedItemPaint);
            } else {
                canvas.drawText(drawText, mFirstItemDrawX, itemDrawY, mTextPaint);
            }
        }
        if (!TextUtils.isEmpty(mIndicatorText)) {
            canvas.drawText(mIndicatorText, mFirstItemDrawX + mTextMaxWidth / 2F + 10, mCenterItemDrawnY - 5, mIndicatorPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    mIsAbortScroller = true;
                } else {
                    mIsAbortScroller = false;
                }
                mTracker.clear();
                mTouchDownY = mLastDownY = event.getY();
                mTouchSlopFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchSlopFlag && Math.abs(mTouchDownY - event.getY()) < mTouchSlop) {
                    break;
                }
                mTouchSlopFlag = false;
                float move = event.getY() - mLastDownY;
                mScrollOffsetY += move;
                mLastDownY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsAbortScroller && mTouchDownY == mLastDownY) {
                    performClick();
                    if (event.getY() > mSelectedItemRect.bottom) {
                        int scrollItem = (int) (event.getY() - mSelectedItemRect.bottom) / mItemHeight + 1;
                        mScroller.startScroll(0, mScrollOffsetY, 0,
                                -scrollItem * mItemHeight);

                    } else if (event.getY() < mSelectedItemRect.top) {
                        int scrollItem = (int) (mSelectedItemRect.top - event.getY()) / mItemHeight + 1;
                        mScroller.startScroll(0, mScrollOffsetY, 0,
                                scrollItem * mItemHeight);
                    }
                } else {
                    mTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int velocity = (int) mTracker.getYVelocity();
                    if (Math.abs(velocity) > mMinimumVelocity) {
                        mScroller.fling(0, mScrollOffsetY, 0, velocity,
                                0, 0, mMinFlingY, mMaxFlingY);
                        mScroller.setFinalY(mScroller.getFinalY() +
                                computeDistanceToEndPoint(mScroller.getFinalY() % mItemHeight));
                    } else {
                        mScroller.startScroll(0, mScrollOffsetY, 0,
                                computeDistanceToEndPoint(mScrollOffsetY % mItemHeight));
                    }
                }
                if (!mIsCyclic) {
                    if (mScroller.getFinalY() > mMaxFlingY) {
                        mScroller.setFinalY(mMaxFlingY);
                    } else if (mScroller.getFinalY() < mMinFlingY) {
                        mScroller.setFinalY(mMinFlingY);
                    }
                }
                mHandler.post(mScrollerRunnable);
                mTracker.recycle();
                mTracker = null;
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int computeDistanceToEndPoint(int remainder) {
        if (Math.abs(remainder) > mItemHeight / 2) {
            if (mScrollOffsetY < 0) {
                return -mItemHeight - remainder;
            } else {
                return mItemHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }


    public void setOnWheelChangeListener(OnWheelChangeListener<T> onWheelChangeListener) {
        mOnWheelChangeListener = onWheelChangeListener;
    }

    public Paint getTextPaint() {
        return mTextPaint;
    }

    public Paint getSelectedItemPaint() {
        return mSelectedItemPaint;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public Paint getIndicatorPaint() {
        return mIndicatorPaint;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> dataList) {
        T currentSelected = getCurrentSelectValue(mDataList);
        T newSelected = getCurrentSelectValue(dataList);
        mDataList = dataList;
        if (dataList.size() == 0) {
            return;
        }
        if (currentSelected != newSelected && newSelected != null && mOnWheelChangeListener != null) {
            mOnWheelChangeListener.onWheelSelected(newSelected, mCurrentPosition);
        }
        computeTextSize();
        computeFlingLimitY();
        requestLayout();
        postInvalidate();
    }

    private int getTextColor() {
        return mTextColor;
    }

    private void setTextColor(int textColor) {
        if (mTextColor == textColor) {
            return;
        }
        mTextPaint.setColor(textColor);
        mTextColor = textColor;
        mLinearGradient.setStartColor(textColor);
        postInvalidate();
    }

    private int getTextSize() {
        return mTextSize;
    }

    private void setTextSize(int textSize) {
        if (mTextSize == textSize) {
            return;
        }
        mTextSize = textSize;
        mTextPaint.setTextSize(textSize);
        computeTextSize();
        postInvalidate();
    }

    private int getSelectedItemTextColor() {
        return mSelectedItemTextColor;
    }

    /**
     * 设置被选中时候的文本颜色
     *
     * @param selectedItemTextColor 文本颜色
     */
    private void setSelectedItemTextColor(int selectedItemTextColor) {
        if (mSelectedItemTextColor == selectedItemTextColor) {
            return;
        }
        mSelectedItemPaint.setColor(selectedItemTextColor);
        mSelectedItemTextColor = selectedItemTextColor;
        mLinearGradient.setEndColor(selectedItemTextColor);
        postInvalidate();
    }

    private int getSelectedItemTextSize() {
        return mSelectedItemTextSize;
    }

    private void setSelectedItemTextSize(int selectedItemTextSize) {
        if (mSelectedItemTextSize == selectedItemTextSize) {
            return;
        }
        mSelectedItemPaint.setTextSize(selectedItemTextSize);
        mSelectedItemTextSize = selectedItemTextSize;
        computeTextSize();
        postInvalidate();
    }


    public String getItemMaximumWidthText() {
        return mItemMaximumWidthText;
    }

    /**
     * 设置输入的一段文字，用来测量 mTextMaxWidth
     *
     * @param itemMaximumWidthText 文本内容
     */
    public void setItemMaximumWidthText(String itemMaximumWidthText) {
        mItemMaximumWidthText = itemMaximumWidthText;
        requestLayout();
        postInvalidate();
    }

    private int getVisibleItemCount() {
        return mHalfVisibleItemCount * 2 + 1;
    }

    private int getItemWidthSpace() {
        return mItemWidthSpace;
    }

    private void setItemWidthSpace(int itemWidthSpace) {
        if (mItemWidthSpace == itemWidthSpace) {
            return;
        }
        mItemWidthSpace = itemWidthSpace;
        requestLayout();
    }

    private int getItemHeightSpace() {
        return mItemHeightSpace;
    }

    private void setItemHeightSpace(int itemHeightSpace) {
        if (mItemHeightSpace == itemHeightSpace) {
            return;
        }
        mItemHeightSpace = itemHeightSpace;
        requestLayout();
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * 设置当前选中的列表项,将滚动到所选位置
     *
     * @param currentPosition 设置的当前位置
     */
    public void setCurrentPosition(int currentPosition) {
        setCurrentPosition(currentPosition, true);
    }

    /**
     * 设置当前选中的列表位置
     *
     * @param currentPosition 设置的当前位置
     * @param smoothScroll    是否平滑滚动
     */
    public synchronized void setCurrentPosition(int currentPosition, boolean smoothScroll) {
        if (currentPosition > mDataList.size() - 1) {
            currentPosition = mDataList.size() - 1;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        if (mCurrentPosition == currentPosition) {
            return;
        }
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

        //如果mItemHeight=0代表还没有绘制完成，这时平滑滚动没有意义
        if (smoothScroll && mItemHeight > 0) {
            mScroller.startScroll(0, mScrollOffsetY, 0, (mCurrentPosition - currentPosition) * mItemHeight);
            int finalY = -currentPosition * mItemHeight;
            mScroller.setFinalY(finalY);
            mHandler.post(mScrollerRunnable);
        } else {
            mCurrentPosition = currentPosition;
            mScrollOffsetY = -mItemHeight * mCurrentPosition;
            postInvalidate();
            if (mOnWheelChangeListener != null) {
                mOnWheelChangeListener.onWheelSelected(mDataList.get(currentPosition), currentPosition);
            }
        }
    }

    private boolean isZoomInSelectedItem() {
        return mIsZoomInSelectedItem;
    }

    private void setZoomInSelectedItem(boolean zoomInSelectedItem) {
        if (mIsZoomInSelectedItem == zoomInSelectedItem) {
            return;
        }
        mIsZoomInSelectedItem = zoomInSelectedItem;
        postInvalidate();
    }

    public boolean isCyclic() {
        return mIsCyclic;
    }

    /**
     * 设置是否循环滚动。
     *
     * @param cyclic 上下边界是否相邻
     */
    public void setCyclic(boolean cyclic) {
        if (mIsCyclic == cyclic) {
            return;
        }
        mIsCyclic = cyclic;
        computeFlingLimitY();
        requestLayout();
    }

    public int getMinimumVelocity() {
        return mMinimumVelocity;
    }

    /**
     * 设置最小滚动速度,如果实际速度小于此速度，将不会触发滚动。
     *
     * @param minimumVelocity 最小速度
     */
    public void setMinimumVelocity(int minimumVelocity) {
        mMinimumVelocity = minimumVelocity;
    }

    public int getMaximumVelocity() {
        return mMaximumVelocity;
    }

    /**
     * 设置最大滚动的速度,实际滚动速度的上限
     *
     * @param maximumVelocity 最大滚动速度
     */
    public void setMaximumVelocity(int maximumVelocity) {
        mMaximumVelocity = maximumVelocity;
    }

    private boolean isTextGradual() {
        return mIsTextGradual;
    }

    private void setTextGradual(boolean textGradual) {
        if (mIsTextGradual == textGradual) {
            return;
        }
        mIsTextGradual = textGradual;
        postInvalidate();
    }

    private boolean isShowCurtain() {
        return mIsShowCurtain;
    }

    private void setShowCurtain(boolean showCurtain) {
        if (mIsShowCurtain == showCurtain) {
            return;
        }
        mIsShowCurtain = showCurtain;
        postInvalidate();
    }

    private int getCurtainColor() {
        return mCurtainColor;
    }

    private void setCurtainColor(int curtainColor) {
        if (mCurtainColor == curtainColor) {
            return;
        }
        mCurtainColor = curtainColor;
        postInvalidate();
    }

    private boolean isShowCurtainBorder() {
        return mIsShowCurtainBorder;
    }

    /**
     * 设置幕布是否显示边框
     *
     * @param showCurtainBorder 是否有幕布边框
     */
    private void setShowCurtainBorder(boolean showCurtainBorder) {
        if (mIsShowCurtainBorder == showCurtainBorder) {
            return;
        }
        mIsShowCurtainBorder = showCurtainBorder;
        postInvalidate();
    }

    private int getCurtainBorderColor() {
        return mCurtainBorderColor;
    }

    private void setCurtainBorderColor(int curtainBorderColor) {
        if (mCurtainBorderColor == curtainBorderColor) {
            return;
        }
        mCurtainBorderColor = curtainBorderColor;
        postInvalidate();
    }

    public void setIndicatorText(String indicatorText) {
        mIndicatorText = indicatorText;
        postInvalidate();
    }

    private void setIndicatorTextColor(int indicatorTextColor) {
        mIndicatorTextColor = indicatorTextColor;
        mIndicatorPaint.setColor(mIndicatorTextColor);
        postInvalidate();
    }

    private void setIndicatorTextSize(int indicatorTextSize) {
        mIndicatorTextSize = indicatorTextSize;
        mIndicatorPaint.setTextSize(mIndicatorTextSize);
        postInvalidate();
    }

    /**
     * 设置数据集格式
     *
     * @param dataFormat 格式
     */
    public void setDataFormat(Format dataFormat) {
        mDataFormat = dataFormat;
        postInvalidate();
    }

    public Format getDataFormat() {
        return mDataFormat;
    }

    private T getCurrentSelectValue(List<T> dataList) {
        if (dataList != null && dataList.size() > 0 && mCurrentPosition >= 0 && mCurrentPosition < dataList.size())
            return dataList.get(mCurrentPosition);
        return null;
    }

    public interface OnWheelChangeListener<T> {
        void onWheelSelected(T item, int position);
    }
}