package com.tyt.bbs.adapter;

import com.tyt.bbs.R;
import com.tyt.bbs.provider.DataColums.PostData;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class CollectionAdapter extends ResourceCursorAdapter {

	/**
	 * @param context
	 * @param layout
	 * @param c
	 * @param autoRequery
	 */
	
	private int[] index;
	public CollectionAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c, false);
		// TODO Auto-generated constructor stub
		int length = PostData.COLUMNS.length;
		index= new int[length];
		for(int i=0;i<length;i++){
			index[i] =c.getColumnIndex(PostData.COLUMNS[i]);
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		  final ItemViewHolder cache = (ItemViewHolder) view.getTag();
          cache.tv_time.setText(cursor.getString(index[2]));
          cache.tv_title.setText(cursor.getString(index[4]));
          cache.tv_board.setText(cursor.getString(index[3]));
          cache.tv_author.setText(cursor.getString(index[1]));
	}

	/* (non-Javadoc)
	 * @see android.widget.ResourceCursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.newView(context, cursor, parent);
		ItemViewHolder cache = new ItemViewHolder();
        cache.tv_time = (TextView) view.findViewById(R.id.tv_time);
        cache.tv_title = (TextView) view.findViewById(R.id.tv_title);
        cache.tv_author = (TextView) view.findViewById(R.id.tv_author);
        cache.tv_board = (TextView) view.findViewById(R.id.tv_board);
        view.setTag(cache);
		return view;
	}
	protected static class ItemViewHolder {
		private TextView tv_time;
		private TextView tv_title;
		private TextView tv_author;
		private TextView tv_board;

	}
	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#changeCursor(android.database.Cursor)
	 */
	@Override
	public void changeCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		super.changeCursor(cursor);
	}

}
