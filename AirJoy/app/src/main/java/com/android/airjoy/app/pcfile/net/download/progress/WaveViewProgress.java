package com.android.airjoy.app.pcfile.net.download.progress;

import com.android.airjoy.R;
import com.android.airjoy.sensor.Gsensor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

/**
 * 波浪进度条类；
 *
 * @author 贾豆
 *
 */
public class WaveViewProgress extends View {

	private Path aboveWavePath = new Path();
	private Path blowWavePath = new Path();

	private Paint aboveWavePaint = new Paint();
	private Paint blowWavePaint = new Paint();
	private Paint textPaint = new Paint();
	private Paint rainPaint = new Paint();

	private final int default_above_wave_color = Color.DKGRAY;
	private final int default_blow_wave_color = 0x636363;
	private final int default_progress = 80;

	private int waveToTop;
	private int aboveWaveColor = 0xffffffff;
	private int blowWaveColor = 0xffffffff;
	private final int default_above_wave_alpha = 220;
	private final int default_blow_wave_alpha = 220;
	private int progress;

	private int offsetIndex = 0;
	private int animTime = 1;// 控制速度，越大越慢；
	private int animTimeCount = 0;
	/** 波长 */
	private static int x_zoom = 200;
	/** 波幅 */
	private static int y_zoom = 25;
	/** offset of X */
	private float offset = 0.5f;
	private final float max_right = x_zoom * offset;
	/** 字体位置 */
	private float text_x, text_y;
	// 波动画
	private float aboveOffset = 0.0f;
	private float blowOffset = 4.0f;
	/** offset of Y */
	private float animOffset = 0.15f;
	private Gsensor gsensor;
	// 刷新线程；
	private RefreshProgressRunnable mRefreshProgressRunnable;

	public WaveViewProgress(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.waveViewStyle);
	}

	public WaveViewProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		progress = 0;
		setProgress(progress);
		initializePainters();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(blowWavePath, blowWavePaint);
		canvas.drawPath(aboveWavePath, aboveWavePaint);
		String text = String.valueOf(progress);
		textPaint.setColor(Color.RED);
		textPaint.setAntiAlias(true);
		// canvas.drawText(text, 350, text_y+100, textPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measure(widthMeasureSpec, true),
				measure(heightMeasureSpec, false));
	}

	private int measure(int measureSpec, boolean isWidth) {
		int result;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		int padding = isWidth ? getPaddingLeft() + getPaddingRight()
				: getPaddingTop() + getPaddingBottom();
		if (mode == MeasureSpec.EXACTLY) {
			result = size;
		} else {
			result = isWidth ? getSuggestedMinimumWidth()
					: getSuggestedMinimumHeight();
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
		aboveWavePaint.setTextSize(50);

		blowWavePaint.setColor(blowWaveColor);
		blowWavePaint.setAlpha(default_blow_wave_alpha);
		blowWavePaint.setStyle(Paint.Style.FILL);
		blowWavePaint.setAntiAlias(true);
	}

	/**
	 * 计算波路径；
	 */
	private void calculatePath() {

		// y_zoom=y_zoom*(int)Math.abs(gsensor.getCurrentValue()[0]);
		// x_zoom=y_zoom*(int)Math.abs(gsensor.getCurrentValue()[1]);
		animTimeCount++;
		if (animTimeCount > animTime) {
			aboveWavePath.reset();
			blowWavePath.reset();
			getWaveOffset();

			aboveWavePath.moveTo(getLeft(), getHeight());
			for (float i = 0; x_zoom * i <= getRight() + max_right; i += offset) {
				aboveWavePath.lineTo((x_zoom * i),
						(float) (y_zoom * Math.cos(i + aboveOffset))
								+ waveToTop);
			}
			aboveWavePath.lineTo(getRight(), getHeight());

			blowWavePath.moveTo(getLeft(), getHeight());
			for (float i = 0; x_zoom * i <= getRight() + max_right; i += offset) {
				blowWavePath
						.lineTo((x_zoom * i),
								(float) (y_zoom * Math.cos(i + blowOffset))
										+ waveToTop);
				text_x = (x_zoom * i);
				text_y = (float) (y_zoom * Math.cos(i + blowOffset))
						+ waveToTop;
			}
			blowWavePath.lineTo(getRight(), getHeight());
			animTimeCount = 0;
		} else {
		}

	}

	public void setProgress(int progress) {
		this.progress = progress > 100 ? 100 : progress;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mRefreshProgressRunnable = new RefreshProgressRunnable();
		post(mRefreshProgressRunnable);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		removeCallbacks(mRefreshProgressRunnable);
	}

	private void getWaveOffset() {
		if (blowOffset > Float.MAX_VALUE - 100) {
			blowOffset = 0;
		} else {
			blowOffset += animOffset;
		}

		if (aboveOffset > Float.MAX_VALUE - 100) {
			aboveOffset = 0;
		} else {
			aboveOffset += animOffset;
		}
	}

	@Override
	public Parcelable onSaveInstanceState() {
		// Force our ancestor class to save its state
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

	private class RefreshProgressRunnable implements Runnable {
		public void run() {
			synchronized (WaveViewProgress.this) {
				waveToTop = (int) (getHeight() * (1f - progress / 100f));
				calculatePath();
				invalidate();
				postDelayed(this, 16);
			}
		}
	}

	private static class SavedState extends BaseSavedState {
		int progress;

		/**
		 * Constructor called from {@link ProgressBar#onSaveInstanceState()}
		 */
		SavedState(Parcelable superState) {
			super(superState);
		}

		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private SavedState(Parcel in) {
			super(in);
			progress = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(progress);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}
