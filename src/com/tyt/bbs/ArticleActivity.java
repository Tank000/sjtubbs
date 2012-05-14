package com.tyt.bbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.tyt.bbs.adapter.ArticelAdapter;
import com.tyt.bbs.entity.ArticleItem;
import com.tyt.bbs.parser.ArticleParser;
import com.tyt.bbs.provider.DataColums.PostData;
import com.tyt.bbs.utils.FileOperate;
import com.tyt.bbs.utils.Login;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;
import com.tyt.bbs.view.LoadingDrawable;

public class ArticleActivity  extends BaseActivity implements OnClickListener {

	private ListView mArticleList;
	private ProgressBar mProgressBar;
	private ArticleParser mParser;
	private ArticelAdapter mAdapter;
	private String   articleUrl;
	private String fullText;

	private static final int DIALOG_REPLAY= 0x0;
	private static final int DIALOG_LOGIN = 0x1;
	private static final int DIALOG_ZHUANTIE = 0x2;
	protected static final int NEED_LOGIN = 0x0;
	protected static final int REFRESH_LIST = 0x1;
	protected static final int POST_ON = 0x2;
	protected static final int LOAD_IMAGE = 0x3;

	private AutoCompleteTextView mZhuanTieBoard;
	private EditText replyContentEditText,passwordEditText,userNameEditText;
	private EditText replyTitleEditText;
	private String fileLink;
	private String reid,board,file,testPrefix;
	private View dv,loginLayout;
	private TextView tv_posttitle ;
	private String user,pswd;
	private Toast mToast;
	private TextView tv_postno;
	private boolean mGetMore;
	public SharedPreferences sp;
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);	
		setContentView(R.layout.a_article);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		initialViews();
		setListView();
		new ArticleListload().execute();	

	}


	private void  initialViews(){
		mProgressBar=(ProgressBar)findViewById(R.id.progressbar_article);
		mProgressBar.setIndeterminateDrawable(new LoadingDrawable(0,
				Color.parseColor("#4F337fd3"), Color.parseColor("#0d337fd3"), Color.TRANSPARENT, 200));

		articleUrl=getIntent().getStringExtra("link");
		reid=getIntent().getStringExtra("reid");
		board=getIntent().getStringExtra("board");

		((TextView)findViewById(R.id.tv_Info)).setText(board);
		tv_posttitle = ((TextView)findViewById(R.id.tv_posttitle));
		tv_postno = ((TextView)findViewById(R.id.tv_postno));
		dv = LayoutInflater.from(this).inflate(R.layout.dialog_reply, null);
		replyTitleEditText = (EditText)dv.findViewById(R.id.replaytitle);
		replyContentEditText = (EditText)dv.findViewById(R.id.replycontent);
		user = Property.getPreferences(this).getString(Property.UserName, "");
		pswd = Property.getPreferences(this).getString(Property.Password, "");
		loginLayout  = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
		userNameEditText = (EditText)loginLayout.findViewById(R.id.UserNameEditText);
		passwordEditText = (EditText)loginLayout.findViewById(R.id.PasswordEditText);
		userNameEditText.setText(user);
		passwordEditText.setText(pswd);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id){
		case DIALOG_REPLAY:
			final Dialog mReplayDialog = new Dialog(this, R.style.Dialog);
			dv.findViewById(R.id.replysubmit).setOnClickListener(this);
			dv.findViewById(R.id.replyupload).setOnClickListener(this);
			dv.findViewById(R.id.paint).setOnClickListener(this);
			dv.setOnClickListener(this);
			mReplayDialog.setContentView(dv);
			return mReplayDialog;
		case DIALOG_LOGIN:
			final Dialog loginDialog =  new Dialog(this, R.style.Dialog);
			loginLayout.findViewById(R.id.btn_dialoglogin).setOnClickListener(this);
			loginLayout.setOnClickListener(this);
			loginDialog.setContentView(loginLayout);
			return loginDialog;
		case DIALOG_ZHUANTIE:
			final Dialog mZhuanTieDialog =  new Dialog(this, R.style.Dialog);
			View zhuanieDv = LayoutInflater.from(this).inflate(R.layout.dialog_addfavor, null);
			mZhuanTieBoard=(AutoCompleteTextView) zhuanieDv.findViewById(R.id.autoCT_addFavor);
			ArrayAdapter<String> boadersadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.boards));
			mZhuanTieBoard.setAdapter(boadersadapter);
			zhuanieDv.findViewById(R.id.btn_addFavorok).setOnClickListener(this);
			zhuanieDv.findViewById(R.id.btn_addFavorcancel).setOnClickListener(this);
			mZhuanTieDialog.setContentView(zhuanieDv);
			return mZhuanTieDialog;
		}
		return null;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,  
			ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo); 
		menu.setHeaderTitle("功能选择");
		getMenuInflater().inflate(R.menu.article_cm, menu);
	}  

	File tempFile = new File("/sdcard/a.jpg");

	public boolean onContextItemSelected(MenuItem item) {  
		switch (item.getItemId()) {  
		case R.id.m_refresh:
			if(mProgressBar.getVisibility()==View.GONE){
				handler.post(FreshListRunnable);
			}
			return true;
		case R.id.m_goboard:
			if(mProgressBar.getVisibility()==View.GONE){
				Intent it = new Intent(this,PostsListActivity.class);
				it.putExtra(Property.Boards,board);
				startActivity(it);
				finish();
			}
			return true;
		case R.id.m_zhuantie:
			showDialog(DIALOG_ZHUANTIE);
			return true;
		case R.id.m_collection:
			ArticleItem aitem =	mAdapter.getItem(0);
			Cursor  c=managedQuery(PostData.CONTENT_URI, null, PostData.REID+"="+reid, null, null);
			Log.v("数量","cursor count: "+c.getCount());
			if(c.getCount()==0){
				ContentResolver cr=getContentResolver();
				ContentValues values = new ContentValues();

				values.put(PostData.AUTHOR,aitem.getAuthor());
				values.put(PostData.BOARD,board);
				values.put(PostData.TIME,aitem.getTime());
				values.put(PostData.TITLE,tv_posttitle.getText().toString());
				values.put(PostData.TEXT,fullText);
				values.put(PostData.LINK,articleUrl);
				values.put(PostData.REID,reid);

				cr.insert(PostData.CONTENT_URI, values);
				Tip("收藏成功!");
			}else{
				Tip("该文已收藏!");
			}
			c.close();
			return true;
		case R.id.m_share:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_TITLE,mAdapter.getItem(0).getArticle().toString());
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "【Shared from SJTUBBS】"+Html.fromHtml(mAdapter.getItem(0).getArticle()));
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,Html.fromHtml(mAdapter.getItem(0).getArticle())+"\n"+articleUrl);
			startActivity(Intent.createChooser(shareIntent, "Share"));      
			return true;
		default:  
			return super.onContextItemSelected(item);  
		}  
	} 

	public void  Tip(String  string){
		if(mToast != null) {  
			mToast.cancel();  
		}
		mToast=Toast.makeText(this, string, 200);
		mToast.show();
	}

	public void setListView(){
		findViewById(R.id.btn_ariticleback).setOnClickListener(this);
		findViewById(R.id.btn_articleMore).setOnClickListener(this);
		registerForContextMenu(findViewById(R.id.btn_articleMore));
		mArticleList=(ListView)findViewById(R.id.listview_article);
		mArticleList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				fileLink=mAdapter.getItem(pos).getLink();
				if(fileLink.indexOf("file=")!=-1)
					file = fileLink.substring(fileLink.indexOf("file=")).replace("file=", "");
				if(file!=null)
					Log.v("===file===", file);
				Log.v("===fileLink===", fileLink);
				ArticleItem  item =mAdapter.getItem(pos);
				String article =mAdapter.getItem(0).getArticle().toString().replace("○", "");
				if(article.indexOf("<br/>")!=-1)
					article=article.substring(0,article.indexOf("<br/>"));
				article = article.trim();
				replyTitleEditText.setText(Html.fromHtml(article.contains("Re:")?article:Property.ReplayMark+article));
				if(pos==0) testPrefix = "     \033[36m【回楼主"+item.getAuthor()+"】\033[m\n";
				else testPrefix = "     \033[36m【回"+pos+"楼"+item.getAuthor()+"】\033[m\n";
				showDialog(DIALOG_REPLAY);
			}
		});
		mArticleList.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "【Shared from SJTUBBS】"+Html.fromHtml(mAdapter.getItem(position).getArticle()));
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,Html.fromHtml(mAdapter.getItem(position).getArticle())+"\n"+articleUrl);
				startActivity(Intent.createChooser(shareIntent, "Share"));      
				return false;
			}
			
		});

		mArticleList.setOnScrollListener(new OnScrollListener() {


			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				mGetMore = false;
				Message message  = new Message();
				message.what = POST_ON;
				handler.sendMessage(message);
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (mGetMore && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					//添加滚动条滚到最底部，加载余下的数据
					// 执行线程异步更新listview的数据 可采用AsyncTask

					Tip("正在加载下一页，请稍后······");
					mGetMore = false;
				}
			} });
	}


	private Runnable FreshListRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mProgressBar.setVisibility(View.VISIBLE);
			mArticleList.setClickable(false);
			if(mParser==null)
				mParser =  new ArticleParser(articleUrl,handler,
						sp.getBoolean(Property.Store_Image, true),
						sp.getBoolean(Property.Show_Image, true));
			mParser.setMode(0);
			try {
				fullText= mParser.parser();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(mAdapter!=null)mAdapter.notifyDataSetChanged();
			mProgressBar.setVisibility(View.GONE);
			mArticleList.setClickable(true);
		}

	};


	// 获取文章列表 异步执行类
	private class ArticleListload extends AsyncTask<String,Integer,Void>
	{
		@Override
		protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... URL) {
			if(mParser==null)
				mParser =  new ArticleParser(articleUrl,handler,
						sp.getBoolean(Property.Store_Image, true),
						sp.getBoolean(Property.Show_Image, true));
			try {
				fullText = mParser.parser();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mAdapter = new ArticelAdapter(ArticleActivity.this,mParser.getList() );
			mArticleList.setAdapter(mAdapter);
			if(mAdapter.isEmpty()) return;
			String article =mAdapter.getItem(0).getArticle().toString().replace("○", "");
			if(article.indexOf("<br/>")!=-1)
				article=article.substring(0,article.indexOf("<br/>"));
			article = article.trim();
			tv_posttitle.setText(Html.fromHtml(article).toString());
			mProgressBar.setVisibility(View.GONE);
			if(mAdapter.getCount()>30)
				mArticleList.setFastScrollEnabled(true);
			super.onPostExecute(result);
		}
	}




	private String newPostZhuanTie(){

		/*
		StringBuilder datas=new StringBuilder();
		datas.append("board="+board
				     +"&file=M."+reid.replace(" ", "")+".A&"
				     +"target="+mZhuanTieBoard.getText().toString());
		Log.v("=转帖datas=", datas.toString());
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add( new BasicNameValuePair("board",board));
		params.add( new BasicNameValuePair("file","M."+reid.replace(" ", "")+".A"));
		params.add( new BasicNameValuePair("target",mZhuanTieBoard.getText().toString()));

		try {
			return Net.getInstance().post("https://bbs.sjtu.edu.cn/bbsccc", params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.v("", e.toString());
			return "ERROR";
		}
	}



	/**
	 *  执行post图片
	 * @return
	 * @throws IOException
	 */
	private String newPostPic(String imagePath){

		//			java.io.File newFile=FileOperate.readPicFromSDcard("1310212944250061.jpg");
		java.io.File newFile = null;
		if(filePath==null) return "ERROR";
		try {
			newFile = FileOperate.readFromSDcardByPath(imagePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Log.v("", e1.toString());
		}
		if(newFile==null) return "ERROR";
		/*
		//获取SDCard目录
		java.io.File SDFile=android.os.Environment.getExternalStorageDirectory();
		//创建文件名，绝对路径
		java.io.File newFile=new java.io.File(SDFile.getAbsolutePath()+java.io.File.separatorChar+"bbs"+
				java.io.File.separatorChar+"image"+java.io.File.separatorChar+"1310212944250061.jpg") ;
		 */

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("MAX_FILE_SIZE","1048577"));
		params.add( new BasicNameValuePair("board",board));
		params.add( new BasicNameValuePair("level","0"));
		params.add( new BasicNameValuePair("live","180"));
		if(board.equalsIgnoreCase("comment")) {
			params.add( new BasicNameValuePair("anony","1"));
		}
		params.add( new BasicNameValuePair("exp",""));
		params.add( new BasicNameValuePair("up",newFile.getAbsolutePath()));
		params.add( new BasicNameValuePair("filename",newFile.getAbsolutePath()));

		try {
			return Net.getInstance().postFile("https://bbs.sjtu.edu.cn/bbsdoupload", params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.v("", e.toString());
			return "ERROR";
		}


	}

	/**
	 * 执行文本发帖
	 * @return
	 */
	private String newPost(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add( new BasicNameValuePair("title",replyTitleEditText.getText().toString()));
		params.add( new BasicNameValuePair("text",(sp.getBoolean(Property.Post_Prefix, true)?testPrefix:"")+
				replyContentEditText.getText().toString()+
				(sp.getBoolean(Property.Post_Tail, true)?Property.Tail:"")));
		//			params.add( new BasicNameValuePair("text",replyContentEditText.getText().toString()+(sp.getBoolean(Property.Post_Tail, true)?Property.Tail:"")));
		params.add( new BasicNameValuePair("board",board));
		params.add( new BasicNameValuePair("file",file));
		params.add( new BasicNameValuePair("reidstr",reid));
		params.add( new BasicNameValuePair("signature","1"));
		if(board.equalsIgnoreCase("comment")) 
			params.add( new BasicNameValuePair("anony","1"));
		params.add( new BasicNameValuePair("autocr","on"));
		params.add( new BasicNameValuePair("live","180"));
		params.add( new BasicNameValuePair("level","0"));
		params.add( new BasicNameValuePair("exp",""));
		params.add( new BasicNameValuePair("MAX_FILE_SIZE","1048577"));
		params.add( new BasicNameValuePair("up",""));
		try {
			return Net.getInstance().post("https://bbs.sjtu.edu.cn/bbssnd", params);
		} catch (Exception e) {
			Log.v("", e.toString());
			return "ERROR";
		}
	}

	private class DoPost extends AsyncTask<String,Integer,String>
	{	

		protected void onPreExecute() {

			Toast.makeText(getApplicationContext(),"操作中...",Toast.LENGTH_SHORT).show();

		}


		@Override
		protected String doInBackground(String... arg0) {

			String result="";

			//判断上传类型
			if(0==postType){
				result = newPost();
			}else if(1==postType){
				for (String imagepath:filePath){
					result = newPostPic(imagepath);
					if(result.contains("ERROR")){
						return result;
					}else{
						//获取上传成功后，返回的URL
						int head=result.indexOf("<font color=green>");
						int end=result.indexOf("</font>", head);
						String fileUrl=result.substring(head, end);
						head=fileUrl.indexOf("http");
						fileUrl=fileUrl.substring(head);
						Message message  = new Message();
						message.what = LOAD_IMAGE;
						Bundle b=new Bundle();
						b.putString("PostFileUrl", fileUrl);
						message.setData(b);
						handler.sendMessage(message);
					}
				}
			}else if(2==postType){
				result=newPostZhuanTie();
				Log.v("=result=", result);
			}
			if(result.contains("ERROR")){
				if(user.equalsIgnoreCase("")||pswd.equalsIgnoreCase("")){
					Message message  = new Message();
					message.what = NEED_LOGIN;
					handler.sendMessage(message);
				}else{
					Login.getInstance().login(user, pswd);
					if(0==postType){
						result = newPost();
					}else if(1==postType){
						for (String imagepath:filePath)
							result = newPostPic(imagepath);
					}
				}
			}
			return result;

		}

		@Override
		protected void onPostExecute(String result) {

			if(result.contains("ERROR")){
				Toast.makeText(getApplicationContext(),"发帖失败，需要重新登录!",Toast.LENGTH_SHORT).show();
				Message message  = new Message();
				message.what = NEED_LOGIN;
				handler.sendMessage(message);
			}
			else{
				restoreCookie();

				if(0==postType){
					handler.post(FreshListRunnable);
					Toast.makeText(getApplicationContext(),"发帖成功!",Toast.LENGTH_SHORT).show();
				}else if(2==postType){
					Toast.makeText(getApplicationContext(),"转帖成功!",Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		switch(v.getId()){
		case R.id.replysubmit:
			postType =0;
			new DoPost().execute();
			dismissDialog(DIALOG_REPLAY);
			break;
		case R.layout.dialog_reply:
			dismissDialog(DIALOG_REPLAY);
			break;
		case R.layout.dialog_login:
			dismissDialog(DIALOG_REPLAY);
			break;
		case R.id.btn_dialoglogin:
			user=userNameEditText.getText().toString();
			pswd=passwordEditText.getText().toString();
			Boolean success = Login.getInstance().login(user, pswd);
			if(success){
				Tip("登录成功!");
				restoreCookie();
				dismissDialog(DIALOG_LOGIN);
				new DoPost().execute();
			}
			else
				Toast.makeText(this,"登录失败!",Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_ariticleback:
			finish();
			break;
		case R.id.btn_articleMore:
			openContextMenu(v);
			break;
		case R.id.replyupload:
			i=new Intent();
			i.setClass(this, FileListActivity.class);
			startActivityForResult(i, 1);
			break;
		case R.id.btn_addFavorok:
			postType =2;
			new DoPost().execute();
			dismissDialog(DIALOG_ZHUANTIE);
			break;
		case R.id.btn_addFavorcancel:
			dismissDialog(DIALOG_ZHUANTIE);
			break;
		case R.id.paint:
			i=new Intent();
			i.setClass(this, PaintAcitivity.class);
			i.putExtra("board", board);
			startActivityForResult(i, 2);
			break;
		}
	}

	private Handler handler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case NEED_LOGIN:
				showDialog(DIALOG_LOGIN);
				break;
			case REFRESH_LIST:
				if(mAdapter!=null)mAdapter.notifyDataSetChanged();
				break;
			case POST_ON:
				if(mArticleList!=null){
					int l= mArticleList.getFirstVisiblePosition();
					if(l==0)
						tv_postno.setText("  沙发");
					else if(l==1){
						tv_postno.setText("  板凳");
					}else
						tv_postno.setText("  "+l+"L")	;
				}
				break;
			case LOAD_IMAGE:
				String fileUrl= msg.getData().getString("PostFileUrl");
				Log.i("PostFileUrl", fileUrl);
				replyContentEditText.append(fileUrl+"\n");
				break;
			}
		}
	};
	private  ArrayList<String> filePath;
	private int postType;

	private void restoreCookie(){
		Editor edit=Property.getPreferences(this).edit();
		edit.putString(Property.UserName, user);
		edit.putString(Property.Password, pswd);
		edit.putString(Property.Cookie, Net.getInstance().getCookie());
		edit.putLong(Property.Cookie_Time, System.currentTimeMillis()/1000);
		edit.commit();
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(1==requestCode){
			switch (resultCode){
			case Activity.RESULT_OK:
				filePath=data.getStringArrayListExtra("filePath");
				postType =1;
				new DoPost().execute();
				break;
			default:
				break;
			}
		}else if(2==requestCode&& Activity.RESULT_OK==resultCode){
			Message message  = new Message();
			message.what = LOAD_IMAGE;
			Bundle b=new Bundle();
			b.putString("PostFileUrl", data.getStringExtra("FileURL"));
			message.setData(b);
			handler.sendMessage(message);
		}
	}



}
