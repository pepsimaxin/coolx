package coolx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import coolx.appcompat.R;

/**
 * Toolbar 定制，平替 androidx.appcompat.widget.Toolbar
 */
public class Toolbar extends androidx.appcompat.widget.Toolbar {
    private final TextView title;

    public Toolbar(@NonNull Context context) {
        this(context, null);
    }

    public Toolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public Toolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.coolx_android_widget_toolbar, this);
        title = findViewById(R.id.coolx_title);
    }

    @Override
    public void setTitle(@StringRes int id) {
        setTitle(getResources().getString(id));
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        title.setText(charSequence);
    }

    @Override
    public CharSequence getTitle() {
        return title.getText();
    }
}
