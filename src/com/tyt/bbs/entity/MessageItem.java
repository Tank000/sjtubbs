/**
 * 
 */
package com.tyt.bbs.entity;

import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;

/**
 * @author tyt2011
 *com.tyt.bbs.entity
 */
public class MessageItem {
	
	private String title;
	private String link;
	private String content;
	private String name;
	private Map<String, Drawable> imageCache = new HashMap<String, Drawable>();
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "title="+title+", link="+link+", content="+content+", name="+name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param imageCache the imageCache to set
	 */
	public void setImageCache(Map<String, Drawable> imageCache) {
		this.imageCache = imageCache;
	}
	/**
	 * @return the imageCache
	 */
	public Map<String, Drawable> getImageCache() {
		return imageCache;
	}

}
