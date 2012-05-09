package com.tyt.bbs;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tyt.bbs.adapter.TopTenAdapter;
import com.tyt.bbs.entity.TopicItem;
import com.tyt.bbs.parser.WebTopTenParser;
import com.tyt.bbs.utils.FileOperate;
import com.tyt.bbs.utils.XmlOperate;
import com.tyt.bbs.view.LoadingDrawable;

public class RecommendActivity extends BaseActivity{

	public static final String TAG = "推荐文章";
	private ListView mToptenList;
	private ProgressBar mProgressBar;
	private WebTopTenParser mParser;
	private TopTenAdapter mAdapter;
	private Boolean isFresh=false;

	private final int STYLE_RECOMMEND=2;
	private final int STYLE_TOPTEN=1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_topten);


		mProgressBar=(ProgressBar)findViewById(R.id.progressbar_topten);
		mProgressBar.setIndeterminateDrawable(new LoadingDrawable(0,
				Color.parseColor("#4F337fd3"), Color.parseColor("#0d337fd3"), Color.TRANSPARENT, 200));
		setListView();
	}

	public void setListView(){
		((TextView)findViewById(R.id.tv_Info)).setText(getResources().getStringArray(R.array.main)[0]);
		mToptenList=(ListView)findViewById(R.id.lv_topten);

		try {
			java.io.File xmlFile=FileOperate.readFromSDcard("recommend.xml");
			mParser = new WebTopTenParser(STYLE_RECOMMEND);
			mProgressBar.setVisibility(View.GONE);
			//若不为第一次登陆，则先从文件中读取xml
			if(xmlFile!=null){

				ArrayList<TopicItem> tmp = 	XmlOperate.getInstance().readXML(xmlFile);
				mAdapter = new TopTenAdapter(RecommendActivity.this,tmp,STYLE_TOPTEN);
				mToptenList.setAdapter(mAdapter);
				mProgressBar.setVisibility(View.GONE);
				isFresh=true;
			}else{
				isFresh=false;
				ListLoad listLoad = new ListLoad();
				listLoad.execute();		
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		mToptenList.setOnItemClickListener(topTenItemOnClick);

	}

	private OnItemClickListener topTenItemOnClick=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			Log.i("onItemClick ", "onItemClick ");
			TopicItem tempdata=mAdapter.getItem(position);
			String link=tempdata.getLinkUrl();
			Intent i=new Intent(RecommendActivity.this,ArticleActivity.class);
			i.putExtra("link", link);
			startActivity(i);
		}

	};

	private class ListLoad extends AsyncTask<Void,Integer,Boolean>
	{
		@Override
		protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			//			mParser = new ArticleParser(articleUrl);
			try {
				mParser.parser();
				if(isFresh)
					mAdapter.setListTitles(mParser.getList());
				else
					mAdapter = new TopTenAdapter(RecommendActivity.this,mParser.getList(),STYLE_RECOMMEND );
				//总的topTen写入SDCard
				String xmString=XmlOperate.getInstance().writeXml(mAdapter.getListTitles());
				if(FileOperate.writeToSDcard("recommend.xml", xmString)){
					Log.i("XMLFileWrite", "success");
				}
				else 
					Log.i("XMLFileWrite", "failed");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "##############################");
				Log.e(TAG,e.getMessage());
				Log.e(TAG, "##############################");
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(isFresh)
				mAdapter.notifyDataSetChanged();
			else
				mToptenList.setAdapter(mAdapter);
			if(result)mProgressBar.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
	}

}
