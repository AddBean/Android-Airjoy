package com.android.airjoy.home.fragment.custom.keypad.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.addbean.autils.tools.ToolsUtils;
import com.addbean.autils.utils.ALog;
import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.keypad.FragmentPadEdit;

/**
 * Created by AddBean on 2016/3/21.
 */
public class ItemLayoutEdit extends ViewGroup {
    private ItemLayoutBase mContent;
    private final int OFFSET_TOP_DP = 48;
    private final int OFFSET_AROUND_DP = 18;
    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mWidth = 0;
    private int mHeigh = 0;
    private float mDownTrackRawX;
    private float mDownTrackRawY;
    private float mDownTrackX;
    private float mDownTrackY;
    private EControlType mControlType = null;
    private int DP = -1;
    private final float MIN_WIDTH = 10;
    private final float MIN_HEIGHT = 10;
    private boolean mIsShowEditView = false;
    private String mTitle = "命令";
    private ModelItem mModelItem;

    public ItemLayoutEdit(Context context, ItemLayoutBase content) {
        super(context);
        this.mContent = content;
        this.mModelItem = content.getmModelItem();
        this.addView(content);
        DP = ToolsUtils.dpConvertToPx(context, 1);
        initView();
    }

    private void initView() {
        this.setRotation(mModelItem.getmAngle());
        this.setX(mModelItem.getmX());
        this.setY(mModelItem.getmY());
        ViewGroup.LayoutParams layoutParams = this.mContent.getLayoutParams();
        this.mContent.setSize(mModelItem.getmWidth(), mModelItem.getmHeight());
        this.setLayoutParams(layoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ItemLayoutBase) {
                view.layout(mPaddingLeft, mPaddingTop, mPaddingLeft + view.getMeasuredWidth(), mPaddingTop + view.getMeasuredHeight());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int viewWidth = 0, viewHeight = 0;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ItemLayoutBase) {
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                viewWidth = view.getMeasuredWidth();
                viewHeight = view.getMeasuredHeight();

                mPaddingTop = OFFSET_TOP_DP * DP;
                mPaddingBottom = OFFSET_AROUND_DP * DP;
                mPaddingLeft = OFFSET_AROUND_DP * DP;
                mPaddingRight = OFFSET_AROUND_DP * DP;
                mWidth = 2 * OFFSET_AROUND_DP * DP + viewWidth;
                mHeigh = OFFSET_AROUND_DP * DP + OFFSET_TOP_DP * DP + viewHeight;
            }
        }
        setMeasuredDimension(mWidth, mHeigh);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!FragmentPadEdit.mEditEnable) {
            return super.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!FragmentPadEdit.mEditEnable) {
            return super.onTouchEvent(event);
        }
        boolean result = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownTrackRawX = event.getRawX();
                mDownTrackRawY = event.getRawY();
                mDownTrackX = event.getX();
                mDownTrackY = event.getY();
                mControlType = getControlType(event);
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onClick(this, getControlType(event));
                break;
            case MotionEvent.ACTION_MOVE:
                float delatRawX = event.getRawX() - mDownTrackRawX;
                float delatRawY = event.getRawY() - mDownTrackRawY;
                result = handleControl(event, delatRawX, delatRawY);
                mDownTrackRawX = event.getRawX();
                mDownTrackRawY = event.getRawY();
                mDownTrackX = event.getX();
                mDownTrackY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return result;
    }

    private boolean handleControl(MotionEvent event, float delatX, float delatY) {
        if (mControlType == null)
            return false;
        switch (mControlType) {
            case LEFT:
            case RIGHT:
            case TOP:
            case BOTTOM:
            case TOP_LEFT:
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
                setSize(mControlType, event.getX() - mDownTrackX, event.getY() - mDownTrackY);
                break;
            case BOTTOM_LEFT:
                setRotate(event.getRawX(), event.getRawY());
                break;
            case COONTENT:
                setPosition(this.getX() + delatX, this.getY() + delatY);
                break;
            case MOVE:
                setPosition(this.getX() + delatX, this.getY() + delatY);
                break;
        }
        invalidate();
        return true;
    }

    private void setSize(EControlType type, float delatX, float delatY) {
        ViewGroup.LayoutParams layoutParams = this.mContent.getLayoutParams();
        if (type == EControlType.BOTTOM_RIGHT) {
            if (this.mContent.getMeasuredWidth() > MIN_WIDTH * DP || delatX > 0) {
                this.mContent.setSize((int) (mContent.getWidth() + delatX), -1);
            }
            if (this.mContent.getMeasuredHeight() > MIN_HEIGHT * DP || delatY > 0) {
                this.mContent.setSize(-1, (int) (mContent.getHeight() + delatY));
            }
            this.mContent.setLayoutParams(layoutParams);
            mContent.onAllScaled();
        } else if (type == EControlType.RIGHT) {
            if (this.mContent.getMeasuredWidth() > MIN_WIDTH * DP || delatX > 0) {
                this.mContent.setSize((int) (mContent.getWidth() + delatX), (int) (mContent.getHeight()));
            }
            this.mContent.setLayoutParams(layoutParams);
            mContent.onXScaled();
        } else if (type == EControlType.BOTTOM) {
            if (this.mContent.getMeasuredHeight() > MIN_HEIGHT * DP || delatY > 0) {
                this.mContent.setSize((int) (mContent.getWidth()), (int) (mContent.getHeight() + delatY));
            }
            this.mContent.setLayoutParams(layoutParams);
            mContent.onYScaled();
        }
        saveData();
    }

    /**
     * 图片的旋转角度
     */
    private float mDegree = 0;

    private void setRotate(float endX, float endY) {
        PointF mCenterPoint = new PointF(this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() / 2 + mPaddingTop / 2);
        PointF mPreMovePointF = new PointF((int) mDownTrackRawX, (int) mDownTrackRawY);
        PointF mCurMovePointF = new PointF((int) endX, (int) endY);
        // 角度
        double a = distance4PointF(mCenterPoint, mPreMovePointF);
        double b = distance4PointF(mPreMovePointF, mCurMovePointF);
        double c = distance4PointF(mCenterPoint, mCurMovePointF);
        double cosb = (a * a + c * c - b * b) / (2 * a * c);
        if (cosb >= 1) {
            cosb = 1f;
        }
        double radian = Math.acos(cosb);
        float newDegree = (float) radianToDegree(radian);
        //center -> proMove的向量，
        PointF centerToProMove = new PointF((mPreMovePointF.x - mCenterPoint.x), (mPreMovePointF.y - mCenterPoint.y));
        //center -> curMove 的向量
        PointF centerToCurMove = new PointF((mCurMovePointF.x - mCenterPoint.x), (mCurMovePointF.y - mCenterPoint.y));
        //向量叉乘结果, 如果结果为负数， 表示为逆时针， 结果为正数表示顺时针
        float result = centerToProMove.x * centerToCurMove.y - centerToProMove.y * centerToCurMove.x;
        if (result < 0) {
            newDegree = -newDegree;
        }
        mDegree = mDegree + newDegree;
        this.setPivotX(this.getWidth() / 2);
        this.setPivotY(this.getHeight() / 2 + mPaddingTop / 2);
        this.setRotation(mDegree);
        saveData();
        mContent.onRotated();
    }

    private void saveData() {
        mModelItem.setmAngle(mDegree);
        mModelItem.setmX((int) this.getX());
        mModelItem.setmY((int) this.getY());
        mModelItem.setmHeight(mContent.getMeasuredHeight());
        mModelItem.setmWidth(mContent.getMeasuredWidth());
    }

    /**
     * 弧度换算成角度
     *
     * @return
     */
    public static double radianToDegree(double radian) {
        return radian * 180 / Math.PI;
    }

    private float distance4PointF(PointF pf1, PointF pf2) {
        float disX = pf2.x - pf1.x;
        float disY = pf2.y - pf1.y;
        return FloatMath.sqrt(disX * disX + disY * disY);
    }

    public void setPosition(float x, float y) {
        this.setX((int) x);
        this.setY((int) y);
        saveData();
        invalidate();
        mContent.onMoved();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mIsShowEditView && FragmentPadEdit.mEditEnable) {
            drawBg(canvas);
            drawControlLine(canvas);
            drawIcons(canvas);
        }
        super.dispatchDraw(canvas);
    }

    private void drawIcons(Canvas canvas) {
        Paint paint = new Paint();
        int w = OFFSET_AROUND_DP * DP * 2 / 3;
        int h = OFFSET_AROUND_DP * DP * 2 / 3;
        int pad = 3 * DP;
        //删除按钮
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.edit_icon_delet);
        canvas.drawBitmap(resizeBitmap(bitmap, w, h),
                pad, mPaddingTop - OFFSET_AROUND_DP * DP + pad, paint);
        //编辑按钮
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.edit_icon_edit);
        canvas.drawBitmap(resizeBitmap(bitmap1, w, h),
                getMeasuredWidth() - pad - w, mPaddingTop - OFFSET_AROUND_DP * DP + pad, paint);

        //旋转按钮
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.edit_icon_rotata);
        canvas.drawBitmap(resizeBitmap(bitmap2, w, h),
                pad, getMeasuredHeight() - pad - h, paint);

        //扩大按钮
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.edit_icon_scale);
        canvas.drawBitmap(resizeBitmap(bitmap3, w, h),
                getMeasuredWidth() - pad - w, getMeasuredHeight() - pad - h, paint);

        //右边扩大按钮
        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.edit_icon_right);
        canvas.drawBitmap(resizeBitmap(bitmap4, w / 2, 3 * h),
                getMeasuredWidth() - pad - w / 2, (getMeasuredHeight()) / 2 - 1.5f * h + OFFSET_TOP_DP - OFFSET_AROUND_DP, paint);

        //右边扩大按钮
        Bitmap bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.edit_icon_down);
        canvas.drawBitmap(resizeBitmap(bitmap5, 3 * w, h / 2),
                getMeasuredWidth() / 2 - 1.5f * w, getMeasuredHeight() - pad - w / 2, paint);
    }


    public Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = w;
            int newHeight = h;
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {
            return null;
        }
    }

    private void drawControlLine(Canvas canvas) {
        setmTitle(TextUtils.isEmpty(mModelItem.getmCmdName()) ? "" : mModelItem.getmCmdName());
        int textSize = 12;
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(textSize * DP);
        int x = getMeasuredWidth() / 2 - ToolsUtils.getTextWidth(mTitle, textSize) * DP / 2;
        int y = (int) ((mPaddingTop - OFFSET_AROUND_DP * DP - 1.3 * DP) / 2 + textSize * DP / 2);
        if (OFFSET_TOP_DP != OFFSET_AROUND_DP)
            canvas.drawText(mTitle, x, y, paint);
        paint.setStrokeWidth(DP);
        PathEffect effects = new DashPathEffect(new float[]{DP, DP, DP, DP}, 1);
        paint.setPathEffect(effects);
        RectF rect = new RectF(
                OFFSET_AROUND_DP * DP - 2 * DP,
                (float) (mPaddingTop - OFFSET_AROUND_DP * DP) + OFFSET_AROUND_DP * DP - 2 * DP,
                (float) (getMeasuredWidth()) - OFFSET_AROUND_DP * DP + 2 * DP,
                (float) (getMeasuredHeight() - OFFSET_AROUND_DP * DP + 2 * DP));
        canvas.drawRect(rect, paint);
    }


    private int mBgColor = 0x20000000;

    private void drawBg(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mBgColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(DP);
        canvas.drawRect(new RectF(0, 0, (float) (getMeasuredWidth()), (float) (mPaddingTop - OFFSET_AROUND_DP * DP - 1.3 * DP)), paint);
        canvas.drawRect(new RectF(0, (float) (mPaddingTop - OFFSET_AROUND_DP * DP), (float) (getMeasuredWidth()), (float) (getMeasuredHeight())), paint);

    }

    public void setEditEnable(boolean isEdit) {
        this.mIsShowEditView = isEdit;
        mContent.onModelChanged(isEdit);
        invalidate();
    }

    public boolean isEditEnable() {
        return this.mIsShowEditView;
    }

    private EControlType getControlType(MotionEvent event) {
        float x = event.getX();//相对坐标；
        float y = event.getY();
        if (x < mPaddingLeft) {
            if (y < mPaddingTop && y > OFFSET_TOP_DP * DP - OFFSET_AROUND_DP * DP) {//左上角
                ALog.e("左上角");
                mContent.onDeleted();
                return EControlType.TOP_LEFT;
            } else if (y > mPaddingTop && y < mHeigh - mPaddingBottom) {//左侧
                ALog.e("左侧");
                return EControlType.LEFT;
            } else if (y > mHeigh - mPaddingBottom) {//左下角
                ALog.e("左下角");
                return EControlType.BOTTOM_LEFT;
            }
        } else if (x > mPaddingLeft && x < mWidth - mPaddingRight) {
            if (y < mPaddingTop - OFFSET_AROUND_DP * DP) {//移动；
                ALog.e("移动");
                return EControlType.MOVE;
            } else if (y > mPaddingTop - OFFSET_AROUND_DP * DP && y < mPaddingTop) {//上部
                ALog.e("上部");
                return EControlType.TOP;
            } else if (y > mPaddingTop && y < mHeigh - mPaddingBottom) {//点击内容
                ALog.e("点击内容");
                return EControlType.COONTENT;
            } else if (y > mHeigh - mPaddingBottom) {//下部
                ALog.e("下部");
                return EControlType.BOTTOM;
            }
        } else if (x > mWidth - mPaddingRight) {
            if (y < mPaddingTop && y > OFFSET_TOP_DP * DP - OFFSET_AROUND_DP * DP) {//右上角
                ALog.e("右上角");
                return EControlType.TOP_RIGHT;
            } else if (y > mPaddingTop && y < mHeigh - mPaddingBottom) {//右侧
                ALog.e("右侧");
                return EControlType.RIGHT;
            } else if (y > mHeigh - mPaddingBottom) {//右下角
                ALog.e("右下角");
                return EControlType.BOTTOM_RIGHT;
            }
        }
        return null;
    }


    public enum EControlType {
        LEFT(0),
        RIGHT(1),
        TOP(2),
        BOTTOM(3),
        TOP_LEFT(4),
        TOP_RIGHT(5),
        BOTTOM_LEFT(6),
        BOTTOM_RIGHT(7),
        COONTENT(3),
        MOVE(4);
        private int mType;

        EControlType(int type) {
            this.mType = type;
        }
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public ItemLayoutBase getmContent() {
        return mContent;
    }


    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onClick(ItemLayoutEdit view, EControlType type);
    }

    public void onFragmentStop(){
        mContent.onStop();
    }
}
