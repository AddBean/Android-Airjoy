package com.android.airjoy.widget.rain;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class Cloud extends View{
	private Context _context;

	public Cloud(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this._context=context;
	}

	public Cloud(Context context, AttributeSet attrs) {
		super(context, attrs);
		this._context=context;
	}

	public Cloud(Context context) {
		super(context);
		this._context=context;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
	}
}
