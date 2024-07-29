package com.gorgeous.coolx.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gorgeous.coolx.R;

public class XCard extends LinearLayout {
    private Context mContext;

    private int mCardGradientResource;
    private String mCardTitle;
    private String mCardSummary;

    private TextView mTitleView;
    private TextView mSummaryView;
    private View mCardGradientView;

    public XCard(Context context) {
        this(context, null);
        mContext = context;
    }

    public XCard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public XCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        mContext = context;
    }

    public XCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XCard);
        mCardGradientResource = ta.getResourceId(R.styleable.XCard_cardGradient, 0);
        mCardTitle = ta.getString(R.styleable.XCard_cardTitle);
        mCardSummary = ta.getString(R.styleable.XCard_cardSummary);
        ta.recycle();
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initTagLayout();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initTagLayout() {
        LayoutInflater.from(mContext).inflate(R.layout.x_card_layout, this);
        mCardGradientView = findViewById(R.id.xCardGradient);
        mTitleView = findViewById(R.id.xCardTitle);
        mSummaryView = findViewById(R.id.xCardSummary);

        mCardGradientView.setBackgroundResource(mCardGradientResource);
        mTitleView.setText(mCardTitle);
        mSummaryView.setText(mCardSummary);
    }
}
