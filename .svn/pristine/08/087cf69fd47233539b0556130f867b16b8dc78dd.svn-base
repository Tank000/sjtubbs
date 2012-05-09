package com.tyt.bbs.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tyt.bbs.R;
import com.tyt.bbs.entity.TopicItem;


public class TopTenAdapter extends BaseAdapter{

	private static final String TAG = "TopTenAdapter";
	private final int STYLE_TOPTEN=1;
	private final int STYLE_RECOMMEND=2;

	private LayoutInflater inflater;
	private Context contxt;
	private ArrayList<TopicItem> listTitles;
	private int mStyle;
	String[] Colums;
	public TopTenAdapter(Context context,ArrayList<TopicItem> mArrayList,int style) {
		// TODO Auto-generated constructor stub
		this.contxt = context;
		this.inflater = LayoutInflater.from(contxt);
		this.listTitles=mArrayList;
		this.mStyle=style;
		Colums= contxt.getResources().getStringArray(R.array.split);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listTitles.size();
	}

	@Override
	public TopicItem getItem(int position) {
		// TODO Auto-generated method stub
		return listTitles.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub			 
		TopicItem topicItem=getItem(position);	 
		ViewHolder holder = new ViewHolder();
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_toptenlist, null);
			holder.textView_board = (TextView)convertView.findViewById(R.id.txtview_topten_board);
			holder.textView_title = (TextView)convertView.findViewById(R.id.txtview_topten_title);
			holder.textView_time = (TextView)convertView.findViewById(R.id.txtview_topten_time);
			holder.textView_authorid = (TextView)convertView.findViewById(R.id.txtview_topten_authorid);
			holder.tv_Colums = (TextView)convertView.findViewById(R.id.tv_Colums);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.textView_board.setText(topicItem.getBoard());			 
		holder.textView_title.setText(topicItem.getTitle());			 
		holder.textView_authorid.setText(topicItem.getAuthorID());	
		int columId=topicItem.getColumId();
		holder.tv_Colums.setText(Colums[columId<0x10?columId:columId-0x10]);
		if(STYLE_TOPTEN==mStyle){
			holder.textView_time.setVisibility(View.GONE);
		}
		else if(STYLE_RECOMMEND==mStyle){
			holder.textView_time.setText(topicItem.getTime());
		} 
		if(columId<0x10){
			holder.tv_Colums.setVisibility(View.GONE);
		}else{
			holder.tv_Colums.setVisibility(View.VISIBLE);
		}
		return convertView;

	}

	protected class ViewHolder {
		private TextView textView_title;
		private TextView textView_board;
		private TextView textView_authorid;
		private TextView textView_time;
		private TextView tv_Colums;

	}

	/**
	 * @return the listTitles
	 */
	public ArrayList<TopicItem> getListTitles() {
		return listTitles;
	}


	/**
	 * @param listTitles the listTitles to set
	 */
	public void setListTitles(ArrayList<TopicItem> listTitles) {
		this.listTitles.clear();
		this.listTitles = listTitles;
	}


}
