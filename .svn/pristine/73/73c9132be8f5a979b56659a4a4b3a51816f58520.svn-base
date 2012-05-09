/**
 * 
 */
package com.tyt.bbs;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tyt.bbs.adapter.DiffMsgAdapter;
import com.tyt.bbs.adapter.MsgListAdapter;
import com.tyt.bbs.entity.MessageItem;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;
import com.tyt.bbs.view.LoadingDrawable;
import com.tyt.bbs.view.TitleFlowIndicator;
import com.tyt.bbs.view.ViewFlow;
import com.tyt.bbs.view.ViewFlow.ViewSwitchListener;

/**
 * @author tyt2011
 *com.tyt.bbs
 */
public class MessageActivity extends BaseActivity implements OnClickListener {

	private static final int DIALOG_REPLAY = 0;
	private ViewFlow viewFlow;
	private MsgListAdapter[]  mAdapter ;
	private ListView[] mgsList ;
	private ProgressBar[] pBar;
	private MessageItem msgitem;
	private EditText replaytitle;
	private EditText replyuser;
	private  EditText replycontent;
	private int onPosition=0;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mgsList = new ListView[2];
		mAdapter =new MsgListAdapter[2];
		pBar = new ProgressBar[2];
		setContentView(R.layout.a_msg);
		setViews();
	}
	
	private void setViews(){
		findViewById(R.id.btn_msgrefresh).setOnClickListener(this);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		View layout_newmsg = LayoutInflater.from(this).inflate(R.layout.layout_newsg, null);
		layout_newmsg.setOnClickListener(this);
		DiffMsgAdapter adapter = new DiffMsgAdapter(this);
		viewFlow.setAdapter(adapter, 0);
		TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewflowindic);
		indicator.setTitleProvider(adapter);
		viewFlow.setFlowIndicator(indicator);
		viewFlow.setOnViewSwitchListener(new ViewSwitchListener(){

			@Override
			public void onSwitched(View view, int position) {
				// TODO Auto-generated method stub
				onPosition=position;
				if(position==1&&mAdapter[position].getMsg()==null){
					new LoadContentTask().execute(position);
				}
			}
			
		});
		setListViews();
	}
	
	private void setListViews(){
		int[] id={R.id.lv_newmsg,R.id.lv_allmsg};
		int[] pbid={R.id.progressbar_newmsg,R.id.progressbar_allmsg};
		for(int i=0;i<2;i++){
			mgsList[i] =(ListView)findViewById(id[i]); 
			pBar[i] =(ProgressBar) findViewById(pbid[i]);
			pBar[i].setIndeterminateDrawable(new LoadingDrawable(
					0,Color.parseColor("#c6e0f2"), Color.parseColor("#337fd3"), Color.TRANSPARENT, 200));
			mAdapter[i] =new MsgListAdapter(this);
			mgsList[i].setAdapter(mAdapter[i]);
			mgsList[i].setOnItemClickListener(listener);
		}
		new LoadContentTask().execute();
	}
	
	private class LoadContentTask extends AsyncTask<Object, Object, Object> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pBar[onPosition].setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		@Override
		protected Object doInBackground(Object... arg) {
			ArrayList<MessageItem> newMsg= new ArrayList<MessageItem>();
			try {
				String url=null;
				if(onPosition==0)
					url="/bbsnewmail";
				else if(onPosition==1)
					url="/bbsmail";
				String result =  Net.getInstance().getWithCookie(Property.Base_URL+url);
					Elements es= Jsoup.parse(result).select("tr");
					for(Element e:es){
						if(es.get(0).equals(e)) continue;
						Elements es_td=e.select("td");
						MessageItem item= new MessageItem();
						if(onPosition==0){
							item.setTitle(es_td.get(4).child(0).text());
							item.setLink(es_td.get(4).select("a").first().attr("href"));
							item.setName(es_td.get(2).child(0).text());
						}else if(onPosition==1){
							item.setTitle(es_td.get(5).child(0).text());
							item.setLink(es_td.get(5).select("a").first().attr("href"));
							item.setName(es_td.get(3).child(0).text());
						}

						String content = Net.getInstance().getWithCookie(Property.Base_URL+"/"+item.getLink());
						content= Jsoup.parse(content).select("pre").text();
						int start = content.indexOf("来  源:");
						content = content.substring(content.indexOf("\n", start+6));
						content = content.replace("【 在", "<br>【 在");
						content = content.replace("】:", "】:<br>");
						content = content.replace("--", "<br><br>--");
						content = content.replace("※ 来源", "<br>※ 来源");
						item.setContent(content);
						newMsg.add(item);
					}
					mAdapter[onPosition].setMsg(newMsg);
				return newMsg;
			} catch (Exception e) {
				// TODO Auto-generated catch block

				Log.i("getMsg", e.toString());
			}
			return null;
		}

		protected void onPostExecute(Object result) {
			// process result  
			mAdapter[onPosition].notifyDataSetChanged();
			pBar[onPosition].setVisibility(View.GONE);
		}

	}	
		
	private final OnItemClickListener listener  =new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			// TODO Auto-generated method stub
			msgitem = mAdapter[onPosition].getItem(pos);
//			try {
//				Net.getInstance().getMsgMarkread(Property.Base_URL+"/"+madapter.getItem(position).getLink());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Toast.makeText(MessageActivity.this, "  "+Net.getInstance().GMTString(), 500).show();
			showDialog(DIALOG_REPLAY);
		}
		
	};


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id){
		case DIALOG_REPLAY:
			final Dialog mReplayDialog = new Dialog(this, R.style.Dialog);
			View dv = LayoutInflater.from(this).inflate(R.layout.dialog_reply_msg, null);
			dv.findViewById(R.id.msg_reply).setOnClickListener(this);
			replaytitle = (EditText) dv.findViewById(R.id.replaytitle);
			replycontent = (EditText) dv.findViewById(R.id.replycontent);
			replyuser = (EditText) dv.findViewById(R.id.replyuser);
			replaytitle.setText(msgitem.getTitle());
			replyuser.setText(msgitem.getName());
			dv.setOnClickListener(this);
			mReplayDialog.setContentView(dv);
			return mReplayDialog;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.body:
			dismissDialog(DIALOG_REPLAY);			
			break;
		case R.id.msg_reply:
			sendMsg();
			break;
		case R.id.btn_msgrefresh:
			new LoadContentTask().execute(onPosition);
			break;
		case R.id.tv_newmsg:
			showDialog(DIALOG_REPLAY);
			break;
		}
	}

	/**
	 * 
	 */
	private void sendMsg() {
		// TODO Auto-generated method stub
		List <NameValuePair> params = new ArrayList <NameValuePair>();   
		params.add(new BasicNameValuePair("title",replaytitle.getText().toString()));   
		params.add(new BasicNameValuePair("userid", replyuser.getText().toString())); 
		params.add(new BasicNameValuePair("text", replycontent.getText().toString())); 
		try {
			String sourceString = Net.getInstance().post(Property.Base_URL+"/bbssndmail", params);
			Log.v("===SendMsg===", sourceString);
			if(sourceString.contains("信件已寄给")){
				Toast.makeText(this, R.string.sendmsg_sucess, 200).show();
				dismissDialog(DIALOG_REPLAY);
			}
			else
				Toast.makeText(this, R.string.sendmsg_fail, 200).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, R.string.sendmsg_error, 200).show();
		}
	}
	
	

}
