/**
 * 
 */
package com.tyt.bbs.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.Drawable;
import android.util.Log;


/**
 * @author yyj2011
 *com.tyt.bbs.utils
 */
public class ImageLoader{
	private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
	
	public Drawable loadDrawable(final String imageUrl,final Boolean isStore,final Map<String, Drawable> imageCache,final ImageCallback callback) {
		// TODO Auto-generated method stub
		executorService.submit(new Runnable() {
			public void run() {
				try {
					String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1,imageUrl.length());
					Log.i("ImageLoader", "==图片=="+filename);
					 Drawable drawable = null;
					if(isStore) drawable = FileOperate.getDrawableFromSDcard(filename);
					if(drawable==null) {
						InputStream is  = new URL(imageUrl).openStream();
						drawable =Drawable.createFromStream(is,	filename); 
						is.close();
					}
					if(isStore)FileOperate.writePicToSDcard(filename, ImageOperate.drawableToBitmap(drawable));
					//信息放入缓存中
					final Drawable d= ImageOperate.zoomDrawable(drawable);
					if(d!=null)
						imageCache.put(imageUrl, d);
					else
						Log.i("ImageLoader", "==图片 Null==");
					callback.imageLoaded(d);
				} catch (Exception e) {
					Log.i("==ImageLoader==", e.toString());
				}
			}
		});
		return null;
	}
	
	//对外界开放的回调接口
    public interface ImageCallback {
        //注意 此方法是用来设置目标对象的图像资源
        public void imageLoaded(Drawable imageDrawable);
    }
 
}
