package com.android.airjoy.widget.rain;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class Rain extends View{
	private Context _context;

	public Rain(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this._context=context;
	}

	public Rain(Context context, AttributeSet attrs) {
		super(context, attrs);
		this._context=context;
	}

	public Rain(Context context) {
		super(context);
		this._context=context;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
	}

}
