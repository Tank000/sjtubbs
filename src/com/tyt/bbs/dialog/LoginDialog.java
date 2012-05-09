package com.tyt.bbs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tyt.bbs.R;
import com.tyt.bbs.utils.Login;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;

public class LoginDialog extends Dialog implements android.view.View.OnClickListener {

	private EditText userNameEditText;
	private EditText passwordEditText;
	private Toast mToast;
	private String pswd;
	private String user;

	/**
	 * @param context
	 * @param theme
	 */
	public LoginDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.dialog_login);
		userNameEditText = (EditText)findViewById(R.id.UserNameEditText);
		passwordEditText = (EditText)findViewById(R.id.PasswordEditText);
		findViewById(R.id.btn_dialoglogin).setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_dialoglogin:
			user=userNameEditText.getText().toString();
			pswd=passwordEditText.getText().toString();
			Boolean success = Login.getInstance().login(user, pswd);
			if(success){
				Tip("��¼�ɹ�!���������ϴ�!");
				restoreCookie();
				dismiss();
			}else
				Tip("��¼ʧ�ܣ�");
			break;
		}
	}
	
	private void restoreCookie(){
		Editor edit=Property.getPreferences(getContext()).edit();
		edit.putString(Property.UserName, user);
		edit.putString(Property.Password, pswd);
		edit.putString(Property.Cookie, Net.getInstance().getCookie());
		edit.putLong(Property.Cookie_Time, System.currentTimeMillis()/1000);
		edit.commit();
	}
	
	public void  Tip(String  string){
		if(mToast != null) {  
			mToast.cancel();  
		}
		mToast=Toast.makeText(getContext(), string, 200);
		mToast.show();
	}

}
