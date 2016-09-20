package com.android.airjoy.app.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.addbean.autils.utils.AnimUtils;
import com.addbean.aviews.utils.multiadapter.AdapterHelper;
import com.addbean.aviews.utils.multiadapter.ListItemEx;
import com.addbean.aviews.utils.multiadapter.MultiAdapter;
import com.android.airjoy.R;
import com.android.airjoy.app.anim.Rotate3dHelper;
import com.android.airjoy.core.BaseActivity;
import com.android.airjoy.others.umeng.UmengConfig;
import com.android.airjoy.widget.InputDialog;
import com.umeng.analytics.MobclickAgent;

import org.w3c.dom.Text;

/**
 * 帮助页面；
 *
 * @author 贾豆
 */
public class AppHelp extends BaseActivity {
    private ListView mList;
    private MultiAdapter mAdapter;
    private List<ListItemEx> mData = new ArrayList<ListItemEx>();
    public Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setIsShowAd(false);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
        setContentView(R.layout.app_help);
        this._context = this;
        initTital();
        initList();
    }

    private void initList() {
        mList = (ListView) findViewById(R.id.help_listView);
        mAdapter = new MultiAdapter(_context, mData, new MultiAdapter.IAdpterListener() {
            @Override
            public void convert(AdapterHelper helper, MultiAdapter.ConvertViewInf data) {
                QuestionModel model = (QuestionModel) data.getData();
                int index = data.getLayoutId();

                switch (index) {
                    case R.layout.app_help_list:
                        helper.setText(R.id.Help_question, model.getmQuestion());
                        helper.setText(R.id.Help_anwser, model.getmQnwser());
                        break;
                    case R.layout.app_help_list2:
                        setOnclick(data.getView());
                        break;
                }
            }
        });
        mAdapter.addType(R.layout.app_help_list);
        mAdapter.addType(R.layout.app_help_list2);
        mList.setAdapter(mAdapter);
        setData();
        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AnimUtils.ScaleAnim(view);
            }
        });
    }

    private void setOnclick(View view) {
        TextView SendEmil = (TextView) view.findViewById(R.id.SendEmil);
        TextView SendSug = (TextView) view.findViewById(R.id.SendSug);
        SendEmil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SendEmailDialog();
            }
        });
        SendSug.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AppHelp.this, InfDialog.class);
                startActivity(intent);
            }
        });
    }

    private void setData() {
        mData.clear();
        mData.add(new ListItemEx(1, new QuestionModel()));
        mData.add(new ListItemEx(0, new QuestionModel(getString(R.string.question0), getString(R.string.anwser0))));
        mData.add(new ListItemEx(0, new QuestionModel(getString(R.string.question1), getString(R.string.anwser1))));
        mData.add(new ListItemEx(0, new QuestionModel(getString(R.string.question2), getString(R.string.anwser2))));
        mData.add(new ListItemEx(0, new QuestionModel(getString(R.string.question3), getString(R.string.anwser3))));
        mData.add(new ListItemEx(0, new QuestionModel(getString(R.string.question4), getString(R.string.anwser4))));
        mData.add(new ListItemEx(0, new QuestionModel(getString(R.string.question5), getString(R.string.anwser5))));
        mData.add(new ListItemEx(0, new QuestionModel(getString(R.string.question6), getString(R.string.anwser6))));
        mData.add(new ListItemEx(0, new QuestionModel(getString(R.string.question7), getString(R.string.anwser7))));
        mData.add(new ListItemEx(0, new QuestionModel(getString(R.string.question8), getString(R.string.anwser8))));
    }

    /* 初始化标题 */
    private void initTital() {
        final ImageView backImageView = (ImageView) findViewById(R.id.app_back_icon);
        backImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                backImageView.setAlpha(50);
                finish();
            }
        });
    }

    /* 发送邮件*/
    private void SendEmailDialog() {
        final InputDialog dialog = new InputDialog(_context, "我的建议");
        dialog.setEditClickListener(new InputDialog.IOnEditClickListener() {
            @Override
            public void onSubmit(String content) {
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendSuggest(content);
                Toast.makeText(getApplicationContext(),"谢谢您的建议!",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onCancel() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
//
//    /* 调用系统邮件发送*/
//    private void sendEmail(String content) {
//        Intent data = new Intent(Intent.ACTION_SENDTO);
//        data.setData(Uri.parse("mailto:"+getString(R.string.Email)));
//        data.putExtra(Intent.EXTRA_SUBJECT, getTitle());
//        data.putExtra(Intent.EXTRA_TEXT, content);
//        startActivity(Intent.createChooser(data, "发送邮件"));
//}
    private void sendSuggest(String msg) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("content", msg);
        MobclickAgent.onEvent(getApplicationContext(), UmengConfig.SUGGEST, map);
    }

    public class QuestionModel {
        private String mQuestion;
        private String mQnwser;

        public QuestionModel() {
        }

        public QuestionModel(String mQuestion, String mQnwser) {
            this.mQuestion = mQuestion;
            this.mQnwser = mQnwser;
        }

        public String getmQuestion() {
            return mQuestion;
        }

        public void setmQuestion(String mQuestion) {
            this.mQuestion = mQuestion;
        }

        public String getmQnwser() {
            return mQnwser;
        }

        public void setmQnwser(String mQnwser) {
            this.mQnwser = mQnwser;
        }
    }

}
