package com.tyt.bbs.adapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tyt.bbs.R;
import com.tyt.bbs.entity.MessageItem;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;
import com.tyt.bbs.view.LoadingDrawable;
import com.tyt.bbs.view.TitleProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DiffMsgAdapter extends BaseAdapter implements TitleProvider {

	private View[] itemView ;
	private int[] viewId= {R.layout.diff_newmsgview,R.layout.diff_allmsgview};
 	private static final int MAX_COUNT = 2;
	private final String[] names = {"New Message","All Message"};
	private LayoutInflater mInflater;
	public DiffMsgAdapter(Context context) {
		itemView= new View[MAX_COUNT];
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getViewTypeCount() {
		return MAX_COUNT;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public View getItem(int position) {
		return itemView[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//        int view = getItemViewType(position);
		return drawView(position,convertView);
	}



	/* (non-Javadoc)
	 * @see org.taptwo.android.widget.TitleProvider#getTitle(int)
	 */
	public String getTitle(int position) {
		return names[position];
	}


	private View drawView(int position, View view) {
		if(itemView[position]==null){
			itemView[position]= mInflater.inflate(viewId[position], null);
		}

		return  itemView[position];
	}

}
