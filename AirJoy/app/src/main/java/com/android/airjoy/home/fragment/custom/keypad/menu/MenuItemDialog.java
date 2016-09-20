package com.android.airjoy.home.fragment.custom.keypad.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.airjoy.R;
import com.android.airjoy.home.fragment.custom.config.ModelItem;
import com.android.airjoy.home.fragment.custom.keypad.core.ItemLayoutBase;

/**
 * Created by AddBean on 2016/3/23.
 */
public class MenuItemDialog extends Dialog implements IOnMenuClickListener {
    public MenuItemViewBase mMenuView;
    private Context mContext;
    private ItemLayoutBase mItemView;

    public MenuItemDialog(Context context, ItemLayoutBase view) {
        super(context, R.style.transparent_dialog_add);
        this.mContext = context;
        this.mItemView = view;
        this.mMenuView = view.getMenuView();
        this.mMenuView.setmOnMenuClickListener(this);
        this.setContentView(this.mMenuView);

    }

    @Override
    public void show() {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.menu_slide_in);
        this.mMenuView.startAnimation(anim);
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
        this.mMenuView.clearAnimation();
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.menu_slide_out);
        this.mMenuView.startAnimation(anim);
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

    @Override
    public void onDialogConfirm(ModelItem modelItem) {
        this.dismiss();
    }

    @Override
    public void onDialogCancel() {
        this.dismiss();
    }

}