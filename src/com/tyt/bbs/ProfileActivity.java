/**
 * 
 */
package com.tyt.bbs;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;

/**
 * @author tyt2011
 *com.tyt.bbs
 */
public class ProfileActivity extends BaseActivity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	TextView tv_profile;
	String user,pswd;
	String URL="https://bbs.sjtu.edu.cn/bbsqry?userid=";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_profile);
		initialView();
	}
	private void initialView(){
		user = Property.getPreferences(this).getString(Property.UserName, "");
		tv_profile = (TextView)findViewById(R.id.tv_profile);
	      ((TextView)findViewById(R.id.usrname)).setText(user);	
	      String file=null;
	      try {
	    	  file=Net.getInstance().get(URL+user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		Log.i("ProfileActivity useInfo", file);
		
		tv_profile.setText(Html.fromHtml(file));
	}
}
