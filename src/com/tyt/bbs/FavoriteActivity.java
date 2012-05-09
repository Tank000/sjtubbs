/**
 * 
 */
package com.tyt.bbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tyt.bbs.adapter.FavAdapter;
import com.tyt.bbs.utils.FileOperate;
import com.tyt.bbs.utils.Login;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;
import com.tyt.bbs.utils.XmlOperate;

/**
 * @author tyt2011
 *com.tyt.bbs
 */
public class FavoriteActivity extends BaseActivity implements OnClickListener{

	protected  final String TAG = "FavoriteActivity";

	protected  final int NEED_LOGIN = 0x2;
	private  final int FAV_ADD=0x0;
	private  final int FAV_DEL=0x1; 

	private  final int DIALOG_ADDFAVOR=0x0;
	private  final int DIALOG_LOGIN = 0x1;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private String URL="https://bbs.sjtu.edu.cn/bbsshowhome?boardfile=main.hck";
	private String user,pswd;
	private ListView ls_Board;
	private FavAdapter mAdapter;
	private AutoCompleteTextView mAddBoard;
	private ArrayList<String>  mDelBoardList;
	private Button btn_selectall;
	private View layout;
	private EditText passwordEditText,userNameEditText;


	//标志按钮动作    0添加版区    1删除版区
	private int       mAction;
	//标志全选还是全部选    0全选  1全部选
	private int       mSelectAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_favorite);
		initialView();
	}
	private void initialView(){
		user = Property.getPreferences(this).getString(Property.UserName, "");
		pswd = Property.getPreferences(this).getString(Property.Password, "");
		if(user.equalsIgnoreCase("")) user="Guest";
		((TextView)findViewById(R.id.usrname)).setText(user);
		((TextView)findViewById(R.id.tv_boardinfo)).setText("正在载入······");
		findViewById(R.id.btn_addFav).setOnClickListener(this);
		registerForContextMenu(findViewById(R.id.btn_addFav));
		btn_selectall = (Button) findViewById(R.id.btn_delFavAll);
		layout = findViewById(R.id.layout_delfav);
		btn_selectall.setOnClickListener(this);
		findViewById(R.id.btn_delFavOK).setOnClickListener(this);

		mSelectAll=0;

		ls_Board = (ListView) findViewById(R.id.lv_fav);
		ls_Board.setVisibility(View.INVISIBLE);
		ls_Board.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> ap, View v, int pos,
					long id) {
				// TODO Auto-generated method stub


				if(mAdapter.getMode()){
					ArrayList<Integer> checkId=mAdapter.getChecked();

					if(!checkId.contains(pos))
						checkId.add(new Integer(pos));
					else if(checkId.contains(pos))
						checkId.remove(new Integer(pos));
					mAdapter.setCheckd(checkId);
					mAdapter.notifyDataSetChanged();

					Message message  = new Message();
					message.what = 1;
					mHandler.sendMessage(message);

				}
				else{
					Intent it = new Intent(FavoriteActivity.this,PostsListActivity.class);
					it.putExtra(Property.Boards,mAdapter.getItem(pos));
					startActivity(it);

				}


			}

		});

		new Thread(loadingRunnable).start();
	}

	private Runnable loadingRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			java.io.File xmlFile = null;
			try {
				xmlFile = FileOperate.readFromSDcard("favlist.xml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "##############################");
				Log.e("TopTen",e.getMessage());
				Log.e("TopTen", "##############################");
			}
			if(xmlFile!=null){
				loadFavList(xmlFile);
				Message message  = new Message();
				message.what = 0;
				mHandler.sendMessage(message);
			}
			if(!user.contentEquals("Guest")){
				refreshFavList();
				Message message  = new Message();
				message.what = 0;
				mHandler.sendMessage(message);
			}
		}
	};

	private void loadFavList(java.io.File xmlFile){
		try{
			Document doc=null;
			DocumentBuilderFactory fac=DocumentBuilderFactory.newInstance();
			fac.setCoalescing(true);
			fac.setIgnoringComments(true);
			fac.setIgnoringElementContentWhitespace(true);
			DocumentBuilder bui = fac.newDocumentBuilder();	 
			doc=bui.parse(xmlFile);
			NodeList itemAll = doc.getElementsByTagName("item");
			int length = itemAll.getLength();
			ArrayList<String> post = new ArrayList<String>();
			for(int i=0;i<length;i++){
				Node itemone =itemAll.item(i);
				post.add(itemone.getFirstChild().getNodeValue());
			}

			mAdapter = new FavAdapter(FavoriteActivity.this, post,mHandler); 
		}catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String getFromAssets(String fileName){ 
		try { 
			InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) ); 
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		return null;
	} 

	private void refreshFavList(){
		String file=null;
		try {
			file=Net.getInstance().getWithCookie(URL);
		if(file!=null){
			file=Jsoup.parse(file).text();
			Log.v(TAG,file);
			file=file.replace("饮水思源BBS-个人收藏夹 序号 讨论区名称 类别 删除","");
			ArrayList<String> post = new ArrayList<String>();
			for(int i=0;i<100;i++){
				int d1=file.indexOf(""+(i+1));
				int d2=file.indexOf("讨论区",d1);
				if(d1==-1) break;
				String ftmp = file.substring(d1+(" "+i).length(), d2);
				if(!ftmp.contains("目录")) {
					post.add(ftmp.trim());  
				}
			}

			mAdapter = new FavAdapter(FavoriteActivity.this, post,mHandler); 
			if(!post.isEmpty()){
				String xmString=XmlOperate.getInstance().writeFavXml(post);

				try {
					if(FileOperate.writeToSDcard("favlist.xml", xmString)){
						Log.i("XMLFileWrite", "success");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, "##############################");
					Log.e("TopTen",e.getMessage());
					Log.e("TopTen", "##############################");
				}
			}
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

	private Handler mHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case FAV_ADD:
				ls_Board.setAdapter(mAdapter);  
				ls_Board.setVisibility(View.VISIBLE);
				((TextView)findViewById(R.id.tv_boardinfo)).setText("板块收藏");
				break;
			case FAV_DEL:
				int size =mAdapter.getChecked().size();
				if(size!=0){
					btn_selectall.setText("全选("+size+")");
					layout.setVisibility(View.VISIBLE);
				}else{
					btn_selectall.setText("全选");
					layout.setVisibility(View.GONE);
					//					mAdapter.setMode(!mAdapter.getMode());
					//					mAdapter.notifyDataSetChanged();
					ls_Board.setAdapter(mAdapter);
				}
				//				ls_Board.setAdapter(mAdapter);  
				break;
			case NEED_LOGIN:
				showDialog(DIALOG_LOGIN);
				break;
			}
		}
	};
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId()){
		case R.id.btn_addFav:
			openContextMenu(v);
			break;
		case R.id.btn_addFavorok:
			if(user.contentEquals("Guest")){

				Message message  = new Message();
				message.what = NEED_LOGIN;
				mHandler.sendMessage(message);

			}
			else{
				new DoGet().execute();		
			}
			mAction=0;
			dismissDialog(DIALOG_ADDFAVOR);
			break;
		case R.id.btn_addFavorcancel:
			dismissDialog(DIALOG_ADDFAVOR);
			break;
		case R.id.btn_delFavAll:
			if(0==mSelectAll){
				mAdapter.setAllCheckd();
				mSelectAll=1;
			}  	
			else if(1==mSelectAll){
				mAdapter.setAllUnChecked();
				mSelectAll=0;
			}

			mAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_delFavOK:
			if(user.contentEquals("Guest")){

				Message message  = new Message();
				message.what = NEED_LOGIN;
				mHandler.sendMessage(message);	
			}
			else{
				new DoGet().execute();
			}
			ArrayList<Integer> checked=mAdapter.getChecked();
			Log.i("DelBoard", checked.size()+"");
			mDelBoardList=new ArrayList<String>();
			int size=checked.size();
			for(int i=0;i<size;i++){
				mDelBoardList.add(mAdapter.getItem(checked.get(i)));
			}
			mAction=1;
			break;
		case R.id.btn_dialoglogin:
			user=userNameEditText.getText().toString();
			pswd=passwordEditText.getText().toString();
			Boolean success = Login.getInstance().login(user, pswd);
			if(success){
				Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_SHORT).show();
				restoreCookie();
				dismissDialog(DIALOG_LOGIN);
				((TextView)findViewById(R.id.usrname)).setText(user);
				new DoGet().execute();
			}
			else
				Toast.makeText(this,"登录失败!",Toast.LENGTH_SHORT).show();
			break;
		default:
			break;

		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,  
			ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo); 
		menu.setHeaderTitle("收藏管理");
		getMenuInflater().inflate(R.menu.fav_cm, menu);
	}  

	public boolean onContextItemSelected(MenuItem item) {  
		switch (item.getItemId()) {  
		case R.id.m_add:
			showDialog(DIALOG_ADDFAVOR);
			return true;
		case R.id.m_delete:

			mAdapter.setMode(!mAdapter.getMode());
			mAdapter.notifyDataSetChanged();
			return true;
		default:  
			return super.onContextItemSelected(item);  
		}  
	} 

	protected Dialog onCreateDialog(int id) {
		switch(id){

		case DIALOG_ADDFAVOR:
			final Dialog mDialog = new Dialog(this,R.style.Dialog);
			View dv = LayoutInflater.from(this).inflate(R.layout.dialog_addfavor, null);
			dv.findViewById(R.id.btn_addFavorok).setOnClickListener(this);
			dv.findViewById(R.id.btn_addFavorcancel).setOnClickListener(this);
			mAddBoard=(AutoCompleteTextView) dv.findViewById(R.id.autoCT_addFavor);
			ArrayAdapter<String> boadersadapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.boards));
			mAddBoard.setAdapter(boadersadapter);
			mDialog.setContentView(dv);
			return mDialog;
		case DIALOG_LOGIN:
			final Dialog loginDialog =  new Dialog(this, R.style.Dialog);
			View loginLayout  = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
			userNameEditText = (EditText)loginLayout.findViewById(R.id.UserNameEditText);
			passwordEditText = (EditText)loginLayout.findViewById(R.id.PasswordEditText);
			userNameEditText.setText(user);
			passwordEditText.setText(pswd);
			loginLayout.findViewById(R.id.btn_dialoglogin).setOnClickListener(this);
			loginLayout.setOnClickListener(this);
			loginDialog.setContentView(loginLayout);
			return loginDialog;

		default:
			return null;
		}

	}

	private class DoGet extends AsyncTask<String,Integer,String>{

		protected void onPreExecute() {

			Toast.makeText(getApplicationContext(),"操作中...",Toast.LENGTH_SHORT).show();

		}


		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("AddFavor", "doInBackground");
			String result="";
			if(0==mAction){
				result=newGetAddFav();
				if(result.contains("ERROR")){

				}
				else{
					refreshFavList();
				}
			}
			else if(1==mAction){
				String delBoard="";
				int size=mDelBoardList.size();
				for(int i=0;i<size;i++){
					delBoard=mDelBoardList.get(i);
					result=newGetDelFav(delBoard);
					if(result.contains("ERROR")){

					}
					else{
						refreshFavList();
					}
				}

			}

			return result;
		}



		protected void onPostExecute(String result) {
			//			Log.i("AddFavor Get", result);
			Message message  = new Message();
			String action="";
			if(0==mAction){ 
				action="添加";
				message.what = FAV_ADD;
			}
			else if(1==mAction){ 
				action="删除";
				message.what = FAV_DEL;
				mAdapter.setAllUnChecked();
			}
			if(result.contains("ERROR")){
				Toast.makeText(getApplicationContext(),action+"版区失败",Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(),action+"版区成功",Toast.LENGTH_SHORT).show();
				mHandler.sendMessage(message);
			}

			super.onPostExecute(result);
		}


		private String newGetAddFav(){

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("newboard",mAddBoard.getText().toString()));
			params.add(new BasicNameValuePair("select","0"));
			params.add(new BasicNameValuePair("boardfile","main.hck"));

			try {
				return Net.getInstance().get("https://bbs.sjtu.edu.cn/bbsshowhome", params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.v("", e.toString());
				return "ERROR";
			}
		}


		private String newGetDelFav(String delBoard){

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("boardfile","main.hck"));
			params.add(new BasicNameValuePair("targetfile","main.hck"));
			params.add(new BasicNameValuePair("delboard",delBoard));

			try {
				return Net.getInstance().get("https://bbs.sjtu.edu.cn/bbsshowhome", params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.v("", e.toString());
				return "ERROR";
			}
		}

	}

	private void restoreCookie(){
		Editor edit=Property.getPreferences(this).edit();
		edit.putString(Property.UserName, user);
		edit.putString(Property.Password, pswd);
		edit.putString(Property.Cookie, Net.getInstance().getCookie());
		edit.putLong(Property.Cookie_Time, System.currentTimeMillis()/1000);
		edit.commit();
	}

}
