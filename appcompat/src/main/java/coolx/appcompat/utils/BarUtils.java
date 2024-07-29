package coolx.appcompat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

/**
 * CoolX Design: 状态栏工具类
 */
public final class BarUtils {
    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        @SuppressLint("DiscouragedApi")
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取导航栏高度
     */
    public static int getNavBarHeight() {
        Resources resources = Resources.getSystem();
        @SuppressLint("DiscouragedApi")
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 判断导航栏是否显示
     */
    public static boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        @SuppressLint("DiscouragedApi")
        int resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android");
        return resources.getInteger(resourceId) != 2;
    }
}
