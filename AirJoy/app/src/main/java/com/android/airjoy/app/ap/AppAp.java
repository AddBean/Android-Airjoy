package com.android.airjoy.app.ap;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.airjoy.R;
import com.android.airjoy.app.anim.Rotate3dHelper;

/**
 * 建立热点Activity；
 *
 * @author 贾豆
 *
 */
public class AppAp extends Activity implements WTBroadcast.EventHandler {
	public Context _context;
	public TextView _txtViewHtml;
	private LinearLayout _apInfLayout;
	private ImageView _apImage;
	private ImageView _apSet;
	private TextView _apName;
	private TextView _apPassword;
	private TextView _apState;
	private TextView _ap_iplist;
	public Rotate3dHelper _switchApAnim;
	private Rotate3dHelper _setAnim;

	private String _ApSSID = "Airjoy";
	private String _ApPassword = "airjoy";
	public checkApIsAlive _checkApIsAlive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		setContentView(R.layout.app_ap);
		this._context = this;
		this._ApSSID = "Airjoy";
		initTital();
		WTBroadcast.ehList.add(this);
		initAllLayout();
		this._apName.setText("");// 获取当前WIFI信息；
		this._apState.setText(Hotspot.getWifiInf(this));
		this._checkApIsAlive = new checkApIsAlive();
		RefreshIplist();
		initIplist();
	}

	private void initAllLayout() {
		this._apInfLayout = (LinearLayout) findViewById(R.id.AP_inf_layout);
		this._apImage = (ImageView) findViewById(R.id.Ap_image);
		this._apName = (TextView) findViewById(R.id.Ap_name);
		this._apState = (TextView) findViewById(R.id.Ap_state);
		this._apSet = (ImageView) findViewById(R.id.Ap_set);
		this._apPassword = (TextView) findViewById(R.id.Ap_password);
		this._ap_iplist=(TextView) findViewById(R.id.Ap_iplist);
		this._switchApAnim = new Rotate3dHelper(this._apInfLayout);// 设置动画；
		this._setAnim = new Rotate3dHelper(this._apSet);// 设置动画；
		this._apImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					if (Hotspot.getWifiInf(_context) != "WifiDisable") {
						_switchApAnim.Rotate3dByX(200, 0, 90, 100, true,
								new Rotate3dHelper.OnAnimOverListener() {
									@Override
									public void AnimOver() {
										Hotspot.setWifiApEnabled(_context, true, _ApPassword,
												_ApSSID);
										_apName.setText(_ApSSID);
										//_apPassword.setText("密码（airjoy）");
										_apState.setText("正在开启热点....");
										_checkApIsAlive.start();
									}
								});
					} else {// 开启热点；

						_switchApAnim.Rotate3dByY(400, 0, 90, 100, true,
								new Rotate3dHelper.OnAnimOverListener() {
									@Override
									public void AnimOver() {
										Hotspot.setWifiApEnabled(_context, false, _ApPassword,
												_ApSSID);
										_apName.setText(_ApSSID);
										//_apPassword.setText("密码（airjoy）");
										_apState.setText("正在开启热点....");
										_checkApIsAlive.start();
									}
								});
					}
				} catch (Exception error) {

				}
			}
		});
		this._apSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_setAnim.Rotate3dByX(200, 0, 90, 100, true,
						new Rotate3dHelper.OnAnimOverListener() {
							@Override
							public void AnimOver() {
								Intent intent = new Intent(
										Settings.ACTION_WIFI_SETTINGS);
								startActivity(intent);
							}
						});
			}
		});
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

	/* 广播接口实现 */
	@Override
	public void wifiStatusNotification() {// wifi状态改变；
		if (Hotspot.getWifiApState(this)) {
			_switchApAnim.Rotate3dByY(400, 0, 90, 100, true,
					new Rotate3dHelper.OnAnimOverListener() {
						@Override
						public void AnimOver() {
							_apName.setText(_ApSSID);
							_apState.setText("正在开启热点....");
						}
					});
		}

	}

	/* 循环检测热点开启是否超时 */
	class checkApIsAlive implements Runnable {
		public boolean running = false;
		private long startTime = 0L;
		private Thread thread = null;

		checkApIsAlive() {
		}

		public void run() {
			while (true) {
				if (!this.running)
					return;
				if (System.currentTimeMillis() - this.startTime >= 8000L) {// 设置8s超时;
					Message msg = handler.obtainMessage(1);
					handler.sendMessage(msg);
				}
				try {
					Thread.sleep(10L);
				} catch (Exception localException) {
				}
			}
		}

		public void start() {
			try {
				this.thread = new Thread(this);
				this.running = true;
				this.startTime = System.currentTimeMillis();
				this.thread.start();
			} finally {
			}
		}

		public void stop() {
			try {
				this.running = false;
				this.thread = null;
				this.startTime = 0L;
			} finally {
			}
		}
	}

	/* msg处理 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (Hotspot.getWifiApState(_context)) {
				_switchApAnim.Rotate3dByY(400, 0, 90, 100, true,
						new Rotate3dHelper.OnAnimOverListener() {
							@Override
							public void AnimOver() {
								_apName.setText(_ApSSID);
								//_apPassword.setText("密码（"+_ApPassword+"）");
								_apState.setText("已开启");
							}
						});
				_checkApIsAlive.stop();
			} else {
				_switchApAnim.Rotate3dByY(400, 0, 90, 100, true,
						new Rotate3dHelper.OnAnimOverListener() {
							@Override
							public void AnimOver() {
								_apName.setText(_ApSSID);
								//_apPassword.setText("热点开启失败，请重试！");
								_apState.setText("出错");
							}
						});
				_checkApIsAlive.stop();
			}
		}
	};

	/* 刷新当前连接列表*/
	public void RefreshIplist(){

		LinearLayout DevLayout=new LinearLayout(_context);
		DevLayout=(LinearLayout)findViewById(R.id.Ap_devlist);
		final Rotate3dHelper r= new Rotate3dHelper(DevLayout);// 设置动画；

		DevLayout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				r.Rotate3dByX(300, 0, 90, 0, true,new Rotate3dHelper.OnAnimOverListener(){
					@Override
					public void AnimOver() {
						initIplist();
					}

				});

			}
		});


	}

	/* initIP列表*/
	public void initIplist(){
		try{
			ArrayList<String>ipList=Hotspot.getConnectedIP();
			_ap_iplist.setText(""+"\r\n");
			for(String ipStr:ipList){//显示当前连接的Ip列表；
				_ap_iplist.append(ipStr.replace("IP", "")+"\r\n");
			}
		}catch(Exception error){
			Toast.makeText(_context, "初始化IP列表出错！", Toast.LENGTH_LONG).show();
		}
	}

	/* 销毁线程及监听*/
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		WTBroadcast.ehList.remove(this);
		_checkApIsAlive.stop();
	}
}
