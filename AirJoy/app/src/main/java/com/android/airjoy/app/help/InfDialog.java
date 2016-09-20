package com.android.airjoy.app.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.airjoy.R;
import com.android.airjoy.app.anim.Rotate3dHelper;

/**
 * 关于页面
 *
 * @author 贾豆
 *
 */
public class InfDialog extends Activity
{
    public Context _context;
    private LinearLayout _share;
    private Rotate3dHelper _shareAnim;
    private TextView _infEmail;
    private TextView _infWeibo;
    private TextView _infQq;
    private TextView _infVision;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        setContentView(R.layout.inf_dialog);
        initTital();
        this._infEmail = (TextView) findViewById(R.id.infEmail);
        this._infEmail.setText("邮箱：" + getString(R.string.Email));
        this._infWeibo = (TextView) findViewById(R.id.infWeibo);
        this._infWeibo.setText("微博：" + getString(R.string.Weibo));
        this._infQq = (TextView) findViewById(R.id.infQq);
        this._infQq.setText("官方群：" + getString(R.string.Qq));
        this._infVision = (TextView) findViewById(R.id.infVision);
        this._infVision.setText(getVersionName());
        this._context = this;
        this._share = (LinearLayout) findViewById(R.id.share);
        this._shareAnim = new Rotate3dHelper(this._share);
        this._share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0)
            {
                _shareAnim.Rotate3dByX(200, 0, 90, 10, true,
                        new Rotate3dHelper.OnAnimOverListener() {
                            @Override
                            public void AnimOver()
                            {
                                shareMsg("Airjoy",
                                        getString(R.string.shareContent)+"。"+getString(R.string.DownloadUrl),
                                        "app_ad.png");
                            }
                        });
            }
        });
    }

    /**
     * 分享功能
     *
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public void shareMsg(String msgTitle, String msgText, String imgPath)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    /* 初始化标题 */
    private void initTital()
    {
        final ImageView backImageView = (ImageView) findViewById(R.id.app_back_icon);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                backImageView.setAlpha(50);
                finish();
            }
        });
    }

    /* 获取当期版本号 */
    private String getVersionName()
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try
        {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        }
        catch (NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }
}
