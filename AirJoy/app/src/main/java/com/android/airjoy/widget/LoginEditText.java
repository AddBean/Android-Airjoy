package com.android.airjoy.widget;

import com.android.airjoy.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * @author sunday
 *         2013-12-04
 */
public class LoginEditText extends EditText
{
    private final static String TAG = "EditTextWithDel";
    private Drawable imgAble;
    private Context mContext;

    public LoginEditText(Context context)
    {
        super(context);
        mContext = context;
        init();
    }

    public LoginEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public LoginEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init()
    {
        imgAble = mContext.getResources().getDrawable(R.drawable.edit_delet);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                setDrawable();
            }
        });
        setDrawable();
    }

    // ����ɾ��ͼƬ
    private void setDrawable()
    {
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
    }

    // ����ɾ���¼�
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP)
        {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas)
    {
        if (this.isFocused() == true){
            canvas.drawColor(0xf0ffffff);
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
        }
        else{
            canvas.drawColor(0xffffffff);
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        super.onDraw(canvas);
    }
}
