/**
 * 
 */
package com.tyt.bbs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

/**
 * @author NieMinghua
 *
 */
public class PubDateParser {

	public static String parse(String pubdate){
//		pubdate = pubdate.toLowerCase();
		Log.v("", pubdate+"  长度"+pubdate.length());
		Date date=null;
		String mdate="发表于";
		if(date==null){
			try {
				 SimpleDateFormat format = new SimpleDateFormat("Mmm dd HH:mm");
				date=format.parse(pubdate);
				long time= date.getTime()- new Date().getTime();
				long day =time/(24 * 60 * 60 * 1000);
				long hour=time%(60 * 60 * 1000);
				 long min=time%(60 * 1000);
				 long second=time/1000;
				if(day>0) mdate=mdate+day+"天";
				else{
					if(hour>0){
						mdate=mdate+hour+"时";
					}else{
						if(min>0){
							mdate=mdate+min+"分";
						}else{
							if(second>0){
								mdate=mdate+second+"秒";
							}else{
								mdate=mdate+"0秒";
							}
						}
					}
					
				}
				mdate=mdate+"前";
				Log.v("", mdate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.e("PubDataParserException",e.toString());
			}
		}


		return mdate;
	}
}
