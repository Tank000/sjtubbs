/**
 * 
 */
package com.tyt.bbs.adapter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyt.bbs.R;
import com.tyt.bbs.utils.FileOperate;
import com.tyt.bbs.utils.ImageOperate;

/**
 * @author yyj2011
 *com.tyt.bbs.adapter
 */
public class ListFileAdapter extends BaseAdapter {

	private List<File> mSubFile ;
	private Map<String, Drawable> imageCache;
	private LayoutInflater mInflater;
	private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
	private Bitmap bitmap = null;
	private ArrayList<Integer> checkId ;

	public ListFileAdapter(Context context,List<File> mFileList) {
		// TODO Auto-generated constructor stub
		mInflater=LayoutInflater.from(context);
		mSubFile =mFileList;
		imageCache = new HashMap<String, Drawable>();
		checkId=new ArrayList<Integer>();
	}

	public ArrayList<Integer> getChecked(){
		return this.checkId;
	}

	public void  setCheckd(ArrayList<Integer> data){
		this.checkId=data;
	}

	public void setAllUnCheckd(){
		this.checkId.clear();
	}

	public void setAllCheckd(){
		int size=getCount();
		for(int i=0;i<size;i++)
			this.checkId.add(i);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSubFile.size();
	}

	@Override
	public File getItem(int position) {
		// TODO Auto-generated method stub
		return mSubFile.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.item_listfile, null);
			holder = new ViewHolder();
			holder.icon=(ImageView) convertView.findViewById(R.id.iv_icon);
			holder.filename=(TextView) convertView.findViewById(R.id.tv_filename);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(pos>getCount()) return convertView;
		final File file=getItem(pos);
		final String strfilename=file.getName();

		if(pos==0 && !getItem(0).equals("/")){
			holder.filename.setText("返回上一级");
		}else holder.filename.setText(strfilename);

		if(strfilename.toLowerCase().endsWith(".jpg")||strfilename.toLowerCase().endsWith(".jpeg")
				||strfilename.toLowerCase().endsWith(".png")||strfilename.toLowerCase().endsWith(".bmp")
				||strfilename.toLowerCase().endsWith(".gif")){

			holder.icon.setVisibility(View.VISIBLE);	
//			holder.icon.setImageResource(R.drawable.bg_img);

			if(imageCache.containsKey(strfilename)){
				holder.icon.setImageDrawable(imageCache.get(strfilename));
			}else{
				holder.icon.setImageResource(R.drawable.bg_img);
//				executorService.submit(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						Drawable d = null;	
//						if(bitmap!=null&&bitmap.isRecycled()) return;
//						//压缩图片到1/16
//						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//						bitmapOptions.inSampleSize=16;
//						try {
//							bitmap = ImageOperate.getFile2Bitmap(FileOperate.readFromSDcardByPath(file.getAbsolutePath()),bitmapOptions);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						//图片添加圆角
//						if(bitmap!=null) bitmap=ImageOperate.getRCB(bitmap,(float)5.0);
//						d = new BitmapDrawable(bitmap);
//						if(!imageCache.containsKey(strfilename)) 
//							imageCache.put(strfilename,d);	
//						holder.icon.setImageDrawable(d);
//						Log.i("=====", imageCache.size()+"");
//
//					}
//				});

			}


		}
		else 
			holder.icon.setVisibility(View.GONE);

		if(checkId.contains(pos))
			convertView.setBackgroundColor(R.color.delete_color_filter);
		else
			convertView.setBackgroundColor(color.transparent);
	
		return convertView;
	}
	
	

	static class ViewHolder {
		ImageView icon;
		TextView filename;
	}

	/**
	 * @return the mSubFile
	 */
	public List<File> getmSubFile() {
		return mSubFile;
	}
	/**
	 * @param mSubFile the mSubFile to set
	 */
	public void setmSubFile(List<File> mSubFile) {
		this.mSubFile = mSubFile;
	}
}
