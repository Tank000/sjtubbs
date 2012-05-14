package com.tyt.bbs;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobclick.android.MobclickAgent;
import com.tyt.bbs.entity.TopicItem;
import com.tyt.bbs.parser.TopTenParser;
import com.tyt.bbs.utils.FileOperate;
import com.tyt.bbs.utils.Login;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;
import com.tyt.bbs.utils.XmlOperate;
import com.tyt.bbs.view.LoadingDrawable;

public class LoginActivity extends BaseActivity implements OnClickListener{
	private static final String TAG = "Login";
	/** Called when the activity is first created. */
	private EditText usernameEditText,passwordEditText;
	private Button btn_login;
	private CheckBox cb_autologin;
	private String usr,pswd;
	private final int LOGIN_SUCESS = 0x001;
	private final int LOGIN_FAILED = 0x002;
	private SharedPreferences sp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		MobclickAgent.onError(this);
//		MobclickAgent.update(this);
		setContentView(R.layout.a_login);
		Property.ScreenWidth=getResources().getDisplayMetrics().widthPixels;
		Property.Ratio = getResources().getDisplayMetrics().densityDpi/160.0;
		sp=Property.getPreferences(this);
		usernameEditText = (EditText) findViewById(R.id.UserNameEditText);
		passwordEditText = (EditText)findViewById(R.id.PasswordEditText);
		cb_autologin = (CheckBox)findViewById(R.id.automaticlogin);
		btn_login= (Button)findViewById(R.id.btn_login);
		findViewById(R.id.btn_guest).setOnClickListener(this);
		btn_login.setOnClickListener(this);
		usernameEditText.setText(sp.getString(Property.UserName, ""));
		passwordEditText.setText(sp.getString(Property.Password, ""));
		Boolean isAuto=sp.getBoolean(Property.AutoLogin, false);
		cb_autologin.setChecked(isAuto);
		if(isAuto) loginProcess(isAuto);
		((ProgressBar) findViewById(R.id.Loading)).setIndeterminateDrawable(new LoadingDrawable(0,
				Color.parseColor("#4F337fd3"), Color.parseColor("#0d337fd3"), Color.TRANSPARENT, 200));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_login:
			loginProcess(sp.getBoolean(Property.AutoLogin, false));
			break;
		case R.id.btn_guest:
			new Thread(getWapsiteRunnable).start();
			startActivity(new Intent(LoginActivity.this,MainActivity.class));
			finish();
			break;
		}
	}

	private void ChangeUI(Boolean isloading){
		if(isloading){
			findViewById(R.id.info).setVisibility(View.GONE);
			findViewById(R.id.loading).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.info).setVisibility(View.VISIBLE);
			findViewById(R.id.loading).setVisibility(View.GONE);
		}
	}

	private void loginProcess(Boolean isAuto){
		ChangeUI(true);
		if(!isAuto){
			usr = usernameEditText.getText().toString();
			pswd = passwordEditText.getText().toString();
		}else{
			usr = sp.getString(Property.UserName, "");
			pswd = sp.getString(Property.Password, "");
			usr=usr.equalsIgnoreCase("")?usernameEditText.getText().toString():usr;
			pswd=pswd.equalsIgnoreCase("")?passwordEditText.getText().toString():pswd;
		}
		if(usr.equalsIgnoreCase("")|| pswd.equalsIgnoreCase("")){
			Toast.makeText(LoginActivity.this, R.string.tips, 500).show();
			ChangeUI(false);
		}else{
			new Thread(loginRunnable).start();
		}
	}

	private Runnable getWapsiteRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			java.io.File xmlFile;
			try {
				xmlFile = FileOperate.readFromSDcard("topten.xml");
				if(xmlFile==null)
					getWapTop10();
				else{
					Message message  = new Message();
					message.what = LOGIN_SUCESS;
					loginHandler.sendMessage(message);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	
	private Runnable loginRunnable = new Runnable() {

		@Override
		public void run() {
			try{
				if (!Net.getInstance().checknetwork(getApplicationContext())) 
				{
					throw new Exception("登入失败,请检查网络连接");
				}else {
					boolean sucess=false;
//					long logininterval= Property.getPreferences(LoginActivity.this).getLong(Property.Cookie_Time, 0);
//					if(logininterval>0)
//						logininterval =System.currentTimeMillis()/1000- logininterval;
//					String cookie = Property.getPreferences(LoginActivity.this).getString(Property.Cookie, "");
//					Log.v(TAG, "登陆间隔="+logininterval+"  ,Cookie="+cookie);
//					if(!cookie.contentEquals("")
//							&&logininterval<180){
//						sucess=true;
//					}else{
						sucess = Login.getInstance().login(usr,pswd);
						if(sucess){
							Editor edit=Property.getPreferences(LoginActivity.this).edit();
							edit.putString(Property.Cookie, Net.getInstance().getCookie());
							edit.putLong(Property.Cookie_Time, System.currentTimeMillis()/1000);
							//							Log.v(TAG, "当前时间="+System.currentTimeMillis()/1000);
							edit.commit();
						}
//					}
					if (sucess)
					{
						java.io.File xmlFile=FileOperate.readFromSDcard("topten.xml");
						if(xmlFile==null)
							getWapTop10();
						else{
							Message message  = new Message();
							message.what = LOGIN_SUCESS;
							loginHandler.sendMessage(message);
						}
					}
					else 
					{
						throw new Exception("用户名或密码错误！");
					}
				}
			}catch (Exception e) {
				Bundle bundle = new Bundle();
				bundle.putString("exception", e.getMessage());
				Message m = new Message();
				m.setData(bundle);
				m.what = LOGIN_FAILED;
				loginHandler.sendMessage(m);
			}

		}

	};

	private void getWapTop10() throws Exception {
		// TODO Auto-generated method stub
		TopTenParser mParser = TopTenParser.getInstance();
		ArrayList<TopicItem> topTens = mParser.parser();
		Log.v(TAG, ""+topTens.size());
		String xmString=XmlOperate.getInstance().writeXml(topTens);
		Log.v(TAG, ""+xmString);
		if(FileOperate.writeToSDcard("topten.xml", xmString)){
			Log.i("XMLFileWrite", "success");
			Message message  = new Message();
			message.what = LOGIN_SUCESS;
			loginHandler.sendMessage(message);
		}
		else 
			Log.i("XMLFileWrite", "failed");
	}

	private Handler loginHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case LOGIN_SUCESS:
			{
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(Property.UserName, usr);
				editor.putString(Property.Password, pswd);
				editor.putBoolean(Property.AutoLogin, cb_autologin.isChecked());
				editor.commit();
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				break;
			}
			case LOGIN_FAILED:
			{
				Toast.makeText(getApplicationContext(),msg.getData().getString("exception"), Toast.LENGTH_SHORT).show();
				ChangeUI(false);
				break;
			}
			}
		}
	};
}