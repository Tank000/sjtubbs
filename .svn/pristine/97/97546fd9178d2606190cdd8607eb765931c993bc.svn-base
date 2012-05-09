package com.tyt.bbs.utils;
/**
 * 
 * @author SJTU SE Ye Rurui ; Zhu Xinyu ; Peng Jianxiang
 * email:yeluolei@gmail.com zxykobezxy@gmail.com
 * No Business Use is Allowed
 * 2011-2-14
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;


public class Login {
	private static Login login;
	public static Login getInstance() 
	{
		if (login == null) 
		{
			login = new Login();
		}
		return login;
	}
	public boolean login(String UserName,String Password) 
	{					
		try {
			List <NameValuePair> params = new ArrayList <NameValuePair>();   
			params.add(new BasicNameValuePair("id",UserName));   
			params.add(new BasicNameValuePair("pw", Password)); 
			String sourceString = Net.getInstance().loginPost(Property.Base_URL+"/bbslogin", params);
			return !sourceString.equalsIgnoreCase("出错啦");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Login", e.toString());
			return false;
		}
	}
}
