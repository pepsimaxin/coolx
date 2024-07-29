package coolx.appcompat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import coolx.appcompat.R;
import coolx.appcompat.widget.CircularLoadingView;

public class LoadingDoneView extends LinearLayout {
    private String endMessage;
    private AppCompatTextView mLoadingMessage;
    private CircularLoadingView mLoadingView;
    private ImageView mDoneIcon;

    public LoadingDoneView(Context context, String startMessage, String endMessage, long time) {
        this(context, null);
        this.endMessage = endMessage;
        initView(context, startMessage, endMessage, time);
    }

    public LoadingDoneView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingDoneView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LoadingDoneView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, String startMessage, String endMessage, long time) {
        LayoutInflater.from(context).inflate(R.layout.coolx_preset_layout_loading_done, this);
        mLoadingView = findViewById(R.id.loadingView);
        mDoneIcon = findViewById(R.id.doneIcon);
        mLoadingMessage = findViewById(R.id.loadingMessage);
        mLoadingMessage.setText(startMessage);
        mLoadingView.doAnimation();
        postDelayed(animationTask, time);
    }

    protected Runnable animationTask = new Runnable() {
        @Override
        public void run() {
            mLoadingView.stopAnim();
            mLoadingView.setVisibility(View.GONE);
            mDoneIcon.setVisibility(View.VISIBLE);
            mLoadingMessage.setText(endMessage);
        }
    };
}
