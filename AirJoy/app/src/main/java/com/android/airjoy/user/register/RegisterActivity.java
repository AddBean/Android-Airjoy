package com.android.airjoy.user.register;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.android.airjoy.R;
import com.android.airjoy.app.anim.Rotate3dHelper;
import com.android.airjoy.app.anim.Rotate3dHelper.OnAnimOverListener;
import com.android.airjoy.constant.Config;
import com.android.airjoy.user.fileUpload;
import com.android.airjoy.user.bean.User;
import com.android.airjoy.user.login.LoginActivity;
import com.android.airjoy.widget.AirjoyToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private static EditText register_username;
	private static EditText register_passwd;
	private static EditText reregister_passwd;
	private static EditText register_email;
	private static Button register_submit;
	private static Context _context;
	private static String _registerResult;
	private static User _user;
	private static String TAG="RegisterActivity";
	private static String _uploadImageResult="";

	/* ������� */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // �����ޱ���
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.register_activity);
		_context = this;
		initTital();
		register_username = (EditText) findViewById(R.id.register_username);
		register_passwd = (EditText) findViewById(R.id.register_passwd);
		reregister_passwd = (EditText) findViewById(R.id.reregister_passwd);
		register_email = (EditText) findViewById(R.id.register_email);
		register_submit = (Button) findViewById(R.id.register_submit);
		register_username.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (register_username.getText().toString().trim().length() < 4) {
						Toast.makeText(RegisterActivity.this, "�û�����С��4���ַ�",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

		});
		register_passwd.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					if (register_passwd.getText().toString().trim().length() < 6) {
						Toast.makeText(RegisterActivity.this, "���벻��С��8���ַ�",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		reregister_passwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					if (!reregister_passwd
							.getText()
							.toString()
							.trim()
							.equals(register_passwd.getText().toString().trim())) {
						Toast.makeText(RegisterActivity.this, "�����������벻һ��",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		register_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Animation anim = getAlphaAnimation();
				v.startAnimation(anim);
				asynTask();
			}
		});
	}

	/* ��ʼ������ */
	private void initTital() {
		final ImageView backImageView = (ImageView) findViewById(R.id.app_back_icon);
		backImageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				backImageView.setAlpha(50);
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		final ImageView addUserImageView=(ImageView)findViewById(R.id.userImage);
		final Rotate3dHelper r3hHelper=new Rotate3dHelper(addUserImageView);
		addUserImageView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				r3hHelper.Rotate3dByY(100, 0, 90, 0, true, new OnAnimOverListener(){
					@Override
					public void AnimOver() {
						// TODO Auto-generated method stub
						selectImage();//��ת��ͼ��ѡ��
					}
				});
			}
		});
	}

	/* �����д��ʽ */
	private boolean CheckEdit() {
		if (register_username.getText().toString().trim().equals("")) {
			Toast.makeText(RegisterActivity.this, "�û�����Ϊ��", Toast.LENGTH_SHORT)
					.show();
		} else if (register_email.getText().toString().trim().equals("")
				|| !register_email.getText().toString().contains("@")) {
			Toast.makeText(RegisterActivity.this, "�����ʽ���ԣ�", Toast.LENGTH_SHORT)
					.show();
		} else if (register_passwd.getText().toString().trim().equals("")) {
			Toast.makeText(RegisterActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT)
					.show();
		} else if (!register_passwd.getText().toString().trim()
				.equals(reregister_passwd.getText().toString().trim())) {
			Toast.makeText(RegisterActivity.this, "�����������벻һ��",
					Toast.LENGTH_SHORT).show();
		} else {
			return true;
		}
		return false;
	}

	/* ע������ */
	private boolean RegisterPost() {
		if (!CheckEdit()) {
			return false;
		}
		String httpUrl = Config.RegisterUrl;
		HttpPost httpRequest = new HttpPost(httpUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", register_username.getText()
				.toString()));
		params.add(new BasicNameValuePair("password", register_passwd.getText()
				.toString()));
		params.add(new BasicNameValuePair("email", register_email.getText()
				.toString()));
		params.add(new BasicNameValuePair("tag", "android"));
		HttpEntity httpentity = null;
		try {
			httpentity = new UrlEncodedFormEntity(params, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpRequest.setEntity(httpentity);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(httpRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String strResult = null;
			try {
				strResult = EntityUtils.toString(httpResponse.getEntity());
				_registerResult = strResult;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} else {
			Toast.makeText(RegisterActivity.this, "�������", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	}

	/* ע���첽���� */
	private void asynTask() {
		register_submit.setText("����ע�ᡭ�����Եȡ�");
		AsyncTask<Void, Void, Boolean> aTask = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					RegisterPost();
				} catch (Exception error) {
					return true;
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				register_submit.setText("ע  ��");
				if (_registerResult == null) {
					Toast.makeText(_context, "����Ӧ", Toast.LENGTH_SHORT).show();
				} else {
					try {
						_user = User.parse(_registerResult);
						if (_user != null) {
							if (_user.getRes()) {
								jumpToActivity();
							} else {
								if (_user.getName() == "") {
									AirjoyToast.makeText(_context, "ע�����",
											Toast.LENGTH_LONG).show();
								} else {
									AirjoyToast.makeText(_context, "���˻��Ѵ��ڣ�",
											Toast.LENGTH_LONG).show();
								}
							}
						}
					} catch (Exception error) {
						AirjoyToast.makeText(_context, "ע��ʱ���?�����ԣ�",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		};
		aTask.execute();
	}

	/* ͸������Ч�� */
	public Animation getAlphaAnimation() {
		// ʵ�� AlphaAnimation ��Ҫ�Ǹı�͸����
		// ͸���� �� 1-��͸�� 0-��ȫ͸��
		Animation animation = new AlphaAnimation(1.0f, 0.5f);
		// ���ö�����ֵ�� ���������ζ���Ч��,���嶯���ı仯��
		animation.setInterpolator(new DecelerateInterpolator());
		// ���ö���ִ��ʱ��
		animation.setDuration(200);
		return animation;
	}

	/* ��ת����¼activity */
	public void jumpToActivity() {
		AirjoyToast.makeText(_context, "��ϲ��ע��ɹ���", Toast.LENGTH_LONG).show();
		Intent intent = new Intent();
		intent.setClass(RegisterActivity.this, LoginActivity.class);
		startActivity(intent);
	}

	/* ��תͼ�� */
	public void selectImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("crop", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/* �ص�ѡ�� */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			System.out.println("requestCode" + requestCode);
			if (requestCode == 2) {//���سɹ���
				Uri uri = data.getData();
				getFielPath(uri);
				File files=getFielPath(uri);
				Map<String, File> mFileMap= new HashMap<String, File>();
				mFileMap.put(files.getName(), files);
				uploadAsynTask(mFileMap);
			}
		}
	}

	/* ��ȡ�ļ�·��*/
	private File getFielPath(Uri uri){
		     String[] proj = { MediaStore.Images.Media.DATA };   
		      Cursor actualimagecursor = this.managedQuery(uri,proj,null,null,null);  
		      int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);   
		      actualimagecursor.moveToFirst();   
		    String img_path = actualimagecursor.getString(actual_image_column_index);  
		    Log.v(TAG, img_path);
		    File file = new File(img_path);
		    return file;
	}

	/* �ϴ�ͼƬ�첽����*/
	private void uploadAsynTask(final Map<String, File> mFileMap) {
		AsyncTask<Void, Void, Boolean> aTask = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					_uploadImageResult=fileUpload.post(Config.UploadUrl, mFileMap);
				} catch (Exception error) {
					return true;
				}
				return true;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				Toast.makeText(_context,_uploadImageResult, Toast.LENGTH_LONG).show();
			}
		};
		aTask.execute();
	}
	
	/* ��ȡ����ͼƬ*/
	public Bitmap returnBitMap(String url){ 
        URL myFileUrl = null;   
        Bitmap bitmap = null;  
        try {   
            myFileUrl = new URL(url);   
        } catch (MalformedURLException e) {   
            e.printStackTrace();   
        }   
        try {   
            HttpURLConnection conn = (HttpURLConnection) myFileUrl   
              .openConnection();   
            conn.setDoInput(true);   
            conn.connect();   
            InputStream is = conn.getInputStream();   
            bitmap = BitmapFactory.decodeStream(is);   
            is.close();   
        } catch (IOException e) {   
              e.printStackTrace();   
        }   
              return bitmap;   
    }   
}
