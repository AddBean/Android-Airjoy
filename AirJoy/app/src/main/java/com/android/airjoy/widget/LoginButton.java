package com.android.airjoy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
public class LoginButton extends Button
{
    public LoginButton(Context context)
    {
        super(context);
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LoginButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

//    @SuppressLint("DrawAllocation")
//    @Override
//    protected void onDraw(Canvas canvas)
//    {
//        Paint paint = new Paint();
//        paint.setColor(color.bluejeans_2);
//        canvas.drawRoundRect(
//                new RectF(2 + this.getScrollX(), 2 + this.getScrollY(),
//                          this.getWidth() - 3 + this.getScrollX(),
//                          this.getHeight() + this.getScrollY() - 1), 12, 12,
//                paint);
//        super.onDraw(canvas);
//    }

}
