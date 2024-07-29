package coolx.appcompat.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import coolx.appcompat.R;
import coolx.appcompat.core.view.LoadingBase;

/**
 * Description: 一种 Loading 圆圈加载动效，匀速
 * Created by marco, at 2024/07/25
 */
public class CircularLoadingView extends LoadingBase {

    private Paint mPaint;             // 圆弧画笔
    private Paint mPaintPro;          // 圆形画笔

    private float mWidth = 0f;
    private float mPadding = 0f;
    private float startAngle = 0f;    // 圆弧起始角度
    private RectF rectF;

    public CircularLoadingView(Context context) {
        super(context);
    }

    public CircularLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void doAnimation() {
        setupCircleColor(getContext().getResources().getColor(R.color.coolx_loading_view_circle_color, null));
        setupArcColor(getContext().getResources().getColor(R.color.coolx_loading_view_arc_color, null));
        startAnim(1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > getHeight()) {
            mWidth = getMeasuredHeight();
        } else {
            mWidth = getMeasuredWidth();
        }
        mPadding = 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2 - mPadding, mPaintPro/* 画笔 */);
        if (rectF == null) {
            rectF = new RectF();
        }
        rectF.set(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
        canvas.drawArc(rectF, startAngle, 250/* 圆弧角度 */, false/* 是否显示半径连线 */, mPaint);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);               // 抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);     // 设置画笔样式
        mPaint.setStrokeCap(Paint.Cap.ROUND);    // 笔触风格
        mPaint.setColor(Color.WHITE);            // 画笔颜色
        mPaint.setStrokeWidth(8);                // 画笔线宽

        mPaintPro = new Paint();
        mPaintPro.setAntiAlias(true);
        mPaintPro.setStyle(Paint.Style.STROKE);
        mPaintPro.setColor(Color.argb(100, 255, 255, 255));
        mPaintPro.setStrokeWidth(8);
    }

    public void setupCircleColor(int color) {
        mPaintPro.setColor(color);
        postInvalidate();
    }

    public void setupArcColor(int color) {
        mPaint.setColor(color);
        postInvalidate();
    }

    @Override
    protected void InitPaint() {
        initPaint();
    }

    @Override
    protected void OnAnimationUpdate(ValueAnimator valueAnimator) {
        float value = (float) valueAnimator.getAnimatedValue();
        startAngle = 360 * value;
        invalidate();
    }

    @Override
    protected void OnAnimationRepeat(Animator animation) {
    }

    @Override
    protected int OnStopAnim() {
        return 0;
    }

    @Override
    protected int SetAnimRepeatMode() {
        return ValueAnimator.RESTART;    /* 设置重复模式，ValueAnimator.RESTART：正序重新开始 */
    }

    @Override
    protected void AinmIsRunning() {

    }
    @Override
    protected int SetAnimRepeatCount() {
        return ValueAnimator.INFINITE;  /* 动画循环次数：无限循环 */
    }
}
