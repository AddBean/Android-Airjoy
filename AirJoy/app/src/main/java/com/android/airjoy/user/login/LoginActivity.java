package com.android.airjoy.user.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.android.airjoy.R;
import com.android.airjoy.constant.Config;
import com.android.airjoy.user.bean.User;
import com.android.airjoy.user.register.RegisterActivity;
import com.android.airjoy.widget.AirjoyToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener
{
    private static String configNameString="UserLoginPref";
    private static ImageView userImageView;
    private static  EditText login_username;
    private static EditText login_password;
    private static CheckBox remeberMe;
    private static CheckBox autoRun;
    private static Button user_login_button;
    private static TextView user_register_button;
    private static Context _context;
    private static String _loginResult=null;
    private static User _user;
    /* ������� */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // �����ޱ���
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.login_activity);
        _context=this;
        initWidget();
    }

    /* ��ʼ���ؼ� */
    private void initWidget()
    {
        userImageView=(ImageView)findViewById(R.id.userImage);
        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);
        remeberMe=(CheckBox)findViewById(R.id.remeberMe);
        autoRun=(CheckBox)findViewById(R.id.autoRun);
        user_login_button = (Button) findViewById(R.id.user_login_button);
        user_register_button = (TextView) findViewById(R.id.user_register_button);
        user_login_button.setOnClickListener(this);
        user_register_button.setOnClickListener(this);
        SharedPreferences sharedata = getSharedPreferences(configNameString, 0);
        
        //��ʼ����¼����
        if(sharedata.getBoolean("isRemeberMe", false)){
            login_username.setText(sharedata.getString("UserName", ""));
            login_password.setText(sharedata.getString("UserPassword", ""));
            remeberMe.setChecked(true);
        }
        
        //�Ƿ�ֱ����ת��
        if(sharedata.getBoolean("isAutoLogin", false)){
            autoRun.setChecked(true);
        }
        
        //EditText����ı��¼���
        login_username.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                // TODO Auto-generated method stub
                if (!hasFocus)
                {
                    String username = login_username.getText().toString().trim();
                    if (username.length() < 4)
                    {
                        Toast.makeText(LoginActivity.this, "�û�����С��4���ַ�",
                                Toast.LENGTH_SHORT);
                    }
                }
            }

        });
        
       //EditText����ı��¼���
        login_password.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                // TODO Auto-generated method stub
                if (!hasFocus)
                {
                    String password = login_password.getText().toString().trim();
                    if (password.length() < 4)
                    {
                        Toast.makeText(LoginActivity.this, "���벻��С��4���ַ�",
                                Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        
       //��ס�˺�CheckBox��
        remeberMe.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor sharedata = getSharedPreferences(
                        configNameString , 0).edit();
                if(isChecked){
                    sharedata.putBoolean("isRemeberMe", true);//�Ƿ��ס�˺ţ�
                    if(checkEdit()){
                        sharedata.putString("UserName", login_username.getText().toString().trim());
                        sharedata.putString("UserPassword", login_password.getText().toString().trim());
                    }
                }
                else{
                    sharedata.putBoolean("isRemeberMe", false);//�Ƿ��ס�˺ţ�
                    sharedata.putString("UserName", "");
                    sharedata.putString("UserPassword", "");
                }
                sharedata.commit();
            }
        });
        
       //�Զ�����CheckBox��
        autoRun.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor sharedata = getSharedPreferences(
                        configNameString , 0).edit();
                if(isChecked){
                    sharedata.putBoolean("isAutoLogin", true);//�Ƿ��ס�˺ţ�
                }
                else{
                    sharedata.putBoolean("isAutoLogin", false);//�Ƿ��ס�˺ţ�
                }
                sharedata.commit();
            }
        });
    }

    /* ����¼����� */
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        Animation anim = getAlphaAnimation();  
        v.startAnimation(anim);  
        switch (v.getId())
        {
            case R.id.user_login_button:
                if (checkEdit())
                {
                    asynTask(login_username.getText().toString(),login_password.getText().toString());
                }
                break;
            case R.id.user_register_button:
                Intent intent2 = new Intent(LoginActivity.this,
                                            RegisterActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /* ��������Ƿ�Ƿ� */
    private boolean checkEdit()
    {
        if (login_username.getText().toString().trim().equals(""))
        {
            Toast.makeText(LoginActivity.this, "�û�����Ϊ��", Toast.LENGTH_SHORT).show();
        }
        else if (login_password.getText().toString().trim().equals(""))
        {
            Toast.makeText(LoginActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
        }
        else
        {
            return true;
        }
        return false;
    }

    /* ��½���� */
    private void login( String username ,String password)
    {
        String httpUrl = Config.loginUrlString;
        HttpPost httpRequest = new HttpPost(httpUrl);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(//�û���
                                          "name",
                                          username));
        params.add(new BasicNameValuePair(//���룻
                                          "password",
                                          password));
        params.add(new BasicNameValuePair(//tag��־��
                "tag",
                "android"));
        HttpEntity httpentity = null;
        try
        {
            httpentity = new UrlEncodedFormEntity(params, "utf8");
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        httpRequest.setEntity(httpentity);
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        try
        {
            httpResponse = httpclient.execute(httpRequest);
        }
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (httpResponse.getStatusLine().getStatusCode() == 200)
        {
            String strResult = null;
            try
            {
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            _loginResult=strResult;
            Toast.makeText(_context, strResult, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(_context, "��¼ʧ�ܣ�", Toast.LENGTH_SHORT).show();
        }
    }

    /* �¿��첽����*/
    private void asynTask(final String username ,final String password){
        final Animation anim = getRotateAnimation();  
        userImageView.startAnimation(anim);  
            AsyncTask<Void, Void, Boolean> aTask = new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        login(username ,password);
                    } catch (Exception error) {
                        return true;
                    }
                    return true;
                }
                @Override
                protected void onPostExecute(Boolean result) {
                    super.onPostExecute(result);
                    anim.cancel();
                    if(_loginResult==null){
                        Toast.makeText(_context, "����Ӧ", Toast.LENGTH_SHORT).show();
                    }else{
                        _user=  User.parse(_loginResult);
                        if(_user!=null){
                            if(_user.getRes()){
                                jumpToActivity();
                            }else{
                                Toast.makeText(_context, "�޴��˺ţ�", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            };
            aTask.execute();
        }

    /* ͸������Ч��*/
    public Animation getAlphaAnimation() {  
        //ʵ�� AlphaAnimation ��Ҫ�Ǹı�͸����  
        //͸���� �� 1-��͸�� 0-��ȫ͸��   
        Animation animation = new AlphaAnimation(1.0f, 0.5f);  
        //���ö�����ֵ�� ���������ζ���Ч��,���嶯���ı仯��   
        animation.setInterpolator(new DecelerateInterpolator());  
        //���ö���ִ��ʱ��  
        animation.setDuration(200);  
        return animation;  
    }  

    /* ��ת����Ч��*/  
    public Animation getRotateAnimation() {  
        //ʵ��RotateAnimation  
        //����������ΪԲ�ģ���ת360�� ��ֵΪ˳ʱ����ת����ֵΪ��ʱ����ת  
        RotateAnimation animation = new RotateAnimation(0, 360,   
                                        Animation.RELATIVE_TO_SELF, 0.5f,  
                                        Animation.RELATIVE_TO_SELF, 0.5f);    
        //���ö�����ֵ�� ������Ч��
        //animation.setInterpolator(new DecelerateInterpolator());  
        //���ö���ִ��ʱ��  
        animation.setDuration(2000);  
      //  animation.setRepeatMode(android.view.animation.Animation.INFINITE);
        animation.setRepeatCount(-1);//����ֹͣ��
        return animation;  
    }  

    /* ��ת��activity*/
    public void jumpToActivity(){
        SharedPreferences.Editor sharedata = getSharedPreferences(
                configNameString , 0).edit();
        if(remeberMe.isChecked()){
            sharedata.putBoolean("isRemeberMe", true);//�Ƿ��ס�˺ţ�
            if(checkEdit()){
                sharedata.putString("UserName", login_username.getText().toString().trim());
                sharedata.putString("UserPassword", login_password.getText().toString().trim());
            }
        }
        else{
            sharedata.putBoolean("isRemeberMe", false);//�Ƿ��ס�˺ţ�
            sharedata.putString("UserName", "");
            sharedata.putString("UserPassword", "");
        }
        sharedata.commit();
        AirjoyToast.makeText(_context,_user.getName()+"+"+_user.getPassword(),Toast.LENGTH_LONG).show();
        for(int i=0;i<10;i++){//ʮ�δ򶴣�
            try{
                sendString("shakeFromAndroid");
            }catch(Exception error){
                
            }
        }
//        //����ɺ���ת��
//        Intent intent = new Intent();
//        intent.setClass(LoginActivity.this, WelcomePage.class);
//        startActivity(intent);
    }

    /* ��¼�ɹ���UDP��*/
    public void sendString(String msg) {
        String ipStr=_user.getPcIp();
        AirjoyToast.makeText(_context, ipStr, Toast.LENGTH_LONG).show();
        String[] ipStrLiStrings=ipStr.split(":");
        int server_port =Integer.parseInt(ipStrLiStrings[1]);
        String server_ip =ipStrLiStrings[0];
        Config.Ip=server_ip;
        Config.Port=server_port;
        
        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress local = null;
        try {
            local = InetAddress.getByName(server_ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int msg_length = msg.length();
        byte[] messageByte = msg.getBytes();
        DatagramPacket p = new DatagramPacket(messageByte, msg_length,
                local, server_port);
        try {
            s.send(p);
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
