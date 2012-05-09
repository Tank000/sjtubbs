package com.tyt.bbs.adapter;


import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyt.bbs.R;

public class FavAdapter  extends BaseAdapter{
	private ArrayList<String> post;
	private ArrayList<Integer> checkId ;
	
	private LayoutInflater mLayoutInflater; 
	private Handler mHandler;
	private Boolean isDel;
	

	public FavAdapter(Context context,ArrayList<String> post,Handler mHandler)
	{
		mLayoutInflater=LayoutInflater.from(context);
		this.post=post;
		this.checkId=new ArrayList<Integer>();		
		this.mHandler=mHandler;
		isDel=false;
		
	}
	
	public void addChecked(int pos){
		this.checkId.add(pos);
	}
	
	public void setCheckd(ArrayList<Integer> data){
		this.checkId=data;
	}
	
	public void setAllCheckd(){
		int size=getCount();
		for(int i=0;i<size;i++)
			this.checkId.add(i);
	
	}
	
	public void setAllUnChecked(){
		this.checkId.clear();
	}
	
	public ArrayList<Integer> getChecked(){
		return this.checkId;
	}
	
	@Override
	public int getCount() { 
		return post.size();
	}  

	@Override
	public String getItem(int pos) {  
		return post.get(pos); 
	}  

	@Override
	public long getItemId(int pos) {  
		return pos;  
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_favorite, null);
			holder.tv_boardname = (TextView) convertView.findViewById(R.id.tv_boardname);
			holder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
			holder.cb_select=(CheckBox)convertView.findViewById(R.id.cb_select);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_boardname.setText(getItem(pos));
		
		if(isDel){
			holder.cb_select.setVisibility(View.VISIBLE);
			
			if(checkId.contains(pos)){
				holder.cb_select.setChecked(true);
				convertView.setBackgroundColor(R.color.list_item_checked);
			}	
			else
			{
				convertView.setBackgroundColor(R.color.list_background);
				holder.cb_select.setChecked(false);
			}
				
			
			
		}else{
			holder.cb_select.setVisibility(View.INVISIBLE);
		}
	
		return convertView;
	}
	
	public void setMode(Boolean isDel) {
		this.isDel = isDel;
	}
	
	public boolean getMode() {
		return this.isDel;
	}


	public static class ViewHolder
	{
		TextView tv_boardname;
		ImageView iv_icon;
		CheckBox  cb_select;
	}
	
	 
}
