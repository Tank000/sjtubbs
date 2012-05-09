package com.tyt.bbs.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SimpleCollectionAdapter extends SimpleCursorAdapter {

	/**
	 * @param context
	 * @param layout
	 * @param c
	 * @param from
	 * @param to
	 */
	public SimpleCollectionAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.widget.SimpleCursorAdapter#setViewText(android.widget.TextView, java.lang.String)
	 */
	@Override
	public void setViewText(TextView v, String text) {
		// TODO Auto-generated method stub
		super.setViewText(v, Html.fromHtml(text).toString());
	}

}
