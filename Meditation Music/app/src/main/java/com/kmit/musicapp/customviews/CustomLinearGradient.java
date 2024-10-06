package com.kmit.musicapp.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.kmit.musicapp.R;
import com.kmit.musicapp.activities.HomeActivity;

/**
 * Created by Harjot on 26-Dec-16.
 */

public class CustomLinearGradient extends View {

    private int one,eig;
    Paint paint;
    int startColor, midColor, endColor;
    int alpha;

    public CustomLinearGradient(Context context) {
        super(context);
        init();
    }

    public CustomLinearGradient(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomLinearGradient(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.background_one, typedValue, true);
        one = typedValue.data;
        theme.resolveAttribute(R.attr.realtimeOverlayColor_set_one, typedValue, true);
        eig = typedValue.data;

    }

    void init() {
        paint = new Paint();
        alpha = 140;
        startColor = Color.argb(alpha, Color.red(HomeActivity.themeColor), Color.green(HomeActivity.themeColor), Color.blue(HomeActivity.themeColor));
        midColor = eig;
        endColor = one;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setStartColor(int color) {
        startColor = color;
    }

    void setEndColor(int color) {
        endColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startColor = Color.argb(alpha, Color.red(HomeActivity.themeColor), Color.green(HomeActivity.themeColor), Color.blue(HomeActivity.themeColor));
        midColor = eig;
        endColor = one;
//        paint.setShader(new LinearGradient(0, 0, 0, getHeight(), new int[]{startColor, midColor, endColor}, new float[]{0.0f, 0.35f, 1.0f}, Shader.TileMode.MIRROR));
        paint.setShader(new LinearGradient(0, 0, 0, getHeight(), startColor, endColor, Shader.TileMode.CLAMP));
        canvas.drawPaint(paint);
    }
}
