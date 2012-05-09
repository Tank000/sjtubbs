package com.tyt.bbs.adapter;

/**
 * 
 * @author SJTU Tan ye teng
 * email:tank.tyt@gmail.com
 * No Business Use is Allowed
 * 2011-2-14
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyt.bbs.R;
import com.tyt.bbs.entity.ArticleItem;
import com.tyt.bbs.entity.MessageItem;
import com.tyt.bbs.entity.PostItem;
import com.tyt.bbs.utils.Property;
import com.tyt.bbs.utils.PubDateParser;

public class MsgListAdapter  extends BaseAdapter{
	private ArrayList<MessageItem> msg;
	private LayoutInflater mLayoutInflater; 
	private MyImgGetter imgGetter;

	public MsgListAdapter(Context context)
	{
		mLayoutInflater=LayoutInflater.from(context);
		imgGetter= new MyImgGetter();
	}

	@Override
	public int getCount() {
		if(getMsg()==null) return 0;
		return getMsg().size();
	}  

	@Override
	public MessageItem getItem(int pos) {  
		return getMsg().get(pos); 
	}  

	@Override
	public long getItemId(int pos) {  
		return pos;  
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if(getCount()==0) return null;
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_msglist, null);
			viewHolder.title = (TextView) convertView.findViewById(R.id.msg_title);
			viewHolder.sender = (TextView)convertView.findViewById(R.id.msg_sender);
			viewHolder.content = (TextView)convertView.findViewById(R.id.msg_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(getMsg()==null) return convertView;
		MessageItem item =getItem(pos);
		viewHolder.title.setText(item.getTitle());
		viewHolder.sender.setText(item.getName());
		imgGetter.setItem(item);
		viewHolder.content.setText(Html.fromHtml(item.getContent()));
		return convertView;
	}


	private class MyImgGetter implements ImageGetter{

		private Drawable     drawable;
		private MessageItem  item;

		@Override
		public Drawable getDrawable(String source) {
			// TODO Auto-generated method stub
			String imageUrl=source.contains("http")?source:Property.Base_URL+source;
			//			Log.i("==ImageCache()==", articleItem.getImageCache().size()+"");
			if(item.getImageCache().containsKey(imageUrl)) {
				drawable=item.getImageCache().get(imageUrl);
				if(drawable!=null){
					drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*Property.Ratio) , (int)(drawable.getIntrinsicHeight()*Property.Ratio ));					
				}else{
					Log.i("Image", "=====Drawable is null ! =====");
				}
			}
			return drawable;
		}

		/**
		 * @param item the item to set
		 */
		public void setItem(MessageItem item) {
			this.item = item;
		}

		/**
		 * @return the item
		 */
		public MessageItem getItem() {
			return item;
		}

	};

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(ArrayList<MessageItem> msg) {
		this.msg = msg;
	}

	/**
	 * @return the msg
	 */
	public ArrayList<MessageItem> getMsg() {
		return msg;
	}

	static class ViewHolder
	{
		TextView title;
		TextView sender;
		TextView content;
	}
}
