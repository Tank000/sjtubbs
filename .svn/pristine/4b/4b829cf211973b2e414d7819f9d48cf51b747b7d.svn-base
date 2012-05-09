/**
 * 
 */
package com.tyt.bbs.utils;

import java.io.File;
import java.io.FilenameFilter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author yyj2011
 *com.tyt.bbs.utils
 */
public class ImageOperate {
	
	public static Bitmap getFile2Bitmap(File imageFile,BitmapFactory.Options option){
		
		Bitmap bitmap=null;
		if(option!=null){
			 bitmap=BitmapFactory.decodeFile(imageFile.getAbsolutePath(),option);
		}
		else 
			bitmap=BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		
		 return bitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成 bitmap 
    {
              int width =  (int)(drawable.getIntrinsicWidth()*Property.Ratio);   // 取 drawable 的长宽 
              int height = (int)(drawable.getIntrinsicHeight()*Property.Ratio);
              Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;         // 取 drawable 的颜色格式 
              Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应 bitmap 
              Canvas canvas = new Canvas(bitmap);         // 建立对应 bitmap 的画布 
              drawable.setBounds(0, 0, width, height);
              drawable.draw(canvas);      // 把 drawable 内容画到画布中 
              return bitmap;
    }
	
	public  static Drawable zoomDrawable(Drawable drawable)
    {
              int width = (int)(drawable.getIntrinsicWidth()*Property.Ratio);
              int height= (int)(drawable.getIntrinsicHeight()*Property.Ratio);
      		Log.i("===Drawable===", " width="+width+"    height="+height+"  Ratio="+Property.Ratio);
              if(width>(Property.ScreenWidth ==0?320:Property.ScreenWidth )){
                  Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap 
                  Matrix matrix = new Matrix();   // 创建操作图片用的 Matrix 对象 
                  float scaleWidth = ((float)(Property.ScreenWidth ==0?320:Property.ScreenWidth )/ width);   // 计算缩放比例 
                  float scaleHeight =scaleWidth;
                  matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例 
                  Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图 
                  return new BitmapDrawable(newbmp);       // 把 bitmap 转换成 drawable 并返回 
              }else{
            	  return drawable;
              }
    }
	
	public static Bitmap imageScale(Bitmap bitmap, int dst_w){
		  
		  int  src_w = bitmap.getWidth();
		  int  src_h = bitmap.getHeight();
		  float scale_w = ((float)dst_w)/src_w;
		  float  scale_h = ((float)src_h)*scale_w;
		  Log.v("放大比例", scale_w+"");
		  Matrix  matrix = new Matrix();
		  matrix.postScale(scale_w, scale_h);
		  Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix, true);
		  
		  return dstbmp;
		 }
	
	public static class ImageFilenameFilter implements FilenameFilter {

		/**
		 * 文件夹过滤器
		 */
		public boolean isImage(String file) {    
		    if (file.toLowerCase().endsWith(".jpeg")
		    		||file.toLowerCase().endsWith(".jpg")
		    		||file.toLowerCase().endsWith(".bmp")
		    		||file.toLowerCase().endsWith(".png")
		    		||file.toLowerCase().endsWith(".gif")){    
		      return true;    
		    }
		    else{    
		      return false;    
		    }    
		  }     

		public ImageFilenameFilter() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return isImage(filename)||dir.isDirectory();
		}

	}
	
	public static Bitmap getRCB(Bitmap bitmap, float roundPX) //RCB means Rounded Corner Bitmap
	{
		Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(dstbmp);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return dstbmp;
	}
	

}
