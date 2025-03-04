package com.kmit.musicapp.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.kmit.musicapp.R;
import com.kmit.musicapp.activities.HomeActivity;

/**
 * Created by Harjot on 23-May-16.
 */
public class AnalogController extends View {

    static float width, height;
    private int one,zero,two;
    float midx, midy;
    Paint textPaint;
    Paint circlePaint;
    public Paint circlePaint2;
    public Paint linePaint;
    String angle;
    float currdeg, deg = 3, downdeg, prevCurrDeg;
    boolean isIncreasing, isDecreasing;

    int progressColor, lineColor;

    onProgressChangedListener mListener;

    String label;

    public interface onProgressChangedListener {
        void onProgressChanged(int progress);
    }

    public void setOnProgressChangedListener(onProgressChangedListener listener) {
        mListener = listener;
    }

    public AnalogController(Context context) {
        super(context);
        init();
    }

    public AnalogController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnalogController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.background_one, typedValue, true);
        one = typedValue.data;
        theme.resolveAttribute(R.attr.background_zero, typedValue, true);
        zero = typedValue.data;
        theme.resolveAttribute(R.attr.background_two, typedValue, true);
        two = typedValue.data;

    }

    void init() {
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(33 * HomeActivity.ratio);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        circlePaint = new Paint();
        circlePaint.setColor(two);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint2 = new Paint();
        circlePaint2.setColor(HomeActivity.themeColor);
//        circlePaint2.setColor(Color.parseColor("FFA036"));
        circlePaint2.setStyle(Paint.Style.FILL);
        linePaint = new Paint();
        linePaint.setColor(HomeActivity.themeColor);
//        linePaint.setColor(Color.parseColor("FFA036"));
        linePaint.setStrokeWidth(7 * HomeActivity.ratio);
        angle = "0.0";
        label = "Label";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        midx = canvas.getWidth() / 2;
        midy = canvas.getHeight() / 2;

        int ang = 0;
        float x = 0, y = 0;
        int radius = (int) (Math.min(midx, midy) * ((float) 14.5 / 16));
        float deg2 = Math.max(3, deg);
        float deg3 = Math.min(deg, 21);
        for (int i = (int) (deg2); i < 22; i++) {
            float tmp = (float) i / 24;
            x = midx + (float) (radius * Math.sin(2 * Math.PI * (1.0 - tmp)));
            y = midy + (float) (radius * Math.cos(2 * Math.PI * (1.0 - tmp)));

            circlePaint.setColor(one);
            canvas.drawCircle(x, y, ((float) radius / 15), circlePaint);
        }
        for (int i = 3; i <= deg3; i++) {
            float tmp = (float) i / 24;
            x = midx + (float) (radius * Math.sin(2 * Math.PI * (1.0 - tmp)));
            y = midy + (float) (radius * Math.cos(2 * Math.PI * (1.0 - tmp)));
            canvas.drawCircle(x, y, ((float) radius / 15), circlePaint2);
        }

        float tmp2 = (float) deg / 24;
        float x1 = midx + (float) (radius * ((float) 2 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
        float y1 = midy + (float) (radius * ((float) 2 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));
        float x2 = midx + (float) (radius * ((float) 3 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
        float y2 = midy + (float) (radius * ((float) 3 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));

        circlePaint.setColor(two);
        canvas.drawCircle(midx, midy, radius * ((float) 13 / 15), circlePaint);
        circlePaint.setColor(zero);
        canvas.drawCircle(midx, midy, radius * ((float) 11 / 15), circlePaint);
        canvas.drawText(label, midx, midy + (float) (radius * 1.1), textPaint);
        canvas.drawLine(x1, y1, x2, y2, linePaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        mListener.onProgressChanged((int) (deg - 2));

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            float dx = e.getX() - midx;
            float dy = e.getY() - midy;
            downdeg = (float) ((Math.atan2(dy, dx) * 180) / Math.PI);
            downdeg -= 90;
            if (downdeg < 0) {
                downdeg += 360;
            }
            downdeg = (float) Math.floor(downdeg / 15);
            return true;
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = e.getX() - midx;
            float dy = e.getY() - midy;
            currdeg = (float) ((Math.atan2(dy, dx) * 180) / Math.PI);
            currdeg -= 90;
            if (currdeg < 0) {
                currdeg += 360;
            }
            currdeg = (float) Math.floor(currdeg / 15);

            if (currdeg == 0 && downdeg == 23) {
                deg++;
                if (deg > 21) {
                    deg = 21;
                }
                downdeg = currdeg;
            } else if (currdeg == 23 && downdeg == 0) {
                deg--;
                if (deg < 3) {
                    deg = 3;
                }
                downdeg = currdeg;
            } else {
                deg += (currdeg - downdeg);
                if (deg > 21) {
                    deg = 21;
                }
                if (deg < 3) {
                    deg = 3;
                }
                downdeg = currdeg;
            }

            angle = String.valueOf(String.valueOf(deg));
            invalidate();
            return true;
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    public int getProgress() {
        return (int) (deg - 2);
    }

    public void setProgress(int x) {
        deg = x + 2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String txt) {
        label = txt;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }
}
