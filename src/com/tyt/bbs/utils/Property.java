package com.tyt.bbs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Property {

	private static String PREFERENCES_NAME = "Config";
	
	private static SharedPreferences setting;
	
	public static String Password="password";
	
	public static String UserName="username";
	
	public static String AutoLogin="autologin";
			
	public static String Cookie="cookie";
	
	public static String Cookie_Time="cookie.time";
	
	public static String LoginTime="login.time";
	
	public static String Boards="boards";
	
	public static String Post_Tail="post.tail";
	
	public static String Post_Prefix="post.prefix";
	
	public static int ScreenWidth = 0;
	
	public static String Base_URL="https://bbs.sjtu.edu.cn/";
	
	public  static String Store_Image="store.image";
	
	public  static String Show_Image="show.image";
	
	public static String Tail="\n                          \033[32m——本贴来自\033[36m水源客户端\033[m";
	
	public static String ReplayMark="Re: ";
	
	public static String Quote="quote";
	
	public static double Ratio=1;

	public static SharedPreferences getPreferences(Context activity){
		if(setting==null)
			setting = PreferenceManager.getDefaultSharedPreferences(activity);
//			setting = activity.getSharedPreferences(Property.PREFERENCES_NAME	,Activity.MODE_PRIVATE);
		return setting;
	}
}
