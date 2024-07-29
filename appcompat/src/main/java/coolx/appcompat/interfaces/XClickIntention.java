package coolx.appcompat.interfaces;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

/**
 * Description: Click 事件封装接口
 */
public interface XClickIntention extends View.OnClickListener {

    <V extends View> V findViewById(@IdRes int id);

    default void setOnClickListener(View... views) {
       setOnClickListener(this, views);
    }

    default void setOnClickListener(@Nullable View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    @Override
    default void onClick(View view) {
        // Not implemented by default, let subclasses implement
    }
}