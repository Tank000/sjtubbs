package com.tyt.bbs.entity;

import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;

public class ArticleItem {

	private String time;
	private String author;
	private String  article;
	private String link;
	private String page;
	private Map<String, Drawable> imageCache = new HashMap<String, Drawable>();
	
	public ArticleItem(){}
	
	public String getTime(){
		return this.time;	
	}
	
	public void setTime(String data_time){
		this.time=data_time;
	}
	
	public String getAuthor(){	
		return this.author;	
	}
	
	public void setAuthor(String data_author){
		this.author=data_author;
	}
	
	public String getArticle(){ return this.article; }	
	
	public void setArticle(String articletext){ this.article=articletext;	}		
	
	public String getLink(){ return this.link; }
	
	public void setLink(String data_link){
		this.link=data_link;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPage() {
		return page;
	}

	public void setImageCache(Map<String, Drawable> imageCache) {
		this.imageCache = imageCache;
	}

	public Map<String, Drawable> getImageCache() {
		return imageCache;
	}
	
}
