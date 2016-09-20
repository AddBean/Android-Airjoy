package com.android.airjoy.home.fragment.custom.keypad.menu;

/**
 * Created by AddBean on 2016/3/21.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.airjoy.R;

/**
 * Created by AddBean on 2016/3/20.
 */
public class MenuAddDialog extends Dialog {
    public MenuAddView mMenuAddView;
    private Context mContext;

    public MenuAddDialog(Context context, IOnMenuClickListener mOnMenuClickListener) {
        super(context, R.style.transparent_dialog_add);
        this.mContext = context;
        this.mMenuAddView = new MenuAddView(context);
        this.mMenuAddView.setmOnMenuClickListener(mOnMenuClickListener);
        this.setContentView(this.mMenuAddView);
    }

    @Override
    public void show() {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.menu_slide_in);
        this.mMenuAddView.startAnimation(anim);
        super.show();
        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.onWindowAttributesChanged(wl);

    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.menu_slide_out);
        this.mMenuAddView.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismissView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void dismissView() {
        super.dismiss();
    }

}