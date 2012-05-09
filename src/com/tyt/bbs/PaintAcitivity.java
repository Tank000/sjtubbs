/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tyt.bbs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tyt.bbs.dialog.ColorPickerDialog;
import com.tyt.bbs.dialog.LoginDialog;
import com.tyt.bbs.utils.FileOperate;
import com.tyt.bbs.utils.ImageOperate;
import com.tyt.bbs.utils.Net;
import com.tyt.bbs.utils.Property;

public class PaintAcitivity extends BaseActivity implements ColorPickerDialog.OnColorChangedListener, OnClickListener {    

	private static final int DIALOG_LOGIN = 0;
	MyView myView;
	private Handler handler = new Handler();
	String board;
	LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_finger);
		layout = (LinearLayout) findViewById(R.id.layout_finger);
		myView = new MyView(this,getResources().getDisplayMetrics());
		layout.addView(myView);
		View paint =  findViewById(R.id.btn_paint);
		registerForContextMenu(paint);
		paint.setOnClickListener(this);
		findViewById(R.id.btn_color).setOnClickListener(this);
		findViewById(R.id.btn_upimg).setOnClickListener(this);
		findViewById(R.id.btn_photo).setOnClickListener(this);

		board = getIntent().getStringExtra("board");

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xff337fd3);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(12);

		mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
				0.4f, 6, 3.5f);

		mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
	}

	private Paint       mPaint;
	private MaskFilter  mEmboss;
	private MaskFilter  mBlur;

	public void colorChanged(int color) {
		mPaint.setColor(color);
	}

	public class MyView extends View {

		private static final float MINP = 0.25f;
		private static final float MAXP = 0.75f;

		Bitmap  mBitmap;
		private Canvas  mCanvas;
		private Path    mPath;
		private Paint   mBitmapPaint;

		public MyView(Context c, DisplayMetrics dm) {
			super(c);
			setBackgroundResource(R.drawable.bg_dark);
			mBitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

			canvas.drawPath(mPath, mPaint);
		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}
		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
				mX = x;
				mY = y;
			}
		}
		private void touch_up() {
			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw
			mPath.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getRawX();
			float y = event.getRawY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
			return true;
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,  
			ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo); 
		menu.setHeaderTitle("画笔选择");
		getMenuInflater().inflate(R.menu.paint_cm, menu);
	}  

	public boolean onContextItemSelected(MenuItem item) {  
		mPaint.setXfermode(null);
		mPaint.setAlpha(0xFF);
		switch (item.getItemId()) {  
		case R.id.m_normal:
			mPaint.setMaskFilter(null);
			return true;
		case R.id.m_emboss:
			if (mPaint.getMaskFilter() != mEmboss) {
				mPaint.setMaskFilter(mEmboss);
			} else {
				mPaint.setMaskFilter(null);
			}
			return true;
		case R.id.m_blur:
			if (mPaint.getMaskFilter() != mBlur) {
				mPaint.setMaskFilter(mBlur);
			} else {
				mPaint.setMaskFilter(null);
			}
			return true;
		case  R.id.m_erase:
			mPaint.setXfermode(new PorterDuffXfermode(
					PorterDuff.Mode.CLEAR));
			return true;
		case  R.id.m_srcatop:
			mPaint.setXfermode(new PorterDuffXfermode(
					PorterDuff.Mode.SRC_ATOP));
			mPaint.setAlpha(0x80);
			return true;
		default:  
			return super.onContextItemSelected(item);  
		}
	}


	/* (按钮监听)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 所有按钮的实现功能
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_paint:
			openContextMenu(v);
			break;
		case R.id.btn_color:
			new ColorPickerDialog(this, this, mPaint.getColor()).show();
			break;
		case R.id.btn_photo:
			Toast.makeText(this, "该功能暂无！", 500).show();
			break;
		case R.id.btn_upimg:
			handler.post(upPaint);
			break;
		}
	}

	private Runnable upPaint =new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			myView.setBackgroundColor(0);
			myView.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
			myView.setDrawingCacheEnabled(true);
			myView.buildDrawingCache();
			Bitmap b = myView.getDrawingCache();
			if(b!=null){
				String result =newPostPic(b);
				if(result.contains("ERROR")){
						showDialog(DIALOG_LOGIN);
				}else{
					//获取上传成功后，返回的URL
					int head=result.indexOf("<font color=green>");
					int end=result.indexOf("</font>", head);
					String fileUrl=result.substring(head, end);
					head=fileUrl.indexOf("http");
					fileUrl=fileUrl.substring(head);
					Toast.makeText(PaintAcitivity.this, "上传图片成功"+fileUrl, 200).show();
					Intent it=new Intent();
					it.putExtra("FileURL", fileUrl);
					setResult(Activity.RESULT_OK, it);
					finish();
				}
			}
		}

	};


	/**
	 *  执行post图片
	 * @return
	 * @throws IOException
	 */
	private String newPostPic(Bitmap bmp){

//       Bitmap newbmp =ImageOperate.imageScale(bmp, 320);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		File file = FileOperate.inputstreamtofile(is);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("MAX_FILE_SIZE","1048577"));
		params.add( new BasicNameValuePair("board",board));
		params.add( new BasicNameValuePair("level","0"));
		params.add( new BasicNameValuePair("live","180"));
		params.add( new BasicNameValuePair("exp",""));
		params.add( new BasicNameValuePair("up",""));
		params.add( new BasicNameValuePair("filename",file.getName()));

		try {
			return Net.getInstance().postFile(params,file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.v("", e.toString());
			return "ERROR";
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id){
		case DIALOG_LOGIN:
			return new LoginDialog(this, R.style.Dialog);
		default :
			return null;
		}
	}

	
}
