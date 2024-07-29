package coolx.appcompat.core;

import android.icu.util.Calendar;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

public abstract class SingleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 500;

    private long lastClickTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            singleClick(v);
        }
    }

    protected abstract void singleClick(View v);
}
