package coolx.appcompat.utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.WindowManager;

/**
 * CoolX Design: 屏幕工具类
 */
public class ScreenUtils {
    /**
     * 获取屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Rect windowsBounds = wm.getCurrentWindowMetrics().getBounds();
            point.x = windowsBounds.width();
            point.y = windowsBounds.height();
        } else {
            wm.getDefaultDisplay().getRealSize(point);
        }
        return point.y;
    }
}
