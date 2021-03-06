﻿package com.tyt.bbs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class FileOperate {

	static String SDState = android.os.Environment.getExternalStorageState();
	/**
	 * 将String类型的文本以指定的文件名写入SDCard中
	 * @param fileName
	 *        指定的文件名
	 * @param txt
	 *        要写入卡中的String文本
	 * @return
	 *        成功写入   返回true
	 *        失败             返回false
	 * @throws IOException
	 */
	public static boolean writeToSDcard(String fileName,String txt) throws IOException{

		//获取SDcard的状态


		//SDCard为可写入状态
		if(SDState.equals(android.os.Environment.MEDIA_MOUNTED)){

			//获取SDCard目录
			java.io.File SDFile=android.os.Environment.getExternalStorageDirectory();
			//创建文件夹目录
			java.io.File desDir=new java.io.File(SDFile.getAbsolutePath()+java.io.File.separatorChar+"bbs");
			//文件夹不存在则创建新文件夹
			if(!desDir.exists()) desDir.mkdir();
			//创建文件
			java.io.File newFile=new java.io.File(desDir.getAbsolutePath()+java.io.File.separatorChar+fileName) ;
			if(!newFile.exists()) newFile.createNewFile();

			//String文本写入新文件中
			FileOutputStream outPut=new FileOutputStream(newFile);
			outPut.write(txt.getBytes());
			outPut.close();

			return true;
		}
		else 
			return false;
	}

	/**
	 * 从SDCard中读取文件
	 * @param fileName
	 *        文件名
	 * @return   
	 *      若目标文件存在  ，返回 目标文件
	 *      否则  返回 NULL    
	 * @throws IOException
	 */
	public static Drawable getDrawableFromSDcard(String fileName) throws IOException{

		//获取SDcard的状态
		String SDState = android.os.Environment.getExternalStorageState();

		//可读写状态
		if(SDState.equals(android.os.Environment.MEDIA_MOUNTED)
				||SDState.equals(android.os.Environment.MEDIA_MOUNTED)){

			//获取SDCard目录
			java.io.File SDFile=android.os.Environment.getExternalStorageDirectory();
			//创建文件名，绝对路径
			java.io.File newFile=new java.io.File(SDFile.getAbsolutePath()+java.io.File.separatorChar+"bbs"+
					java.io.File.separatorChar+"image"+java.io.File.separatorChar+fileName) ;

			if(newFile.exists()){

				FileInputStream inputStream = new FileInputStream(newFile);
				 byte[] buffer = new byte[1024];
                 inputStream.read(buffer);
                 Drawable d= Drawable.createFromStream(inputStream, fileName);
                 inputStream.close();
				 return d;
			}	
		}
		return null;

	}

	/**
	 * 从SDCard中读取文件
	 * @param fileName
	 *        文件名
	 * @return   
	 *      若目标文件存在  ，返回 目标文件
	 *      否则  返回 NULL    
	 * @throws IOException
	 */
	public static java.io.File readFromSDcard(String fileName) throws IOException{

		//获取SDcard的状态
		String SDState = android.os.Environment.getExternalStorageState();

		//可读写状态
		if(SDState.equals(android.os.Environment.MEDIA_MOUNTED)
				||SDState.equals(android.os.Environment.MEDIA_MOUNTED)){

			//获取SDCard目录
			java.io.File SDFile=android.os.Environment.getExternalStorageDirectory();
			//创建文件名，绝对路径
			java.io.File newFile=new java.io.File(SDFile.getAbsolutePath()+java.io.File.separatorChar+"bbs"+
					java.io.File.separatorChar+fileName) ;

			if(newFile.exists()){

				return newFile;

				/*FileInputStream inputStream = new FileInputStream(newFile);
				 byte[] buffer = new byte[1024];
                 inputStream.read(buffer);
                 inputStream.close();
				 return true;*/
			}	
		}
		else return null;

		return null;

	}

	
	public static java.io.File readFromSDcardByPath(String filePath) throws IOException{

		//获取SDcard的状态
		String SDState = android.os.Environment.getExternalStorageState();

		//可读写状态
		if(SDState.equals(android.os.Environment.MEDIA_MOUNTED)
				||SDState.equals(android.os.Environment.MEDIA_MOUNTED)){

			//获取SDCard目录
			java.io.File SDFile=android.os.Environment.getExternalStorageDirectory();
			//创建文件名，绝对路径
			java.io.File newFile=new java.io.File(filePath) ;

			if(newFile.exists()){

				return newFile;

				/*FileInputStream inputStream = new FileInputStream(newFile);
				 byte[] buffer = new byte[1024];
                 inputStream.read(buffer);
                 inputStream.close();
				 return true;*/
			}	
		}
		else return null;

		return null;

	}


	/**
	 * 从SDCard里读取图片，返回为BITMAP
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static java.io.File readPicFromSDcard(String fileName) throws IOException{

		//获取SDcard的状态
		String SDState = android.os.Environment.getExternalStorageState();

		//可读写状态
		if(SDState.equals(android.os.Environment.MEDIA_MOUNTED)
				||SDState.equals(android.os.Environment.MEDIA_MOUNTED)){

			//获取SDCard目录
			java.io.File SDFile=android.os.Environment.getExternalStorageDirectory();
			//创建文件名，绝对路径
			java.io.File newFile=new java.io.File(SDFile.getAbsolutePath()+java.io.File.separatorChar+"bbs"+
					java.io.File.separatorChar+"image"+java.io.File.separatorChar+fileName) ;

			if(newFile.exists()){

				//				Bitmap bitmap=BitmapFactory.decodeFile(newFile.getAbsolutePath());
				//				 return bitmap;

				return newFile;

			}	
		}
		else return null;

		return null;

	}

	/**
	 * 图片写入SDCard
	 * @param fileName
	 *        存入的图片名
	 * @param bitmap
	 *        存入的图片 格式为bitmap
	 * @return
	 * @throws IOException
	 */
	public static boolean writePicToSDcard(String fileName,Bitmap bitmap) throws IOException{

		//获取SDcard的状态
		String SDState = android.os.Environment.getExternalStorageState();

		//SDCard为可写入状态
		if(SDState.equals(android.os.Environment.MEDIA_MOUNTED)){

			//获取SDCard目录
			java.io.File SDFile=android.os.Environment.getExternalStorageDirectory();
			//创建文件夹目录
			java.io.File desDir=new java.io.File(SDFile.getAbsolutePath()+java.io.File.separatorChar+"bbs"
					+java.io.File.separatorChar+"image");
			//文件夹不存在则创建新文件夹
			if(!desDir.exists()) desDir.mkdir();
			//创建文件
			java.io.File newFile=new java.io.File(desDir.getAbsolutePath()+java.io.File.separatorChar+fileName) ;
			//			Log.i("writePicToSDcard FileName", newFile.getAbsolutePath());
			//			Log.i("writePicToSDcard", ""+newFile.exists());
			if(!newFile.exists()){ 
				//				Log.i("writePicToSDcard", "new File Createding");
				boolean flag=newFile.createNewFile();
				Log.i("writePicToSDcard", "new File Createding Result "+flag);

				//Bitmap bitmap写入新文件中
				FileOutputStream outPut=new FileOutputStream(newFile);
				if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, outPut)){
					outPut.flush();
					outPut.close();
					Log.i("writePicToSDcard", "bitmap.compress Success!");
					return true;
				}else{
					outPut.close();
					Log.i("writePicToSDcard", "bitmap.compress Failed!");
					return false;
				}
			}else{
				return true;
			}

		}else 
			return false;
	}



	/*
	public static void Write(String path,String txt,Context context){ 

		OutputStream os;
		try {
			os = context.openFileOutput(path, Context.MODE_PRIVATE);
			OutputStreamWriter osw=new OutputStreamWriter(os);
			osw.write(txt);
			osw.close();
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

	public static File inputstreamtofile(InputStream ins) {
		//SDCard为可写入状态
		if(SDState.equals(android.os.Environment.MEDIA_MOUNTED)){
			try {
				//获取SDCard目录
				java.io.File SDFile=android.os.Environment.getExternalStorageDirectory();
				//创建文件夹目录
				java.io.File desDir=new java.io.File(SDFile.getAbsolutePath()+java.io.File.separatorChar+"bbs");
				//文件夹不存在则创建新文件夹
				if(!desDir.exists()) desDir.mkdir();
				//创建文件
				java.io.File newFile=new java.io.File(desDir.getAbsolutePath()+java.io.File.separatorChar+"myPaint.png") ;
				if(!newFile.exists()) newFile.createNewFile();
				OutputStream os = new FileOutputStream(newFile);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				ins.close();
				return newFile;
			} catch (Exception e) {
				Log.i("inputstreamtofile", e.toString());
			}
		}
		return null;
	}

}
