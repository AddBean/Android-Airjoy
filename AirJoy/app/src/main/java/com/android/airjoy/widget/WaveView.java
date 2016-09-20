package com.android.airjoy.widget;


import com.addbean.autils.tools.ToolsUtils;
import com.android.airjoy.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 波动动画自定义控件；
 *
 * @author 贾豆
 */
public class WaveView extends View {

    private Path aboveWavePath = new Path();
    private Path blowWavePath = new Path();

    private Paint aboveWavePaint = new Paint();
    private Paint blowWavePaint = new Paint();


    private int waveToTop;
    private int aboveWaveColor = 0xffffffff;
    private int blowWaveColor = 0xffffffff;
    private final int default_above_wave_alpha = 240;
    private final int default_blow_wave_alpha = 240;
    private int progress;
    private int mAnimBlowTime = 1;//控制速度，越大越慢；
    private int mAnimTimeBlowCount = 0;
    private int mAnimAboveTime = 1;//控制速度，越大越慢；
    private int mAnimTimeAboveCount = 0;
    private static int x_above_zoom = 80;//波长
    private static int y_above_zoom = 15;//波幅
    private static int x_blow_zoom = 80;//波长
    private static int y_blow_zoom = 15;//波幅
    private float offset = 0.5f;//相位差
    private final float max_right = x_above_zoom *(1f+ offset);

    private float aboveOffset = (float) (Math.PI);//波动画
    private float blowOffset = 0.0f;
    private float animBlowOffset = 0.15f;
    private float animAboveOffset = 0.15f;
    private float mDynamicOffset = 0;

    private float DP=1;

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.waveViewStyle);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DP= ToolsUtils.dpConvertToPx(context,1);
        progress = 50;
        setProgress(progress);
        initializePainters();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(blowWavePath, blowWavePaint);
        canvas.drawPath(aboveWavePath, aboveWavePaint);
        waveToTop = (int) (getHeight() * (1f - progress / 100f));
        calculatePath();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    private void initializePainters() {
        aboveWavePaint.setColor(aboveWaveColor);
        aboveWavePaint.setAlpha(default_above_wave_alpha);
        aboveWavePaint.setStyle(Paint.Style.FILL);
        aboveWavePaint.setAntiAlias(true);

        blowWavePaint.setColor(blowWaveColor);
        blowWavePaint.setAlpha(default_blow_wave_alpha);
        blowWavePaint.setStyle(Paint.Style.FILL);
        blowWavePaint.setAntiAlias(true);
    }

    /**
     * 计算波路径；
     */
    private void calculatePath() {
        mAnimTimeAboveCount++;
        mAnimTimeBlowCount++;
        if (mAnimTimeAboveCount > mAnimAboveTime) {
            aboveWavePath.reset();
            getAboveWaveOffset();

            aboveWavePath.moveTo(getLeft(), getHeight());
            for (float i = 0; x_above_zoom *DP * i <= getRight() + max_right; i += offset) {
                aboveWavePath.lineTo((x_above_zoom *DP * i), (float) (y_above_zoom *DP * Math.cos(i + aboveOffset+mDynamicOffset)) + waveToTop);
            }
            aboveWavePath.lineTo(getRight(), getHeight());
            mAnimTimeAboveCount = 0;
        }
        if (mAnimTimeBlowCount> mAnimBlowTime) {
            blowWavePath.reset();
            getBlowWaveOffset();
            blowWavePath.moveTo(getLeft(), getHeight());
            for (float i = 0; x_blow_zoom *DP * i <= getRight() + max_right; i += offset) {
                blowWavePath.lineTo((x_blow_zoom *DP * i), (float) (y_blow_zoom *DP * Math.cos(i + blowOffset+mDynamicOffset)) + waveToTop);
            }
            blowWavePath.lineTo(getRight(), getHeight());
            mAnimTimeBlowCount = 0;
        }
    }

    public void setProgress(int progress) {
        this.progress = progress > 100 ? 100 : progress;
    }

    private void getAboveWaveOffset() {

        if (aboveOffset > Float.MAX_VALUE - 100) {
            aboveOffset = 0;
        } else {
            aboveOffset += animAboveOffset;
        }
    }

    private void getBlowWaveOffset() {
        if (blowOffset > Float.MAX_VALUE - 100) {
            blowOffset = 0;
        } else {
            blowOffset += animBlowOffset;
        }
    }
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.progress = progress;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setProgress(ss.progress);
    }

    public void setDynamicOffset(float offset) {
        this.mDynamicOffset = offset;
    }

    private static class SavedState extends BaseSavedState {
        int progress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
