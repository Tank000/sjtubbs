package com.tyt.bbs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyt.bbs.R;

public class MainAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private String[] main;
	private int[] bg={
			R.drawable.bg_mainitem_1,
			R.drawable.bg_mainitem_2,
			R.drawable.bg_mainitem_3,
			R.drawable.bg_mainitem_5,
			R.drawable.bg_mainitem_4,
			R.drawable.bg_mainitem_6,
	}; 
	public MainAdapter(Context contxt){
		this.inflater = LayoutInflater.from(contxt);
		main =contxt.getResources().getStringArray(R.array.main); 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return main.length;
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return main[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_gridview, null);
			holder.app_name = (TextView) convertView.findViewById(R.id.app_name);
			holder.iv_icon = (ImageView)convertView.findViewById(R.id.app_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundResource(bg[pos%bg.length]);
		holder.app_name.setText(getItem(pos));
		return convertView;
	}
	
	public  static class ViewHolder
	{
		TextView app_name;
		ImageView iv_icon;
	}
}
