package com.tyt.bbs.adapter;

import android.R.color;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tyt.bbs.R;

public class BoardsAdapter extends BaseAdapter{
	private String[]  boards;
	private int selectpos;
	private LayoutInflater inflater;
	private static TextView tv_board;
	public BoardsAdapter( String[] mboards,Context contxt){
		boards=mboards;
		this.inflater = LayoutInflater.from(contxt);
		setSelectpos(0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return boards.length;
	}

	@Override
	public String getItem(int pos) {
		// TODO Auto-generated method stub
		return boards[pos];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_navigation, null);
		}
		tv_board = (TextView)convertView.findViewById(R.id.tv_boardname);
		tv_board.setText(getItem(position));
		if(position==selectpos)
			convertView.setBackgroundColor(R.color.select_write);
			else
				convertView.setBackgroundColor(color.transparent);
		return convertView;
	}

	public void setSelectpos(int selectpos) {
		this.selectpos = selectpos;
	}

	public int getSelectpos() {
		return selectpos;
	}

	/**
	 * @return the boards
	 */
	public String[] getBoards() {
		return boards;
	}

	/**
	 * @param boards the boards to set
	 */
	public void setBoards(String[] boards) {
		this.boards = boards;
	}

}
